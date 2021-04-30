package com.ankitsingh.bestcard.adapters.holders;

import android.widget.TextView;

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
public class CategoryDropdownViewHolder {

    private TextView categoryName;
    private TextView categoryId;
}