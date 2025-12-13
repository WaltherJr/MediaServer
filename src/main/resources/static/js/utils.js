
window.mediaServerUtils = {
    setMessageStrings: function(messageStrings) {
        localStorage.setItem('messageStrings', JSON.stringify(messageStrings));
    },
    getMessageString: function(messageKey) {
        var messageStringsJSON = localStorage.getItem('messageStrings');

        if (messageStringsJSON) {
            var messageStrings = JSON.parse(messageStringsJSON);
            return messageStrings[messageKey];
        } else {
            return null;
        }
    },
    createResponse: function(responseType, responseText) {
        return JSON.stringify({responseType: responseType, responseText: responseText});
    },
    createErrorResponse: function(responseText) {
        return window.mediaServerUtils.createResponse('ERROR', responseText);
    },
    createSuccessResponse: function(responseText) {
        return window.mediaServerUtils.createResponse('SUCCESS', responseText);
    }
}
