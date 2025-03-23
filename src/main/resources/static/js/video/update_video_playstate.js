function setVideoPlayState(newPlayState) {
    var currentPlayerState = window.videoPlayer.getPlayerState();

    if (newPlayState === 'PLAYING') {
        if (currentPlayerState === YT.PlayerState.PLAYING) {
            return window.mediaServerUtils.createErrorResponse(window.mediaServerUtils.getMessageString('trying_to_play_already_playing_video'));
        } else {
            window.videoPlayer.playVideo();
            return window.mediaServerUtils.createSuccessResponse();
        }
    } else if (newPlayState === 'PAUSED') {
        if (currentPlayerState === YT.PlayerState.PAUSED) {
            return window.mediaServerUtils.createErrorResponse(window.mediaServerUtils.getMessageString('trying_to_pause_already_paused_video'));
        } else {
            window.videoPlayer.pauseVideo();
            return window.mediaServerUtils.createSuccessResponse();
        }
    }
}

setVideoPlayState('%s');
