package org.eriksandsten.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.eriksandsten.chromedpt.ChromeClient;
import com.eriksandsten.chromedpt.response.domain.runtime.EvaluateResponse;
import org.eriksandsten.domain.Volume;
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        import org.eriksandsten.domain.allente.channel.EPGChannel;
import org.eriksandsten.domain.allente.channel.TVChannel;
import org.eriksandsten.exception.Http405MethodNotAllowedRequestException;
import org.eriksandsten.domain.MuteState;
import org.eriksandsten.domain.PlayState;
import org.eriksandsten.request.SetCurrentTVChannelRequest;
import org.eriksandsten.utils.MediaServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.regex.Pattern;

@Service
public class AllenteService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private final Pattern allenteTVChannelURLRegex = Pattern.compile("https://tv\\.allente\\.se/play/live/([0-9]+)");
    private final String allenteMediaPlayerPlayButtonSelector = "button#play-pause [data-test-id='player_play_button']";
    private final String allenteMediaPlayerPauseButtonSelector = "button#play-pause [data-test-id='player_pause_button']";
    private final String allenteUserLoginButtonSelector = "button[data-test-id='guest_alert_login_button']";
    private final String allenteUsernameInputFieldSelector = "input#usernameControl";
    private final String allentePasswordInputFieldSelector = "input#passwordControl";
    private final String allenteTVChannelURL;
    private final String allenteTVChannelEpgURL;
    private final String allenteUsername;
    private final String allentePassword;
    private final MessageSource messageSource;
    private final ChromeClient chromeClient;
    private final String baseJavaScript;
    private final String getPlayStateJavaScript;
    private final String getMuteStateJavaScript;
    private final String updatePlayStateJavaScript;
    private final String updateMuteStateJavaScript;
    private final String getCurrentChannelJavaScript;
    private final String getVolumeJavaScript;
    private final String updateVolumeJavaScript;

    @Autowired
    public AllenteService(MessageSource messageSource, ChromeClient chromeClient,
                          @Value("${allente_tv_channel_url}") String allenteTVChannelURL,
                          @Value("${allente_tv_channel_epg_url}") String allenteTVChannelEpgURL) {

        this.messageSource = messageSource;
        this.chromeClient = chromeClient;
        this.allenteTVChannelURL = allenteTVChannelURL;
        this.allenteTVChannelEpgURL = allenteTVChannelEpgURL;
        this.allenteUsername = System.getenv("AllenteUsername");
        this.allentePassword = System.getenv("AllentePassword");
        this.baseJavaScript = MediaServerUtils.readResourceFile("static/js/utils.js");
        this.getPlayStateJavaScript = MediaServerUtils.readResourceFile("static/js/tv/get_channel_playstate.js");
        this.getMuteStateJavaScript = MediaServerUtils.readResourceFile("static/js/tv/get_channel_mutestate.js");
        this.updatePlayStateJavaScript = MediaServerUtils.readResourceFile("static/js/tv/update_channel_playstate.js");
        this.updateMuteStateJavaScript = MediaServerUtils.readResourceFile("static/js/tv/update_channel_mutestate.js");
        this.getCurrentChannelJavaScript = MediaServerUtils.readResourceFile("static/js/tv/get_current_channel.js");
        this.getVolumeJavaScript = MediaServerUtils.readResourceFile("static/js/tv/get_channel_volume.js");
        this.updateVolumeJavaScript = MediaServerUtils.readResourceFile("static/js/tv/update_channel_volume.js");
    }

    public List<TVChannel> getAllTVChannels() {
        return TVChannel.subscribedTVChannels;
    }

    public Volume getTVChannelVolume() {
        try {
            var baseResult = chromeClient.evaluateJavascript(baseJavaScript);
            final Optional<EvaluateResponse> result = chromeClient.evaluateJavascript(getVolumeJavaScript);

            if (result.isPresent()) {
                final String scriptReturnValue = (String) result.get().getResult().getResult().getValue();
                final EvaluateJSReturnValue returnValue = objectMapper.readValue(scriptReturnValue, EvaluateJSReturnValue.class);
                return returnValue.responseText == null ? null : new Volume(Short.valueOf(returnValue.responseText));
            } else {
                return null;
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void setTVChannelVolume(Short volume) {
        try {
            var baseResult = chromeClient.evaluateJavascript(updateVolumeJavaScript.formatted(volume));
            final Optional<EvaluateResponse> result = chromeClient.evaluateJavascript(updateVolumeJavaScript);

            if (result.isPresent()) {
                final String scriptReturnValue = (String) result.get().getResult().getResult().getValue();
                final EvaluateJSReturnValue returnValue = objectMapper.readValue(scriptReturnValue, EvaluateJSReturnValue.class);
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public PlayState getTVChannelPlayState() {
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

    public MuteState getTVChannelMuteState() {
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

    public void setTVChannelPlayState(PlayState playState) {
        try {
            final String formattedJS = updatePlayStateJavaScript.formatted(playState);
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

    public void setTVChannelMuteState(MuteState muteState) {
        try {
            final String formattedJS = updateMuteStateJavaScript.formatted(muteState);
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

    public TVChannel getCurrentTVChannel() {
        try {
            var baseResult = chromeClient.evaluateJavascript(baseJavaScript);
            final Optional<EvaluateResponse> result = chromeClient.evaluateJavascript(getCurrentChannelJavaScript);

            if (result.isPresent()) {
                final String scriptReturnValue = (String) result.get().getResult().getResult().getValue();
                final EvaluateJSReturnValue returnValue = objectMapper.readValue(scriptReturnValue, EvaluateJSReturnValue.class);
                return TVChannel.getMappedChannelById(returnValue.responseText);

            } else {
                return null;
            }
        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void setCurrentTVChannel(SetCurrentTVChannelRequest request) {
        try {
            navigateToChannel("EPG".equals(request.channelId()) ? new EPGChannel() : new TVChannel(request.channelId()));
        } catch (Exception e) {
            throw new RuntimeException(e.getCause());
        }
    }

    public void navigateToChannel(TVChannel channel) throws IOException, URISyntaxException, InterruptedException {
        if (channel instanceof EPGChannel) {
            chromeClient.loadPage(allenteTVChannelEpgURL);

        } else if (channel instanceof TVChannel tvChannel) {
            TVChannel mappedChannelId = Optional.ofNullable(TVChannel.subscribedTVChannels.get(Integer.valueOf(tvChannel.getChannelId()))).orElseThrow(() ->
                    new IllegalArgumentException(messageSource.getMessage("no_such_tv_channel", null, Locale.getDefault())));

            chromeClient.loadPage(allenteTVChannelURL.formatted(mappedChannelId));
            injectBaseJavascriptAndMessageStrings();
        }
    }

    private void injectBaseJavascriptAndMessageStrings() throws IOException, URISyntaxException, InterruptedException {

        final Optional<EvaluateResponse> injectBaseJS = chromeClient.evaluateJavascript(baseJavaScript);

        final String messageStringsJSON = objectMapper.writeValueAsString(MediaServerUtils.createMessageStrings(messageSource,
                "trying_to_play_already_playing_video", "trying_to_pause_already_paused_video",
                "trying_to_mute_already_muted_video", "trying_to_unmute_already_unmuted_video"));

        final String injection1 = "window.mediaServerUtils.setMessageStrings(%s)".formatted(messageStringsJSON);

        final Optional<EvaluateResponse> injectMessageStrings = chromeClient.evaluateJavascript(injection1);
    }
}
