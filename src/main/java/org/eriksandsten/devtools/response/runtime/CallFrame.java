package org.eriksandsten.devtools.response.runtime;

import lombok.Data;

@Data
public class CallFrame {
    public String functionName;
    public String scriptId;
    public String url;
    public Integer lineNumber;
    public Integer columnNumber;
}
