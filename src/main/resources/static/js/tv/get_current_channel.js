function getCurrentChannel() {
    var urlPath = window.location.pathname.match(/^\/play\/live\/([0-9]+)$/);
    if (urlPath) {
        return window.mediaServerUtils.createSuccessResponse(urlPath[1]);
    } else if (window.location.host === 'tv.allente.se' && window.location.pathname === '/epg') {
        return window.mediaServerUtils.createSuccessResponse('EPG');
    } else {
        window.mediaServerUtils.createErrorResponse(window.mediaServerUtils.getMessageString('unknown_channel'));
    }
}

getCurrentChannel();
