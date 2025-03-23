package org.eriksandsten.service;

import lombok.Data;

@Data
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
}
