package com.ankitsingh.bestcard.model.interval;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonDeserialize(builder = DefaultInterval.DefaultIntervalBuilder.class)
@Builder
public class DefaultInterval extends Interval {

    @JsonPOJOBuilder(withPrefix ="")
    public static class DefaultIntervalBuilder {}

    public DefaultInterval clone() {
        return DefaultInterval.builder().build();
    }
}
