package com.ankitsingh.bestcard.model;

import com.ankitsingh.bestcard.model.interval.Interval;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = Reward.RewardBuilder.class)
@Builder
public class Reward {

    private final Interval interval;
    private final String categoryId;
    private final Double value;

    @Builder.Default
    private Boolean disabled = false;

    @JsonPOJOBuilder(withPrefix ="")
    public static class RewardBuilder {}

    @Override
    public Reward clone() {
        return Reward.builder()
                .interval(this.interval.clone())
                .categoryId(this.categoryId)
                .value(this.value)
                .disabled(this.disabled)
                .build();
    }
}
