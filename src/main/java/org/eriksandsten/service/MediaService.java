package org.eriksandsten.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.eriksandsten.chromedpt.ChromeClient;
import com.eriksandsten.chromedpt.response.domain.runtime.EvaluateResponse;
import org.eriksandsten.domain.MuteState;
import org.eriksandsten.domain.PlayState;
import org.eriksandsten.domain.youtube.MusicVideo;
import org.eriksandsten.exception.Http405MethodNotAllowedRequestException;
import org.eriksandsten.request.*;
import org.eriksandsten.utils.MediaServerUtils;
import com.eriksandsten.chromedpt.utils.WHelper;
import org.eriksandsten.utils.YouTubeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class MediaService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final WHelper wHelper = new WHelper();
    private final MessageSource messageSource;
    private final ChromeClient chromeClient;
    private final AllenteService allenteService;
    private final YouTubeService youtubeService;
    private final String baseJavaScript;
    private final String getPlayStateJavaScript;
    private final String setPlayStateJavaScript;
    private final String getMuteStateJavaScript;
    private final String setMuteStateJavaScript;
    private final String setVolumeJavaScript;

    @Autowired
    public MediaService(MessageSource messageSource, ChromeClient chromeClient, YouTubeService youtubeService, AllenteService allenteService) {
        this.messageSource = messageSource;
        this.chromeClient = chromeClient;
        this.youtubeService = youtubeService;
        this.allenteService = allenteService;
        this.baseJavaScript = MediaServerUtils.readResourceFile("static/js/utils.js");
        this.getPlayStateJavaScript = MediaServerUtils.readResourceFile("static/js/video/get_video_playstate.js");
        this.getMuteStateJavaScript = MediaServerUtils.readResourceFile("static/js/video/get_video_mutestate.js");
        this.setPlayStateJavaScript = MediaServerUtils.readResourceFile("static/js/video/update_video_playstate.js");
        this.setMuteStateJavaScript = MediaServerUtils.readResourceFile("static/js/video/update_video_mutestate.js");
        this.setVolumeJavaScript = MediaServerUtils.readResourceFile("static/js/video/update_video_volume.js");
        // Runtime.getRuntime().addShutdownHook(new Thread(webDriver::close));
    }

    public void setMusicVideoVolume(Short volume) {
        try {
            var baseResult = chromeClient.evaluateJavascript(baseJavaScript);
            final Optional<EvaluateResponse> result = chromeClient.evaluateJavascript(setVolumeJavaScript.formatted(volume));

            if (result.isPresent()) {
                final String scriptReturnValue = (String) result.get().getResult().getResult().getValue();
                final EvaluateJSReturnValue returnValue = objectMapper.readValue(scriptReturnValue, EvaluateJSReturnValue.class);
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void playMusicVideo(SetCurrentMusicVideoRequest request) {
        MusicVideo musicVideo = null;

        if (request instanceof ByVideoIdRequest byVideoIdRequest) {
            musicVideo = YouTubeHelper.getMusicVideoByVideoId(byVideoIdRequest.getVideoId());

        } else if (request instanceof ByArtistAndSongNameRequest byArtistAndSongNameRequest) {
            musicVideo = YouTubeHelper.getMusicVideoByArtistAndSongName(byArtistAndSongNameRequest.getArtistName(), byArtistAndSongNameRequest.getSongName());
        }

        try {
            chromeClient.loadPage("http://localhost:8000/youtube/%s".formatted(musicVideo.videoId()));

        } catch (ConnectException e) {
            throw new RuntimeException(messageSource.getMessage("cannot_connect_to_chrome", null, Locale.getDefault()));

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public PlayState getMusicVideoPlayState() {
        try {
            var baseResult = chromeClient.evaluateJavascript(baseJavaScript);
            final Optional<EvaluateResponse> result = chromeClient.evaluateJavascript(getPlayStateJavaScript);

            if (result.isPresent()) {
                final String scriptReturnValue = (String) result.get().getResult().getResult().getValue();
                final EvaluateJSReturnValue returnValue = objectMapper.readValue(scriptReturnValue, EvaluateJSReturnValue.class);
                return returnValue.responseText == null ? null : PlayState.valueOf(returnValue.responseText);
            } else {
                return null;
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void setMusicVideoPlayState(PlayState playState) {
        try {
            final String formattedJS = setPlayStateJavaScript.formatted(playState);
            final Optional<EvaluateResponse> result = chromeClient.evaluateJavascript(formattedJS);

            if (result.isPresent()) {
                final String scriptReturnValue = (String) result.get().getResult().getResult().getValue();
                final EvaluateJSReturnValue returnValue = objectMapper.readValue(scriptReturnValue, EvaluateJSReturnValue.class);

                if (returnValue.responseType == EvaluateJSReturnValue.ResponseType.ERROR) {
                    throw new Http405MethodNotAllowedRequestException(returnValue.responseText);
                }
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public MuteState getMusicVideoMuteState() {
        try {
            var baseResult = chromeClient.evaluateJavascript(baseJavaScript);
            final Optional<EvaluateResponse> result = chromeClient.evaluateJavascript(getMuteStateJavaScript);

            if (result.isPresent()) {
                final String scriptReturnValue = (String) result.get().getResult().getResult().getValue();
                final EvaluateJSReturnValue returnValue = objectMapper.readValue(scriptReturnValue, EvaluateJSReturnValue.class);
                return returnValue.responseText == null ? null : MuteState.valueOf(returnValue.responseText);
            } else {
                return null;
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void setMusicVideoMuteState(MuteState muteState) {
        try {
            final Optional<EvaluateResponse> result = chromeClient.evaluateJavascript(setMuteStateJavaScript.formatted(muteState));

            if (result.isPresent()) {
                final String scriptReturnValue = (String) result.get().getResult().getResult().getValue();
                final EvaluateJSReturnValue returnValue = objectMapper.readValue(scriptReturnValue, EvaluateJSReturnValue.class);

                if (returnValue.responseType == EvaluateJSReturnValue.ResponseType.ERROR) {
                    throw new Http405MethodNotAllowedRequestException(returnValue.responseText);
                }
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    /*
    private void testWHelper(String className) {
        List<Long> processIds = wHelper.getProcessIdsByName(className);
        // var a = win32Helper.helloWorld(processIds.get(0).intValue());
        List<Long> distinctProcessIds = processIds.stream().distinct().toList();
        var windowHandlesByClassName = wHelper.getWindowHandlesByWindowClassName(className);
        var windowCaptionsByClassName = wHelper.getWindowCaptionsByWindowClassName(className);

        List<Long> windowHandles = distinctProcessIds.stream().map(processId -> {
            Long windowHandle = wHelper.getWindowHandleByProcessId(processId.intValue());
            return windowHandle;
        }).toList();
        List<Long> distinctWindowHandles = windowHandles.stream().distinct().toList();

        // System.out.println("SETTING ACTIVE WINDOW: " + windowHandlesByClassName.get(0));

        try {
            Thread.sleep(5000);
            // win32Helper.setActiveWindow(windowHandlesByClassName.get(0));
        } catch (InterruptedException e) {

        }
    }
     */
}
