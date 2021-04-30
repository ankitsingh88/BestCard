package com.ankitsingh.bestcard.adapters.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.R;
import com.google.android.material.checkbox.MaterialCheckBox;

import lombok.Getter;

@Getter
public class CardRewardViewHolder extends RecyclerView.ViewHolder {

    private ConstraintLayout cardRewardRow;

    private MaterialCheckBox cardRewardEnabled;
    private ImageView categoryIcon;
    private TextView categoryName;
    private TextView categoryRewardValue;
    private TextView categoryInterval;
    private ImageButton categoryEdit;
    private ImageButton categoryDelete;

    public CardRewardViewHolder(final View cardRewardView) {
        super(cardRewardView);

        this.cardRewardRow = (ConstraintLayout) cardRewardView;

        this.cardRewardEnabled = cardRewardRow.findViewById(R.id.card_reward_enabled);
        this.categoryIcon = cardRewardRow.findViewById(R.id.category_icon);
        this.categoryName = cardRewardRow.findViewById(R.id.category_name);
        this.categoryRewardValue = cardRewardRow.findViewById(R.id.card_reward_value);
        this.categoryInterval = cardRewardRow.findViewById(R.id.card_reward_interval);
        this.categoryEdit = cardRewardRow.findViewById(R.id.card_reward_edit);
        this.categoryDelete = cardRewardRow.findViewById(R.id.card_reward_delete);
    }
}
