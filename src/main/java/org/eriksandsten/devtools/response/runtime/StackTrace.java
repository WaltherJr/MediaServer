package org.eriksandsten.devtools.response.runtime;

import lombok.Data;

import java.util.List;

@Data
public class StackTrace {
    public String description;
    public List<CallFrame> callFrames;
    public StackTrace parent;
    public StackTraceId parentId;
}
