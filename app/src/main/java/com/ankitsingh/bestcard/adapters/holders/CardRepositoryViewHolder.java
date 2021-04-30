package com.ankitsingh.bestcard.adapters.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.R;

import lombok.Getter;

@Getter
public class CardRepositoryViewHolder extends RecyclerView.ViewHolder {

    private RelativeLayout cardRow;

    private ImageView cardIcon;
    private TextView cardName;
    private TextView cardType;
    private TextView cardAnnualFees;

    public CardRepositoryViewHolder(final View cardView) {
        super(cardView);

        this.cardRow = (RelativeLayout) cardView;

        this.cardIcon = cardView.findViewById(R.id.card_icon);
        this.cardName = cardView.findViewById(R.id.card_name);
        this.cardType = cardView.findViewById(R.id.card_type);
        this.cardAnnualFees = cardView.findViewById(R.id.card_annual_fees);
    }
}
