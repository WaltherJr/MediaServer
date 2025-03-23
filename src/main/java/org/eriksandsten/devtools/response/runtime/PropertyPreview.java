package org.eriksandsten.devtools.response.runtime;

import lombok.Data;

@Data
public class PropertyPreview {
    public String name;
    public String type;
    public String value;
    public ObjectPreview valuePreview;
    public String subtype;
}
