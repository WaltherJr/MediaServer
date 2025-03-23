function setVideoMuteState(newMuteState) {
    if (newMuteState === 'MUTED') {
        if (window.videoPlayer.isMuted()) {
            return window.mediaServerUtils.createErrorResponse(window.mediaServerUtils.getMessageString('trying_to_mute_already_muted_video'));
        } else {
            window.videoPlayer.mute();
            return window.mediaServerUtils.createSuccessResponse();
        }
    } else if (newMuteState === 'UNMUTED') {
        if (!window.videoPlayer.isMuted()) {
            return window.mediaServerUtils.createErrorResponse(window.mediaServerUtils.getMessageString('trying_to_unmute_already_unmuted_video'));
        } else {
            window.videoPlayer.unMute();
            return window.mediaServerUtils.createSuccessResponse();
        }
    }
}

setVideoMuteState('%s');
