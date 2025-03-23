package org.eriksandsten.devtools.response.runtime;

import lombok.Data;

@Data
public class DeepSerializedValue {
    public String type;
    public Object value;
    public String objectId;
    public Integer weakLocalObjectReference;
}
