package org.eriksandsten.service;

public class EvaluateJSReturnValue {
    public enum ResponseType { ERROR, SUCCESS };

    public ResponseType responseType;
    public String responseText;

    public EvaluateJSReturnValue() {
    }

    public EvaluateJSReturnValue(ResponseType responseType, String responseText) {
        this.responseType = responseType;
        this.responseText = responseText;
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public void setResponseType(ResponseType responseType) {
        this.responseType = responseType;
    }

    public String getResponseText() {
        return responseText;
    }

    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }
}
