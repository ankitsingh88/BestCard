package com.ankitsingh.bestcard.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.fragments.CardsFragment;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.util.CardUtil;
import com.ankitsingh.bestcard.util.CardViewUtil;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class CardAdapter extends ListAdapter<CardAdapter.CardStateHolder, CardAdapter.CardViewHolder> {

    @Getter
    @Setter
    @EqualsAndHashCode
    @ToString
    @Builder
    public static class CardStateHolder {
        private final Card card;
        private boolean isExpanded;
        private Double maxReward;
        private String query;
        private int maximumHeight;
    }

    private final View.OnClickListener onCardRowClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = recyclerView.getChildAdapterPosition(v);
                    final CardStateHolder currentCardStateHolder = getItem(position);
                    final boolean isExpanded = currentCardStateHolder.isExpanded();

                    if (isExpanded) {
                        currentCardStateHolder.setExpanded(false);

                        change(position);
                    } else {
                        for (int i = 0; i < getCurrentList().size(); ++i) {
                            final CardStateHolder cardStateHolder = getCurrentList().get(i);

                            if (position != i) {

                                if (cardStateHolder.isExpanded() && StringUtils.isBlank(cardStateHolder.getQuery())) {
                                    cardStateHolder.setExpanded(false);

                                    change(i);
                                }
                            } else {
                                cardStateHolder.setExpanded(true);

                                change(position);
                            }
                        }
                    }
                }
            };

    private final View.OnClickListener onExpandCollapseClickListener =
            v -> ((View) v.getParent()).callOnClick();

    private final View.OnClickListener onDeleteClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = recyclerView.getChildAdapterPosition((View) v.getParent().getParent().getParent());

                    CardViewUtil
                            .createDeleteDialog(bestCardActivity, CardAdapter.this, position)
                            .show();
                }
            };

    private final CardsFragment cardsFragment;
    private final RecyclerView recyclerView;
    private final BestCardActivity bestCardActivity;

    private final Map<String, CardStateHolder> masterCardStateHolderMap = new HashMap<>();
    private final List<CardStateHolder> currentCardStateHolders = new ArrayList<>();

    public static final DiffUtil.ItemCallback<CardStateHolder> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CardStateHolder>() {
                @Override
                public boolean areItemsTheSame(
                        final CardStateHolder oldItem,
                        final CardStateHolder newItem) {
                    return oldItem.getCard().getId().equals(newItem.getCard().getId());
                }
                @Override
                public boolean areContentsTheSame(
                        final CardStateHolder oldItem,
                        final CardStateHolder newItem) {
                    return oldItem.getCard().getId().equals(newItem.getCard().getId());
                }
            };

    public CardAdapter(
            final CardsFragment cardsFragment,
            final RecyclerView recyclerView,
            final Context context,
            final List<CardStateHolder> cardStateHolders) {
        super(DIFF_CALLBACK);

        this.cardsFragment = cardsFragment;
        this.recyclerView = recyclerView;
        this.bestCardActivity = (BestCardActivity) context;

        cardStateHolders.forEach(cardStateHolder -> {
            this.masterCardStateHolderMap.put(cardStateHolder.getCard().getId(), cardStateHolder);
        });

        changeDataSet(null, cardStateHolders);
    }

    public void filter(
            final String originalQuery) {
        final String query = originalQuery == null ? null : originalQuery.toLowerCase(Locale.getDefault());

        changeDataSet(query, this.masterCardStateHolderMap.values());
    }

    private void changeDataSet(
            final String query,
            final Collection<CardStateHolder> masterCardStateHolders) {
        final List<CardStateHolder> sortedCardStateHolders =
                CardUtil.queryAndSortByRewardValue(query, masterCardStateHolders);

        this.currentCardStateHolders.clear();
        this.currentCardStateHolders.addAll(sortedCardStateHolders);

        submitList(this.currentCardStateHolders);

        notifyDataSetChanged();
    }

    public void change(
            final int position) {
        notifyItemChanged(position);
    }

    public void delete(
            final int position) {
        this.masterCardStateHolderMap.remove(getItem(position).getCard().getId());
        this.currentCardStateHolders.remove(position);

        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(
            @NonNull final ViewGroup parent,
            int viewType) {
        final View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card, parent, false);

        return new CardViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull final CardViewHolder cardViewHolder,
            final int position) {
        final CardStateHolder cardStateHolder = getItem(position);
        final Card card = cardStateHolder.getCard();

        cardViewHolder
                .getCardRow()
                .setOnClickListener(this.onCardRowClickListener);

        cardViewHolder
                .getCardIcon()
                .setImageResource(
                        this.bestCardActivity
                                .getResources()
                                .getIdentifier(
                                        card.getIconName(),
                                        "drawable",
                                        "com.ankitsingh.bestcard"));
        cardViewHolder
                .getCardName()
                .setText(card.getName());

        cardViewHolder
                .getCardType()
                .setText(
                        this.bestCardActivity
                                .getResources()
                                .getIdentifier(
                                        card.getCardType().name().toLowerCase(),
                                        "string",
                                        "com.ankitsingh.bestcard"));
        cardViewHolder
                .getCardMultiplier()
                .setText(Html.fromHtml("<span>&#x00D7</span> " + new DecimalFormat("0.##").format(card.getMultiplier()), Html.FROM_HTML_MODE_COMPACT));

        cardViewHolder
                .getCardExpanded()
                .setVisibility(cardStateHolder.isExpanded() ? View.VISIBLE : View.GONE);

        cardViewHolder
                .getCardActionContainer()
                .setVisibility(StringUtils.isBlank(cardStateHolder.getQuery()) ? View.VISIBLE : View.GONE);

        cardViewHolder
                .getCardExpandCollapse()
                .setImageResource(cardStateHolder.isExpanded() ? R.drawable.collapse : R.drawable.expand);

        cardViewHolder
                .getCardExpandCollapse()
                .setOnClickListener(this.onExpandCollapseClickListener);

        final int maximumHeight =
                CardViewUtil.createCardRewards(
                        bestCardActivity,
                        cardViewHolder.getCardRewards(),
                        cardStateHolder);

        System.out.println(maximumHeight);

        cardStateHolder.setMaximumHeight(maximumHeight);

        cardViewHolder
                .getCardDelete()
                .setOnClickListener(this.onDeleteClickListener);
    }

    @Getter
    public class CardViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout cardRow;

        private ImageView cardIcon;
        private TextView cardName;
        private TextView cardType;
        private TextView cardMultiplier;

        private LinearLayout cardExpanded;
        private ImageButton cardExpandCollapse;

        private LinearLayout cardRewards;

        private LinearLayout cardActionContainer;

        private ImageButton cardDelete;
        private ImageButton cardEdit;

        public CardViewHolder(final View cardView) {
            super(cardView);

            this.cardRow = (RelativeLayout) cardView;

            this.cardIcon = cardView.findViewById(R.id.card_icon);
            this.cardName = cardView.findViewById(R.id.card_name);
            this.cardType = cardView.findViewById(R.id.card_type);
            this.cardMultiplier = cardView.findViewById(R.id.card_multiplier);

            this.cardExpanded = cardView.findViewById(R.id.card_expanded);
            this.cardExpandCollapse = cardView.findViewById(R.id.card_expand_collapse);

            this.cardRewards = cardView.findViewById(R.id.card_rewards);

            this.cardActionContainer = cardView.findViewById(R.id.card_action_container);

            this.cardDelete = cardView.findViewById(R.id.card_delete);
            this.cardEdit = cardView.findViewById(R.id.card_edit);
        }
    }
}
