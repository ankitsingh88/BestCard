package com.ankitsingh.bestcard.adapters.holders;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.R;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import lombok.Getter;

@Getter
public class CardViewHolder extends RecyclerView.ViewHolder {

    private MaterialCardView cardRow;

    private ImageView cardIcon;
    private TextView cardName;
    private TextView cardType;
    private TextView cardAnnualFees;
    private TextView cardMultiplier;
    private LinearLayout cardExpanded;
    private ImageButton cardExpandCollapse;
    private SwitchMaterial cardEnabled;
    private LinearLayout cardRewards;
    private LinearLayout cardActionContainer;
    private LinearLayout cardDelete;
    private LinearLayout cardEdit;

    public CardViewHolder(final View cardView) {
        super(cardView);

        this.cardRow = (MaterialCardView) cardView;

        this.cardIcon = cardView.findViewById(R.id.card_icon);
        this.cardName = cardView.findViewById(R.id.card_name);
        this.cardType = cardView.findViewById(R.id.card_type);
        this.cardAnnualFees = cardView.findViewById(R.id.card_annual_fees);
        this.cardMultiplier = cardView.findViewById(R.id.card_multiplier);
        this.cardExpanded = cardView.findViewById(R.id.card_expanded);
        this.cardExpandCollapse = cardView.findViewById(R.id.card_expand_collapse);
        this.cardEnabled = cardView.findViewById(R.id.card_enabled);
        this.cardRewards = cardView.findViewById(R.id.card_rewards);
        this.cardActionContainer = cardView.findViewById(R.id.card_action_container);
        this.cardDelete = cardView.findViewById(R.id.card_delete_container);
        this.cardEdit = cardView.findViewById(R.id.card_edit_container);
    }
}
