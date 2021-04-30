package com.ankitsingh.bestcard.model.interval;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.time.LocalDate;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonDeserialize(builder = TimeInterval.TimeIntervalBuilder.class)
@Builder
public class TimeInterval extends Interval {

    private final LocalDate startDate;
    private final LocalDate endDate;

    @JsonPOJOBuilder(withPrefix ="")
    public static class TimeIntervalBuilder {}

    public TimeInterval clone() {
        return TimeInterval.builder()
                .startDate(LocalDate.of(this.startDate.getYear(), this.startDate.getMonth(), this.startDate.getDayOfMonth()))
                .endDate(LocalDate.of(this.endDate.getYear(), this.endDate.getMonth(), this.endDate.getDayOfMonth()))
                .build();
    }
}
