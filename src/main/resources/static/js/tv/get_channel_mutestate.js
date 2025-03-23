function getTVChannelMuteState() {
    var playerVolume = JSON.parse(localStorage.getItem('playerVolume') || '{}');

    if (playerVolume.muted === true) {
        return window.mediaServerUtils.createSuccessResponse('MUTED');
    } else if (playerVolume.muted === false) {
        return window.mediaServerUtils.createSuccessResponse('UNMUTED');
    } else {
        return window.mediaServerUtils.createSuccessResponse(null);
    }
}

getTVChannelMuteState();
