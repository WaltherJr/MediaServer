function getTVChannelPlayState() {
    if (document.querySelector('button#play-pause > [data-test-id="player_pause_button"]')) {
        return window.mediaServerUtils.createSuccessResponse('PLAYING');
    } else if (document.querySelector('button#play-pause > [data-test-id="player_play_button"]')) {
        return window.mediaServerUtils.createSuccessResponse('PAUSED');
    } else {
        return window.mediaServerUtils.createSuccessResponse(null);
    }
}

getTVChannelPlayState();
