package com.ankitsingh.bestcard.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
public class Category {

    private final String name;
    private final String iconName;
}
