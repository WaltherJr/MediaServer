package org.eriksandsten.controller;

import com.sun.jna.Pointer;
import jakarta.validation.Valid;
import org.eriksandsten.*;
import org.eriksandsten.utils.Win32Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MediaController {
    private MediaService mediaService;
    private final Win32Helper win32Helper;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
        this.win32Helper = new Win32Helper();
    }

    @GetMapping("/get-process-window")
    public Long getProcessWindow(@RequestParam(name = "processName") String processName) {
        List<Long> processIds = win32Helper.getProcessIdsByName(processName);
        // var a = win32Helper.helloWorld(processIds.get(0).intValue());
        List<Long> distinctProcessIds = processIds.stream().distinct().toList();
        var windowHandlesByClassName = win32Helper.getWindowHandlesByWindowClassName(processName);
        var windowCaptionsByClassName = win32Helper.getWindowCaptionsByWindowClassName(processName);

        List<Long> windowHandles = distinctProcessIds.stream().map(processId -> {
            Long windowHandle = win32Helper.getWindowHandleByProcessId(processId.intValue());
            return windowHandle;
        }).toList();
        List<Long> distinctWindowHandles = windowHandles.stream().distinct().toList();

        // System.out.println("SETTING ACTIVE WINDOW: " + windowHandlesByClassName.get(0));

        try {
            Thread.sleep(5000);
            // win32Helper.setActiveWindow(windowHandlesByClassName.get(0));
        } catch (InterruptedException e) {

        }
        return windowHandles.getFirst();
    }

    @PutMapping("/music-video/artist-and-song")
    public String playMusicVideoByArtistAndSongName(MusicVideoByArtistAndSongNameRequestBody musicVideoRequest) {
        return mediaService.playMusicVideoByArtistAndSongName(
            musicVideoRequest.artistName() + " - " + musicVideoRequest.songName());
    }

    @PutMapping("/music-video/id")
    public String playMusicVideoByURL(@Valid MusicVideoByIdRequest musicVideoRequest) {
        return mediaService.playMusicVideoById(musicVideoRequest.videoId());
    }
}
