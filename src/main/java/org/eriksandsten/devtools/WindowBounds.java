package org.eriksandsten.devtools;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WindowBounds {
    public Integer left;
    public Integer top;
    public Integer width;
    public Integer height;
    public String windowState;
}
