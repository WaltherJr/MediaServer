package org.eriksandsten.devtools.response.runtime;

import lombok.Data;

import java.util.List;

@Data
public class ObjectPreview {
    public String type;
    public String subtype;
    public String description;
    public Boolean overflow;
    public List<PropertyPreview> properties;
    public List<EntryPreview> entries;
}
