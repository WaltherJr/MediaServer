function getMusicVideoMuteState() {
    var isMuted = window.videoPlayer.isMuted();

    if (isMuted === true) {
        return window.mediaServerUtils.createSuccessResponse('MUTED');
    } else if (isMuted === false) {
        return window.mediaServerUtils.createSuccessResponse('UNMUTED');
    } else {
        return window.mediaServerUtils.createSuccessResponse(null);
    }
}

getMusicVideoMuteState();
