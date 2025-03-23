package org.eriksandsten.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.eriksandsten.request.SetCurrentTVChannelRequest;
import org.eriksandsten.request.SetMuteStateRequest;
import org.eriksandsten.request.SetPlayStateRequest;
import org.eriksandsten.request.SetTVVolumeRequest;
import org.eriksandsten.response.*;
import org.eriksandsten.service.AllenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TVChannelController {
    private final AllenteService allenteService;

    @Autowired
    public TVChannelController(AllenteService allenteService) {
        this.allenteService = allenteService;
    }

    @Operation(summary = "Set the current TV channel's volume")
    @GetMapping("/tv/channels/active/volume")
    public GetVolumeResponse getActiveTVChannelVolume() {
        return new GetVolumeResponse(allenteService.getTVChannelVolume());

    }

    @Operation(summary = "Get available TV channels")
    @GetMapping("/tv/channels")
    public GetAllTVChannelsResponse getAllTVChannels() {
        return new GetAllTVChannelsResponse(allenteService.getAllTVChannels());
    }

    @Operation(summary = "Get the current TV channel")
    @GetMapping("/tv/channels/active")
    public GetActiveTVChannelResponse getActiveTVChannel() {
        return new GetActiveTVChannelResponse(allenteService.getCurrentTVChannel());
    }

    @Operation(summary = "Set the current TV channel")
    @PutMapping("/tv/channels/active")
    public void setActiveTVChannel(@Valid @RequestBody SetCurrentTVChannelRequest request) {
        allenteService.setCurrentTVChannel(request);
    }

    @Operation(summary = "Set the current TV channel's volume")
    @PutMapping("/tv/channels/active/volume")
    public void setActiveTVChannelVolume(@Valid @RequestBody SetTVVolumeRequest setTVVolumeRequest) {
        allenteService.setTVChannelVolume(setTVVolumeRequest.volume());
    }

    @Operation(summary = "Get the current TV channel's play state")
    @GetMapping("/tv/channels/active/playstate")
    public GetPlayStateResponse getActiveTVChannelPlayState() {
        return new GetPlayStateResponse(allenteService.getTVChannelPlayState());
    }

    @Operation(summary = "Set the current TV channel's play state")
    @PutMapping("/tv/channels/active/playstate")
    public void setCurrentTVChannelPlayState(@Valid @RequestBody SetPlayStateRequest setPlayStateRequest) {
        allenteService.setTVChannelPlayState(setPlayStateRequest.playState());
    }

    @Operation(summary = "Get the current TV channel's mute state")
    @GetMapping("/tv/channels/active/mutestate")
    public GetMuteStateResponse getActiveTVChannelMuteState() {
        return new GetMuteStateResponse(allenteService.getTVChannelMuteState());
    }

    @Operation(summary = "Set the current TV channel's mute state")
    @PutMapping("/tv/channels/active/mutestate")
    public void setActiveTVChannelMuteState(@Valid @RequestBody SetMuteStateRequest setMuteStateRequest) {
        allenteService.setTVChannelMuteState(setMuteStateRequest.muteState());
    }
}
