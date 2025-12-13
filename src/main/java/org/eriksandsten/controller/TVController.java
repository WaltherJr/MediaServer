package org.eriksandsten.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.eriksandsten.domain.MuteState;
import org.eriksandsten.domain.Volume;
import org.eriksandsten.philips.request.SetTVMuteStateRequest;
import org.eriksandsten.request.*;
import org.eriksandsten.response.*;
import org.eriksandsten.service.AllenteService;
import org.eriksandsten.service.MediaService;
import org.eriksandsten.philips.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TVController {
    private final MediaService mediaService;
    private final PhilipsTVService philipsTVService;
    private final AllenteService allenteService;

    @Autowired
    public TVController(MediaService mediaService, PhilipsTVService philipsTVService, AllenteService allenteService) {
        this.mediaService = mediaService;
        this.philipsTVService = philipsTVService;
        this.allenteService = allenteService;
    }

    @Operation(summary = "Get the TV's volume")
    @GetMapping("/tv/volume")
    public GetVolumeResponse getPhilipsTVVolume() {
        return new GetVolumeResponse(new Volume(philipsTVService.getTVVolume().getCurrent()));
    }

    @Operation(summary = "Set the TV's volume")
    @PutMapping("/tv/volume")
    public void setPhilipsTVVolume(@Valid @RequestBody SetTVVolumeRequest setTVVolumeRequest) {
        philipsTVService.setTVVolume(setTVVolumeRequest);
    }

    @Operation(summary = "Get the TV's mute state")
    @GetMapping("/tv/mutestate")
    public GetMuteStateResponse getPhilipsTVMuteState() {
        return new GetMuteStateResponse(MuteState.fromBoolean(philipsTVService.getTVMuteState().getMuted()));
    }

    @Operation(summary = "Set the TV's mute state")
    @PutMapping("/tv/mutestate")
    public void setPhilipsTVMuteState(@Valid @RequestBody SetTVMuteStateRequest setTVMuteStateRequest) {
        philipsTVService.setTVMuteState(setTVMuteStateRequest);
    }

}
