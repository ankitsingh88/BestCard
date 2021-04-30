package com.ankitsingh.bestcard.adapters.holders;

import com.ankitsingh.bestcard.model.Category;

import java.util.Map;

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
public class CategoryStateHolder {

    private final Category category;
    private final Map<String, Double> cardIdToRewardValueMap;
    private boolean isExpanded;
}
