function getMusicVideoPlayState() {
    var state = window.videoPlayer.getPlayerState();

    if (state === YT.PlayerState.PLAYING) {
        return window.mediaServerUtils.createSuccessResponse('PLAYING');
    } else if (state === YT.PlayerState.PAUSED) {
        return window.mediaServerUtils.createSuccessResponse('PAUSED');
    } else {
        return window.mediaServerUtils.createSuccessResponse(null);
    }
}

getMusicVideoPlayState();
