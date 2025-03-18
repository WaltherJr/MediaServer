package org.eriksandsten.controller;

import org.eriksandsten.ActiveTVChannelInfoResponse;
import org.eriksandsten.MediaService;
import org.eriksandsten.SetActiveChannelPlayStateRequestBody;
import org.eriksandsten.philips.*;
import org.eriksandsten.philips.response.PowerStateResponse;
import org.eriksandsten.philips.response.VolumeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TVController {
    private final MediaService mediaService;
    private final PhilipsTVService philipsTVService;

    @Autowired
    public TVController(MediaService mediaService, PhilipsTVService philipsTVService) {
        this.mediaService = mediaService;
        this.philipsTVService = philipsTVService;
    }

    @PutMapping("/tv/mutestate")
    public String setTVMuteState(TVMuteStateRequestBody muteStateRequest) {
        String s = philipsTVService.setTVMuteState(muteStateRequest.muted());
        return s;
    }

    @GetMapping("/tv/current-channel")
    public String getTVCurrentChannel() {
        return philipsTVService.getTVCurrentChannel();
    }

    @GetMapping("/tv/volume")
    public VolumeResponse getTVVolume() {
        return philipsTVService.getTVVolume();
    }

    @GetMapping("/tv/standbystate")
    public PowerStateResponse setTVStandbyState() {
        return philipsTVService.getTVPowerState();
    }

    @PutMapping("/tv/standbystate")
    public String setTVStandbyState(TVStandbyStateRequestBody standbyStateRequest) {
        String s = philipsTVService.setTVStandbyState(standbyStateRequest.standby());
        return s;
    }

    @GetMapping("/tv/channel/epg")
    public void viewAllenteTVChannelEPG() {
        mediaService.viewAllenteTVChannelEPG();
    }

    @PutMapping("/tv/channel/active")
    public void turnOnAllenteTVChannel(TurnOnTVChannelBody turnOnTVChannelRequest) {
        // mediaService.turnOnTVChannel(turnOnTVChannelRequest.channelId());
    }

    @GetMapping("/tv/channel/active")
    public ActiveTVChannelInfoResponse getActiveTVChannelInfo() {
        return null;
        // return mediaService.getActiveTVChannelInfo();
    }

    @PutMapping("/tv/channel/active/playstate")
    public void setActiveChannelPlayState(SetActiveChannelPlayStateRequestBody setActiveChannelPlayStateRequestBody) {
        // mediaService.setActiveChannelPlayState(setActiveChannelPlayStateRequestBody);
    }
}
