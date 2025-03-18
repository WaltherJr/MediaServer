package org.eriksandsten;

import org.eriksandsten.devtools.ChromeDevTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import java.net.ConnectException;
import java.util.Locale;
import java.util.regex.Pattern;

@Service
public class MediaService {
    private final Logger logger = LoggerFactory.getLogger(MediaService.class);
    private final Pattern allenteTVChannelURLRegex = Pattern.compile("https://tv\\.allente\\.se/play/live/([0-9]+)");
    private final Integer thisServiceHDMIChannel;
    private final String allenteTVChannelURL;
    private final String allenteTVChannelEpgURL;
    private final String allenteUsername;
    private final String allentePassword;
    private final String allenteMediaPlayerPlayButtonSelector = "button#play-pause [data-test-id='player_play_button']";
    private final String allenteMediaPlayerPauseButtonSelector = "button#play-pause [data-test-id='player_pause_button']";
    private final String allenteUserLoginButtonSelector = "button[data-test-id='guest_alert_login_button']";
    private final String allenteUsernameInputFieldSelector = "input#usernameControl";
    private final String allentePasswordInputFieldSelector = "input#passwordControl";
    private final YoutubeVideoPlayer youtubeVideoPlayer;
    private final AllenteVideoPlayer allenteVideoPlayer;
    private final MessageSource messageSource;
    private final ChromeDevTools chromeDevTools;

    @Autowired
    public MediaService(@Value("${thisServiceHDMIChannel}") Integer thisServiceHDMIChannel,
                        @Value("${allente_tv_channel_url}") String allenteTVChannelURL,
                        @Value("${allente_tv_channel_epg_url}") String allenteTVChannelEpgURL,
                        MessageSource messageSource,
                        ChromeDevTools chromeDevTools) {

        this.thisServiceHDMIChannel = thisServiceHDMIChannel;
        this.allenteTVChannelURL = allenteTVChannelURL;
        this.allenteTVChannelEpgURL = allenteTVChannelEpgURL;
        this.allenteUsername = System.getenv("AllenteUsername");
        this.allentePassword = System.getenv("AllentePassword");
        this.youtubeVideoPlayer = new YoutubeVideoPlayer();
        this.allenteVideoPlayer = new AllenteVideoPlayer();
        this.messageSource = messageSource;
        this.chromeDevTools = chromeDevTools;
        // Runtime.getRuntime().addShutdownHook(new Thread(webDriver::close));
    }

    public String playMusicVideoByArtistAndSongName(String videoSearchStr) {
        youtubeVideoPlayer.navigateToVideoUrlBySearch(videoSearchStr);
        MediaHelper.switchHDMIChannel(thisServiceHDMIChannel);
        return "OK!";
    }

    public String playMusicVideoById(String videoId) {
        try {
            return "RESULT: " + chromeDevTools.loadPage("http://localhost:8000/youtube/" + videoId);
        } catch (ConnectException e) {
            throw new RuntimeException(messageSource.getMessage("cannot_connect_to_chrome", null, Locale.getDefault()));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void viewAllenteTVChannelEPG() {
        // allenteVideoPlayer.navigateToUrl(allenteTVChannelEpgURL);
        // AllenteHelper.acceptCookies(webDriver);
    }

}
