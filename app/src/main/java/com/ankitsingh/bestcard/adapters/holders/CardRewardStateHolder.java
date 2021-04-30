package com.ankitsingh.bestcard.adapters.holders;

import com.ankitsingh.bestcard.model.Reward;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class CardRewardStateHolder {

    private Reward reward;
}
