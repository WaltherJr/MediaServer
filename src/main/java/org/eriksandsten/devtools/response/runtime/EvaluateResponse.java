package org.eriksandsten.devtools.response.runtime;

import lombok.Data;

@Data
public class EvaluateResponse {
    private Long id;
    private Result result;
    private String sessionId;

    @Data
    public static class Result {
        private String type;
        private String className;
        private EvalResult result;
        private ExceptionDetails exceptionDetails;
    }

    @Data
    public static class EvalResult {
        public String type;
        public String subtype;
        public String className;
        public Object value;
        public String unserializableValue;
        public String description;
        public DeepSerializedValue deepSerializedValue;
        public String objectId;
        public ObjectPreview preview;
        public CustomPreview customPreview;
    }

    @Data
    public static class ExceptionDetails {
        public Integer exceptionId;
        public String text;
        public Integer lineNumber;
        public Integer columnNumber;
        public String scriptId;
        public String url;
        public StackTrace stackTrace;
        public EvalResult exception;
        public Integer executionContextId;
        public Object exceptionMetaData;
    }
}
