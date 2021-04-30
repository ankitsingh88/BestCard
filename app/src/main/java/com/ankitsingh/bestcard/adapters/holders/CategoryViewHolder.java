package com.ankitsingh.bestcard.adapters.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.R;
import com.google.android.material.card.MaterialCardView;

import lombok.Getter;

@Getter
public class CategoryViewHolder extends RecyclerView.ViewHolder {

    private MaterialCardView categoryRow;

    private MaterialCardView categoryIconContainer;
    private ImageView categoryIcon;
    private Space categoryDetailsVerticalSpace;
    private TextView categoryName;
    private LinearLayout categoryExpanded;
    private ImageButton categoryExpandCollapse;
    private LinearLayout categoryCards;
    private LinearLayout categoryTags;
    private LinearLayout categoryActionContainer;
    private LinearLayout categoryAddTag;

    public CategoryViewHolder(final View categoryView) {
        super(categoryView);

        this.categoryRow = (MaterialCardView) categoryView;

        this.categoryIconContainer = categoryView.findViewById(R.id.category_icon_container);
        this.categoryDetailsVerticalSpace = categoryView.findViewById(R.id.category_row_vertical_space);
        this.categoryIcon = categoryView.findViewById(R.id.category_icon);
        this.categoryName = categoryView.findViewById(R.id.category_name);
        this.categoryExpanded = categoryView.findViewById(R.id.category_expanded);
        this.categoryExpandCollapse = categoryView.findViewById(R.id.category_expand_collapse);
        this.categoryCards = categoryView.findViewById(R.id.category_cards_container);
        this.categoryTags = categoryView.findViewById(R.id.category_tags_container);
        this.categoryActionContainer = categoryView.findViewById(R.id.category_action_container);
        this.categoryAddTag = categoryView.findViewById(R.id.category_add_tag_container);
    }
}
