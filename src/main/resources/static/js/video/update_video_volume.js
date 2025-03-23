function setVideoVolume(volume) {
    window.videoPlayer.setVolume(volume);
    return window.mediaServerUtils.createSuccessResponse();
}

debugger;

setVideoVolume(%s);
