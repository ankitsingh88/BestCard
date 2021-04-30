package com.ankitsingh.bestcard.adapters.holders;

import com.ankitsingh.bestcard.model.Category;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Builder
public class CategoryDropdownStateHolder {

    private Category category;

    @Override
    public String toString() {
        return this.category.getName();
    }
}