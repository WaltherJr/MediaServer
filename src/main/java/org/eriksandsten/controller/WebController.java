package org.eriksandsten.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import java.util.Map;

@Controller
public class WebController {
    @ResponseBody
    @GetMapping(value = "/")
    public String hello() {
        return "Hello from ASUS media server!";
    }

    @GetMapping("/youtube/{videoId}")
    public ModelAndView viewYouTubeEmbedPlayer(@PathVariable(name = "videoId") String videoId) {
        return new ModelAndView("youtube_player", Map.of("videoId", videoId));
    }
}
