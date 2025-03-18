package org.eriksandsten.devtools.response.dom;

import lombok.Data;
import java.util.List;

@Data
public class GetDocumentResponse {
    private Long id;
    private Result result;
    private String sessionId;

    @Data
    public static class Result {
        private Long nodeId;
        private Root root;
    }

    @Data
    public static class Root {
        private Long nodeId;
        private Long backendNodeId;
        private Integer nodeType;
        private String nodeName;
        private String localName;
        private String nodeValue;
        private Integer childNodeCount;
        private List<NodeChild> children;
        private String documentURL;
        private String baseURL;
        private String xmlVersion;
        private String compatibilityMode;
        private Boolean isScrollable;
    }

    @Data
    public static class NodeChild {
        private Long nodeId;
        private Long parentId;
        private Long backendNodeId;
        private Integer nodeType;
        private String nodeName;
        private String localName;
        private String nodeValue;
        private String publicId;
        private String systemId;
        private Integer childNodeCount;
        private List<NodeChild> children;
        private List<String> attributes;
        private List<ShadowRoot> shadowRoots;
        private String frameId;
        private Boolean isScrollable;
        private Boolean isSVG;
        private TemplateContent templateContent;
        private List<PseudoElement> pseudoElements;
        private ContentDocument contentDocument;
    }

    @Data
    public static class ContentDocument {
        private String documentURL;
        private String baseURL;
        private String xmlVersion;
        private String compatibilityMode;
        private Long nodeId;
        private Long parentId;
        private Long backendNodeId;
        private Integer nodeType;
        private String nodeName;
        private String localName;
        private String nodeValue;
        private String publicId;
        private String systemId;
        private Integer childNodeCount;
        private List<NodeChild> children;
    }

    @Data
    public static class TemplateContent {
        private String frameId;
        private Long nodeId;
        private Long parentId;
        private String backendNodeId;
        private String nodeType;
        private String nodeName;
        private String localName;
        private String nodeValue;
        private Integer childNodeCount;
    }

    @Data
    public static class ShadowRoot extends NodeChild {
        private String shadowRootType;
    }

    @Data
    public static class PseudoElement {
        private String selector;
        private String frameId;
        private Long nodeId;
        private Long parentId;
        private String backendNodeId;
        private String nodeType;
        private String nodeName;
        private String localName;
        private String nodeValue;
        private Integer childNodeCount;
        private List<String> attributes;
        private PseudoType pseudoType;
    }

    @Data
    public static class PseudoType {
        private String before;

        public PseudoType(String before) {
            this.before = before;
        }
    }
}
