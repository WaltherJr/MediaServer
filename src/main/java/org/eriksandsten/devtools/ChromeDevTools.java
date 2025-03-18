package org.eriksandsten.devtools;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eriksandsten.MediaServerWebSocketClient;
import org.eriksandsten.devtools.request.browser.GetWindowForTargetRequest;
import org.eriksandsten.devtools.request.browser.SetWindowBoundsRequest;
import org.eriksandsten.devtools.request.page.EnableRequest;
import org.eriksandsten.devtools.request.runtime.EvaluateRequest;
import org.eriksandsten.devtools.request.target.*;
import org.eriksandsten.devtools.response.browser.GetWindowForTargetResponse;
import org.eriksandsten.devtools.response.browser.SetWindowBoundsResponse;
import org.eriksandsten.devtools.response.page.EnableResponse;
import org.eriksandsten.devtools.response.runtime.EvaluateResponse;
import org.eriksandsten.devtools.response.target.*;
import org.eriksandsten.utils.MediaServerUtils;
import org.eriksandsten.utils.Win32Helper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
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

@Component
public class ChromeDevTools {
    private final String mediaServerPageChannelTitle;
    private final String chromeDevToolsJsonUrl;
    private final String youtubeJavaScript;
    private MediaServerWebSocketClient chromeWSClient;
    private Win32Helper win32Helper = new Win32Helper();
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private record ChromeConnection(MediaServerWebSocketClient webSocketClient, BrowserTarget browserTarget) {}

    public ChromeDevTools(@Value("${media_server_page_channel_title}") String mediaServerPageChannelTitle,
                          @Value(("${chrome_devtools_json_url}")) String chromeDevToolsJsonUrl) {
        this.mediaServerPageChannelTitle = mediaServerPageChannelTitle;
        this.chromeDevToolsJsonUrl = chromeDevToolsJsonUrl;
        youtubeJavaScript = MediaServerUtils.readResourceFile("youtube.js");
    }

    private record SessionCreationResponse(String sessionId, Boolean createdBrandNewSession) {}

    public String loadPage(String url) throws IOException, URISyntaxException, InterruptedException {
        ChromeConnection chromeConnection = connectToChrome();
        chromeWSClient = chromeConnection.webSocketClient();

        GetTargetInfoResponse targetInfoResponse = (GetTargetInfoResponse) chromeWSClient.executeCommand(new GetTargetInfoRequest(chromeConnection.browserTarget.getId(), true), GetTargetInfoResponse.class);
        GetWindowForTargetResponse getWindowForTargetResponse = (GetWindowForTargetResponse) chromeWSClient.executeCommand(new GetWindowForTargetRequest(chromeConnection.browserTarget().getId(), true), GetWindowForTargetResponse.class);
        var a = win32Helper.getWindowHandleByProcessId(getWindowForTargetResponse.getResult().getWindowId());
        var b = win32Helper.getWindowHandleByProcessId_nonNative(getWindowForTargetResponse.getResult().getWindowId());
        var c = win32Helper.getWindowTitleByWindowHandle(getWindowForTargetResponse.getResult().getWindowId());
        // Long aWindowHandle = win32Helper.getWindowHandleByWindowClassName(targetInfoResponse.getResult().getTargetInfo().getTitle());

        SetWindowBoundsResponse dede = (SetWindowBoundsResponse) chromeWSClient.executeCommand(new SetWindowBoundsRequest(getWindowForTargetResponse.getResult().getWindowId(),
                WindowBounds.builder().left(0).top(0).width(800).height(600).windowState(BrowserWindowState.FULLSCREEN.getValue()).build(),
                Boolean.TRUE), SetWindowBoundsResponse.class);

        // GetBrowserContextsResponse getBrowserContextsResponse = (GetBrowserContextsResponse) chromeWSClient.executeCommand(new GetBrowserContextsRequest(), GetBrowserContextsResponse.class);

        final SessionCreationResponse session = createNewOrGetExistingSession(url);
        var apa = win32Helper.getWindowCaptionsByWindowClassName("Chrome_WidgetWin_1");
        var dejsan = apa.stream().filter(aa -> aa.startsWith(mediaServerPageChannelTitle)).findFirst();

        if (dejsan.isPresent()) {
            var windowToActivate = win32Helper.getWindowHandleByWindowTitle(dejsan.get());
            win32Helper.setActiveWindow(windowToActivate);
        }

        if (session.createdBrandNewSession) {
            chromeWSClient.addCallback("Page.loadEventFired", (params) -> {
                EvaluateResponse evaluateResponse2 = (EvaluateResponse) chromeWSClient.executeCommand(
                        new EvaluateRequest(session.sessionId, youtubeJavaScript.formatted(session.createdBrandNewSession ? "" : url, "%s [%s]".formatted(mediaServerPageChannelTitle
                                , url.substring(url.lastIndexOf("/"))))), EvaluateResponse.class);
            });
        } else {
            EvaluateResponse navigateToNewURLResponse = (EvaluateResponse) chromeWSClient.executeCommand(
                    new EvaluateRequest(session.sessionId, "window.location.href = '" + url + "';"), EvaluateResponse.class);
            EvaluateResponse setDocumentTitleResponse = (EvaluateResponse) chromeWSClient.executeCommand(
                    new EvaluateRequest(session.sessionId, "document.title = '%s [%s]';".formatted(mediaServerPageChannelTitle, url.substring(url.lastIndexOf("/")))), EvaluateResponse.class);
        }

        return "OK!";
    }

    private SessionCreationResponse createNewOrGetExistingSession(String url) {
        GetTargetsResponse getTargetsCommand = (GetTargetsResponse) chromeWSClient.executeCommand(new GetTargetsRequest(), GetTargetsResponse.class);

        final Optional<GetTargetsResponse.TargetInfo> existingChannelTab = getTargetsCommand.getResult().getTargetInfos().stream()
                .filter(target -> target.getType().equals(TargetType.PAGE.getType()) && target.getTitle().startsWith(mediaServerPageChannelTitle)).findFirst();

        final String targetId = existingChannelTab.map(targetInfo -> targetInfo.getTargetId()).orElseGet(() -> {
            CreateTargetResponse createTargetResponse = (CreateTargetResponse) chromeWSClient.executeCommand(new CreateTargetRequest(url), CreateTargetResponse.class);
            return createTargetResponse.getResult().getTargetId();
        });
        final AttachToTargetResponse attachToTargetResponse = (AttachToTargetResponse) chromeWSClient.executeCommand(new AttachToTargetRequest(targetId, true), AttachToTargetResponse.class);
        final ActivateTargetResponse activateTargetResponse = (ActivateTargetResponse)
                chromeWSClient.executeCommand(new ActivateTargetRequest(targetId, true), ActivateTargetResponse.class); // Does not activate the Chrome window, only the tab
        final String sessionId = attachToTargetResponse.getResult().getSessionId();
        final EnableResponse enableResponse = (EnableResponse) chromeWSClient.executeCommand(new EnableRequest(sessionId), EnableResponse.class);

        return new SessionCreationResponse(sessionId, existingChannelTab.isEmpty());
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
