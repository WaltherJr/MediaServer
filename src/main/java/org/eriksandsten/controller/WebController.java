package org.eriksandsten.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.eriksandsten.utils.MediaServerUtils;
import org.eriksandsten.utils.YouTubeHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletResponse;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class WebController {
    private final MessageSource messageSource;
    @Value("${media_server_page_channel_title}")
    String mediaServerPageChannelTitle;

    @Autowired
    public WebController(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Operation(summary = "Ping the application")
    @ResponseBody
    @GetMapping(value = "/")
    public String hello() {
        return "Hello from ASUS media server!";
    }
/*
    @Operation(summary = "View a YouTube video locally")
    @GetMapping("/youtube/{videoId}")
    public ModelAndView viewYouTubeEmbedPlayer(@PathVariable(name = "videoId") String videoId) {
        return new ModelAndView("youtube_player", Map.of(
                "messageStrings", createMessageStrings(
            "trying_to_play_already_playing_video", "trying_to_pause_already_paused_video",
                        "trying_to_mute_already_muted_video", "trying_to_unmute_already_unmuted_video"
                ),
                "videoId", videoId,
                "videoUrl", YouTubeHelper.getMusicVideoByVideoId(videoId).url(),
                "documentTitle", "%s [%s]".formatted(mediaServerPageChannelTitle, videoId))
        );
    }
*/
    @Operation(summary = "View a YouTube video locally")
    @GetMapping("/youtube/{videoId}")
    public ModelAndView viewYouTubeEmbedPlayer(@PathVariable(name = "videoId") String videoId, HttpServletResponse httpResponse) {
        return new ModelAndView("youtube_player", Map.of(
                "messageStrings", MediaServerUtils.createMessageStrings(messageSource,
                        "trying_to_play_already_playing_video", "trying_to_pause_already_paused_video",
                        "trying_to_mute_already_muted_video", "trying_to_unmute_already_unmuted_video"
                ),
                "videoId", videoId,
                "videoUrl", YouTubeHelper.getMusicVideoByVideoId(videoId).url(),
                "documentTitle", "%s [%s]".formatted(mediaServerPageChannelTitle, videoId))
        );
    }
}
