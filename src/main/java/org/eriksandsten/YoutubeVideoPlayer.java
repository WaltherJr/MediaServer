package org.eriksandsten;

import org.eriksandsten.devtools.ChromeDevTools;
import org.eriksandsten.utils.MediaServerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Pattern;

@Service
public class YoutubeVideoPlayer {
    @Autowired
    private ChromeDevTools chromeDevTools;
    private final String youtubeJavaScript;

    private final Pattern videoIdRegex = Pattern.compile("\"videoId\":\"([A-Za-z0-9]+)\"", Pattern.MULTILINE);
    private final Map<String, Map<String, String>> i18_strings = Map.of(
            "en-US", Map.of(
                    "BUTTON_CONSENT_TEXT", "Accept the use of cookies and other data for the purposes described",
                    "NO_THANKS_BUTTON_TEXT", "No thanks"
            ),
            "sv-SE", Map.of(
                    "BUTTON_CONSENT_TEXT", "Godkänn att cookies och annan data används för de ändamål som beskrivs",
                    "NO_THANKS_BUTTON_TEXT", "Nej tack"
            )
    );

    public YoutubeVideoPlayer() {
        youtubeJavaScript = MediaServerUtils.readResourceFile("youtube.js");
    }

    public void navigateToVideoUrl(String url) {
        // chromeDevTools.
        navigateToYoutubeVideo(url);
        // executeJavascript(youtubeJavaScript);
    }

    public void navigateToVideoUrlBySearch(String videoSearchStr) {
        var encodedSearchStr = UriUtils.encodeQuery(videoSearchStr, "UTF-8");
        var url = "https://www.youtube.com/results?search_query=" + encodedSearchStr;

        try {
            Scanner sc = new Scanner(Runtime.getRuntime().exec(new String[] {"bash", "fetch_youtube_windows.sh", url})
                    .getInputStream()).useDelimiter("\\A");
            String json = sc.hasNext() ? sc.next() : "";
            var matcher = videoIdRegex.matcher(json);

            if (matcher.find()) {
                navigateToVideoUrl("https://www.youtube.com/watch?v=" + matcher.group(1));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void navigateToYoutubeVideo(String url) {

    }
}
