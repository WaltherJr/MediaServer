function setTVChannelPlayState(newPlayState) {
    debugger;

document.querySelector('[data-test-id="player_title"]').innerHTML = 'hejsan';

    let currentPlayerState;
    var playPauseButton = document.querySelector('button#play-pause');

    if (playPauseButton.querySelector('[data-test-id="player_pause_button"]')) {
        currentPlayerState = 'PLAYING';
    } else if (playPauseButton.querySelector('[data-test-id="player_play_button"]')) {
        currentPlayerState = 'PAUSED';
    }

    if (newPlayState === 'PLAYING') {
        if (currentPlayerState === 'PLAYING') {
            return window.mediaServerUtils.createErrorResponse(window.mediaServerUtils.getMessageString('trying_to_play_already_playing_video'));
        } else {
            playPauseButton.click();
            return window.mediaServerUtils.createSuccessResponse();
        }
    } else if (newPlayState === 'PAUSED') {
        if (currentPlayerState === 'PAUSED') {
            return window.mediaServerUtils.createErrorResponse(window.mediaServerUtils.getMessageString('trying_to_pause_already_paused_video'));
        } else {
            playPauseButton.click();
            return window.mediaServerUtils.createSuccessResponse();
        }
    }
}

setTVChannelPlayState('%s');
