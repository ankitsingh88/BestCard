package com.ankitsingh.bestcard.model.interval;

import java.time.LocalDate;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = false)
@ToString
@Builder
public class TimeInterval extends Interval {

    private final LocalDate startDate;
    private final LocalDate endDate;
}
