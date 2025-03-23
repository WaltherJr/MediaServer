package org.eriksandsten.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.eriksandsten.request.SetCurrentMusicVideoRequest;
import org.eriksandsten.request.SetMuteStateRequest;
import org.eriksandsten.request.SetPlayStateRequest;
import org.eriksandsten.request.SetTVVolumeRequest;
import org.eriksandsten.response.GetMuteStateResponse;
import org.eriksandsten.response.GetPlayStateResponse;
import org.eriksandsten.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MusicController {
    private MediaService mediaService;

    @Autowired
    public MusicController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @Operation(summary = "Play a music video, retrieving it by ID or by artist and song name")
    @PutMapping("/music/music-video/volume")
    public void playMusicVideo(@Valid @RequestBody SetTVVolumeRequest setTVVolumeRequest) {
        mediaService.setMusicVideoVolume(setTVVolumeRequest.volume());
    }

    @Operation(summary = "Play a music video, retrieving it by ID or by artist and song name")
    @PutMapping("/music/music-video")
    public void playMusicVideo(@Valid @RequestBody SetCurrentMusicVideoRequest request) {
        mediaService.playMusicVideo(request);
    }

    @Operation(summary = "Get the current music video's play state")
    @GetMapping("/music/music-video/playstate")
    public GetPlayStateResponse getCurrentMusicVideoPlayState() {
        return new GetPlayStateResponse(mediaService.getMusicVideoPlayState());
    }

    @Operation(summary = "Set the current music video's play state")
    @PutMapping("/music/music-video/playstate")
    public void setCurrentMusicVideoPlayState(@Valid @RequestBody SetPlayStateRequest request) {
        mediaService.setMusicVideoPlayState(request.playState());
    }

    @Operation(summary = "Get the current music video's mute state")
    @GetMapping("/music/music-video/mutestate")
    public GetMuteStateResponse getCurrentMusicVideoMuteState() {
        return new GetMuteStateResponse(mediaService.getMusicVideoMuteState());
    }

    @Operation(summary = "Set the current music video's mute state")
    @PutMapping("/music/music-video/mutestate")
    public void setCurrentMusicVideoMuteState(@Valid @RequestBody SetMuteStateRequest request) {
        mediaService.setMusicVideoMuteState(request.muteState());
    }
}
