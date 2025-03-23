package org.eriksandsten.utils;

import org.eriksandsten.domain.youtube.MusicVideo;
import org.springframework.web.util.UriUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Pattern;

public class YouTubeHelper {
    private static final Pattern videoIdRegex = Pattern.compile("\"videoId\":\"([A-Za-z0-9]+)\"", Pattern.MULTILINE);

    public static void setMusicVideoVolume(Integer volume) {

    }

    public static MusicVideo getMusicVideoByVideoId(String videoId) {
        return new MusicVideo(videoId, "https://www.youtube.com/embed/%s?autoplay=1&fullscreen=1".formatted(videoId));
    }

    public static MusicVideo getMusicVideoByArtistAndSongName(String artistName, String songName) {
        final String encodedSearchStr = UriUtils.encodeQuery("%s - %s".formatted(artistName, songName), StandardCharsets.UTF_8.name());
        final String videoSearchResultsUrl = "https://www.youtube.com/results?search_query=%s".formatted(encodedSearchStr);

        try {
            final Scanner sc = new Scanner(Runtime.getRuntime().exec(new String[] {"bash", "fetch_youtube_windows.sh", videoSearchResultsUrl}).getInputStream()).useDelimiter("\\A");
            final String result = sc.hasNext() ? sc.next() : "";
            var matcher = videoIdRegex.matcher(result);

            if (matcher.find()) {
                return new MusicVideo(matcher.group(1), "https://www.youtube.com/watch?v=%s".formatted(matcher.group(1)));
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getCause());
        }

        return null;
    }
}
