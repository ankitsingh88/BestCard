package com.ankitsingh.bestcard.model.interval;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        {
            @JsonSubTypes.Type(value = DefaultInterval.class),
            @JsonSubTypes.Type(value = TimeInterval.class)
        }
)
public abstract class Interval {

    public abstract Interval clone();
}
