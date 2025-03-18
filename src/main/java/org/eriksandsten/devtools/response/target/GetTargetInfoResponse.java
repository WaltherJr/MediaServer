package org.eriksandsten.devtools.response.target;


import lombok.Data;

@Data
public class GetTargetInfoResponse {
    private Long id;
    private Result result;

    @Data
    public static class Result {
        private TargetInfo targetInfo;
    }

    @Data
    public static class TargetInfo {
        private String targetId;
        private String type;
        private String title;
        private String url;
        private Boolean attached;
        private String openerId;
        private Boolean canAccessOpener;
        private String openerFrameId;
        private String browserContextId;
        private String subtype;
    }
}
