package com.ankitsingh.bestcard.model;

import com.ankitsingh.bestcard.model.interval.Interval;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Reward {

    private final Interval interval;
    private final Category category;
    private final Double value;
}
