package com.ankitsingh.bestcard.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@JsonDeserialize(builder = Category.CategoryBuilder.class)
@Builder(builderClassName = "CategoryBuilder", toBuilder = true)
public class Category {

    private final String id;
    private String name;
    private String iconName;

    @Builder.Default
    private Set<String> tags = new HashSet<>();

    @JsonPOJOBuilder(withPrefix ="")
    public static class CategoryBuilder {}

    @Override
    public Category clone() {
        return Category.builder()
                .id(this.id)
                .name(this.name)
                .iconName(this.iconName)
                .tags(new HashSet<>(this.tags))
                .build();
    }
}
