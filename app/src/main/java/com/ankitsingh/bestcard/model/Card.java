package com.ankitsingh.bestcard.model;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Card {

    private final String id;
    private final String name;
    private final String iconName;
    private final CardType cardType;
    private final Double multiplier;

    private final List<Reward> rewards;
}
