package org.eriksandsten.devtools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eriksandsten.devtools.request.page.EnableRequest;
import org.eriksandsten.devtools.request.page.NavigateRequest;
import org.eriksandsten.devtools.request.runtime.EvaluateRequest;
import org.eriksandsten.devtools.request.target.*;
import org.eriksandsten.devtools.response.page.EnableResponse;
import org.eriksandsten.devtools.response.page.NavigateResponse;
import org.eriksandsten.devtools.response.runtime.EvaluateResponse;
import org.eriksandsten.devtools.response.target.*;
import org.eriksandsten.utils.MediaHelper;
import org.eriksandsten.utils.Win32Helper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ChromeClient {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private Win32Helper win32Helper = new Win32Helper();

    private final String mediaServerPageChannelTitle;
    private final String chromeDevToolsJsonUrl;
    private final String chromeWindowClassName;
    private MediaServerWebSocketClient chromeWSClient;
    private ChromeSession currentSession;

    public ChromeClient(@Value("${media_server_page_channel_title}") String mediaServerPageChannelTitle,
                        @Value("${chrome_devtools_json_url}") String chromeDevToolsJsonUrl,
                        @Value("${chrome_window_class_name}") String chromeWindowClassName) {

        this.mediaServerPageChannelTitle = mediaServerPageChannelTitle;
        this.chromeDevToolsJsonUrl = chromeDevToolsJsonUrl;
        this.chromeWindowClassName = chromeWindowClassName;
    }

    public void loadPage(String url) throws IOException, URISyntaxException, InterruptedException {
        loadPage(url, false);
    }

    public void loadPage(String url, boolean hasAttemptedToStartNewChrome) throws IOException, URISyntaxException, InterruptedException {
        try {
            chromeWSClient = connectToChrome().webSocketClient();
            final ChromeSession session = createNewOrGetExistingSession(url);
            activateMediaServerChannelChromeWindow();

            if (session.createdBrandNewSession()) {
                // chromeWSClient.addCallback("Page.loadEventFired", (params) -> setSessionWindowTitle(session, url));
            } else {
                // chromeWSClient.addCallback("Page.loadEventFired", (params) -> setSessionWindowTitle(session, url));
                navigateToUrl(session.sessionId(), url);
            }
        } catch (final ConnectException e) {
            if (!hasAttemptedToStartNewChrome) {
                MediaHelper.startNewChromeInstance(); // Attempt to start a new Chrome instance (only one time)
                // createNewOrGetExistingSession(url); // Channel page could have been saved and now re-opened from the previous session - use it
                Thread.sleep(10000); // TODO: fix!
                System.out.println("==================================== APA =======================================");
                loadPage(url, true);
            }
        }
    }

    public Optional<EvaluateResponse> evaluateJavascript(String javascript) throws IOException, URISyntaxException, InterruptedException {
        chromeWSClient = connectToChrome().webSocketClient();
        return getExistingSession().map((session) -> {
            EvaluateResponse evaluateResponse = (EvaluateResponse) chromeWSClient.executeCommand(
                    new EvaluateRequest(session.sessionId(), javascript), EvaluateResponse.class);

            return evaluateResponse;
        });
    }

    private void navigateToUrl(String sessionId, String url) {
        NavigateResponse navigateResponse = (NavigateResponse) chromeWSClient.executeCommand(new NavigateRequest(sessionId, url), NavigateResponse.class);
        setSessionWindowTitle(sessionId, url);
    }

    private void setSessionWindowTitle(String sessionId, String url) {
        // Allente sets the window title after some seconds, override it by setting it later
        IntStream.range(0, 5).forEach(iteration -> {
                // TODO: should iterate 3 times, bug with queue.take()
                int a = iteration;
                // Thread.sleep(1000);
                EvaluateResponse setDocumentTitleResponse = (EvaluateResponse) chromeWSClient.executeCommand(
                        new EvaluateRequest(sessionId, "document.title = '%s [%s]';".formatted(mediaServerPageChannelTitle, url.substring(url.lastIndexOf("/")))), EvaluateResponse.class);

        });
    }

    private Optional<ChromeSession> getExistingSession() {
        final Optional<GetTargetsResponse.TargetInfo> existingChannelTarget = getExistingChannelTarget();

        if (existingChannelTarget.isPresent()) {
            final String sessionId = attachToAndActivateTarget(existingChannelTarget.get().getTargetId());
            final EnableResponse enableResponse = (EnableResponse) chromeWSClient.executeCommand(new EnableRequest(sessionId), EnableResponse.class);

            currentSession = new ChromeSession(sessionId, false);
            return Optional.of(currentSession);
        } else {
            return Optional.empty();
        }
    }

    private ChromeSession createNewOrGetExistingSession(String url) {
        final Optional<GetTargetsResponse.TargetInfo> existingChannelTarget = getExistingChannelTarget();

        final String targetId = existingChannelTarget.map(GetTargetsResponse.TargetInfo::getTargetId).orElseGet(() -> {
            CreateTargetResponse createTargetResponse = (CreateTargetResponse) chromeWSClient.executeCommand(new CreateTargetRequest(url), CreateTargetResponse.class);
            return createTargetResponse.getResult().getTargetId();
        });

        final String sessionId = attachToAndActivateTarget(targetId);
        final EnableResponse enableResponse = (EnableResponse) chromeWSClient.executeCommand(new EnableRequest(sessionId), EnableResponse.class);

        currentSession = new ChromeSession(sessionId, existingChannelTarget.isEmpty());
        return currentSession;
    }

    private String attachToAndActivateTarget(String targetId) {
        final AttachToTargetResponse attachToTargetResponse = (AttachToTargetResponse) chromeWSClient.executeCommand(
                new AttachToTargetRequest(targetId, true), AttachToTargetResponse.class);

        // Does not activate the Chrome window, only the tab
        final ActivateTargetResponse activateTargetResponse = (ActivateTargetResponse)
                chromeWSClient.executeCommand(new ActivateTargetRequest(targetId, true), ActivateTargetResponse.class);

        return attachToTargetResponse.getResult().getSessionId();
    }

    private Optional<GetTargetsResponse.TargetInfo> getExistingChannelTarget() {
        GetTargetsResponse getTargetsCommand = (GetTargetsResponse) chromeWSClient.executeCommand(new GetTargetsRequest(), GetTargetsResponse.class);

        return getTargetsCommand.getResult().getTargetInfos().stream()
                .filter(target -> target.getType().equals(TargetType.PAGE.getType()) && target.getTitle().startsWith(mediaServerPageChannelTitle)).findFirst();
    }

    private void activateMediaServerChannelChromeWindow() {
        final List<String> chromeWindowCaptions = win32Helper.getWindowCaptionsByWindowClassName(chromeWindowClassName);
        chromeWindowCaptions.stream().filter(caption -> caption.startsWith(mediaServerPageChannelTitle)).findFirst().ifPresent(caption -> {
            long windowToActivate = win32Helper.getWindowHandleByWindowTitle(caption);
            win32Helper.setActiveWindow(windowToActivate);
        });
    }

    private ChromeConnection connectToChrome() throws IOException, InterruptedException, URISyntaxException {
        try (HttpClient httpClient = HttpClient.newHttpClient()) {
            final HttpRequest fetchBrowserTargetsRequest = HttpRequest.newBuilder().uri(new URI(chromeDevToolsJsonUrl)).build();
            final HttpResponse<String> fetchBrowserTargetsResponse = httpClient.send(fetchBrowserTargetsRequest, HttpResponse.BodyHandlers.ofString());
            final BrowserTarget[] browserTargets = objectMapper.readValue(fetchBrowserTargetsResponse.body(), BrowserTarget[].class);
            final Map<String, List<BrowserTarget>> browserTargetsByType = Arrays.stream(browserTargets).collect(Collectors.groupingBy(BrowserTarget::getType));

            BrowserTarget browserTarget = Arrays.stream(browserTargets).filter(bt ->
                    bt.getTitle().startsWith(mediaServerPageChannelTitle)).findFirst().orElseGet(() -> browserTargets[0]);

            final MediaServerWebSocketClient webSocketClient = new MediaServerWebSocketClient(browserTarget.getWebSocketDebuggerUrl());
            webSocketClient.connectBlocking();

            return new ChromeConnection(webSocketClient, browserTarget);
        }
    }
}
