package com.ankitsingh.bestcard.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = Card.CardBuilder.class)
@Builder(builderClassName = "CardBuilder", toBuilder = true)
public class Card {

    private final String id;
    private String name;
    private String iconName;
    private CardType cardType;

    @Builder.Default
    private Integer annualFees = 0;

    @Builder.Default
    private Double multiplier = 1.0;

    @Builder.Default
    private Boolean disabled = false;

    @Builder.Default
    private List<Reward> rewards = new ArrayList<>();

    @JsonPOJOBuilder(withPrefix ="")
    public static class CardBuilder {}

    public Card clone() {
        return Card.builder()
                .id(this.id)
                .name(this.name)
                .iconName(this.iconName)
                .cardType(this.cardType)
                .annualFees(this.annualFees)
                .multiplier(this.multiplier)
                .disabled(this.disabled)
                .rewards(this.rewards.stream().map(Reward::clone).collect(Collectors.toList()))
                .build();
    }
}
