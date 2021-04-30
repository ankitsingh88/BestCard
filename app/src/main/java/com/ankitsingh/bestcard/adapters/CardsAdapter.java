package com.ankitsingh.bestcard.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.holders.CardStateHolder;
import com.ankitsingh.bestcard.adapters.holders.CardViewHolder;
import com.ankitsingh.bestcard.fragments.CardEditFormDialogFragment;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.util.CardUtil;
import com.ankitsingh.bestcard.util.CardViewUtil;
import com.ankitsingh.bestcard.util.DataUtil;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

public class CardsAdapter extends ListAdapter<CardStateHolder, CardViewHolder> {

    private final View.OnClickListener onCardRowClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = recyclerView.getChildAdapterPosition(v);

                    final CardStateHolder currentCardStateHolder = getItem(position);
                    final boolean isExpanded = currentCardStateHolder.isExpanded();

                    if (isExpanded) {
                        currentCardStateHolder.setExpanded(false);

                        notifyItemChanged(position);

                        recyclerView.scrollToPosition(position);
                    } else {
                        for (int i = 0; i < getCurrentList().size(); ++i) {
                            final CardStateHolder cardStateHolder = getCurrentList().get(i);

                            if (position != i) {

                                if (cardStateHolder.isExpanded() && StringUtils.isBlank(cardStateHolder.getQuery())) {
                                    cardStateHolder.setExpanded(false);

                                    notifyItemChanged(i);
                                }
                            } else {
                                cardStateHolder.setExpanded(true);

                                notifyItemChanged(position);

                                recyclerView.scrollToPosition(position);
                            }
                        }
                    }
                }
            };

    private final View.OnClickListener onExpandCollapseClickListener =
            v -> ((View) v.getParent().getParent()).callOnClick();

    private final View.OnClickListener onCardEnabledClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = recyclerView.getChildAdapterPosition((View) v.getParent().getParent().getParent().getParent());

                    if (((SwitchMaterial) v).isChecked()) {
                        CardsAdapter.this.enable(getItem(position).getCard());
                    } else {
                        CardsAdapter.this.disable(getItem(position).getCard());
                    }
                }
            };

    private final View.OnClickListener onDeleteClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = recyclerView.getChildAdapterPosition((View) v.getParent().getParent().getParent().getParent());

                    CardViewUtil
                            .createCardDeleteDialog(
                                    appCompatActivity,
                                    CardsAdapter.this,
                                    position,
                                    getSavedCard(getItem(position).getCard().getId()));
                }
            };

    private final View.OnClickListener onEditClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = recyclerView.getChildAdapterPosition((View) v.getParent().getParent().getParent().getParent());

                    CardEditFormDialogFragment.display(
                            getBestCardActivity(),
                            CardsAdapter.this,
                            position,
                            getSavedCard(getItem(position).getCard().getId()));
                }
            };

    private final View.OnClickListener onAnnualFeesClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = recyclerView.getChildAdapterPosition((View) v.getParent().getParent().getParent().getParent());

                    CardViewUtil
                            .createCardAnnualFeesDialog(
                                    appCompatActivity,
                                    getSavedCard(getItem(position).getCard().getId()));
                }
            };

    private final View.OnClickListener onMultiplierClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = recyclerView.getChildAdapterPosition((View) v.getParent().getParent().getParent().getParent());

                    CardViewUtil
                            .createCardMultiplierDialog(
                                    appCompatActivity,
                                    getSavedCard(getItem(position).getCard().getId()));
                }
            };

    private final RecyclerView recyclerView;
    private final AppCompatActivity appCompatActivity;

    private final Map<String, CardStateHolder> masterCardStateHolderMap = new HashMap<>();
    @Getter private final List<CardStateHolder> currentCardStateHolders = new ArrayList<>();

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
                    return oldItem.getCard().getId().equals(newItem.getCard().getId())
                            && oldItem.getCard().getMultiplier().equals(newItem.getCard().getMultiplier())
                            && oldItem.getCard().getDisabled().equals(newItem.getCard().getDisabled());
                }
            };

    public CardsAdapter(
            final RecyclerView recyclerView,
            final Context context,
            final List<CardStateHolder> cardStateHolders) {
        super(DIFF_CALLBACK);

        this.recyclerView = recyclerView;
        this.appCompatActivity = (AppCompatActivity) context;

        cardStateHolders.forEach(cardStateHolder -> {
            this.masterCardStateHolderMap.put(cardStateHolder.getCard().getId(), cardStateHolder);
        });

        changeDataSet(null, this.masterCardStateHolderMap.values());

        notifyDataSetChanged();
    }

    public void filter(
            final String originalQuery) {
        final String query = originalQuery == null ? null : originalQuery.toLowerCase(Locale.getDefault());

        final List<CardStateHolder> sortedCardStateHolders =
                CardUtil.queryByCategoryAndSortByRewardValue(
                        getBestCardActivity().getDataLoader().getSavedCategoriesMap(),
                        query,
                        this.masterCardStateHolderMap.values());

        changeDataSet(
                query,
                sortedCardStateHolders.stream()
                        .filter(cardStateHolder -> {
                            final List<Reward> rewards =
                                    cardStateHolder.getCard().getRewards().stream()
                                            .filter(reward -> !reward.getDisabled())
                                            .collect(Collectors.toList());

                            return CollectionUtils.isNotEmpty(rewards);
                        })
                    .collect(Collectors.toList()));

        notifyDataSetChanged();
    }

    private void changeDataSet(
            final String query,
            final Collection<CardStateHolder> masterCardStateHolders) {
        final List<CardStateHolder> sortedCardStateHolders =
                CardUtil.queryByCategoryAndSortByRewardValue(
                        getBestCardActivity().getDataLoader().getSavedCategoriesMap(),
                        query,
                        masterCardStateHolders);

        this.currentCardStateHolders.clear();
        this.currentCardStateHolders.addAll(sortedCardStateHolders);

        submitList(this.currentCardStateHolders);
    }

    public void delete(
            final int position) {
        final Card card = getItem(position).getCard();

        this.masterCardStateHolderMap.remove(card.getId());
        this.currentCardStateHolders.remove(position);

        submitList(this.currentCardStateHolders);

        notifyItemRemoved(position);

        deleteCard(card);

        getBestCardActivity().getCardsFragment().setupTitleAndList();
    }

    public void add(
            final Card card) {
        this.masterCardStateHolderMap.put(card.getId(), CardStateHolder.builder().card(card.clone()).build());

        changeDataSet(null, this.masterCardStateHolderMap.values());

        notifyDataSetChanged();

        saveCard(card);
    }

    public void add(
            final List<Card> cards) {
        cards.forEach(card -> {
            this.masterCardStateHolderMap.put(card.getId(), CardStateHolder.builder().card(card.clone()).build());
        });

        changeDataSet(null, this.masterCardStateHolderMap.values());

        notifyDataSetChanged();

        saveCards(cards);
    }

    private void enable(
            final Card card) {
        this.masterCardStateHolderMap.get(card.getId()).getCard().setDisabled(false);

        for (int i = 0; i < this.currentCardStateHolders.size(); ++i) {
            if (this.currentCardStateHolders.get(i).getCard().getId().equals(card.getId())) {
                this.currentCardStateHolders.get(i).getCard().setDisabled(false);

                notifyItemChanged(i);
            }
        }

        saveCard(card);
    }

    private void disable(
            final Card card) {
        this.masterCardStateHolderMap.get(card.getId()).getCard().setDisabled(true);

        for (int i = 0; i < this.currentCardStateHolders.size(); ++i) {
            if (this.currentCardStateHolders.get(i).getCard().getId().equals(card.getId())) {
                this.currentCardStateHolders.get(i).getCard().setDisabled(true);

                notifyItemChanged(i);
            }
        }

        saveCard(card);
    }

    public void update(
            final int position,
            final Card card) {
        final CardStateHolder cardStateHolder = this.masterCardStateHolderMap.get(card.getId());

        cardStateHolder.getCard().setCardType(card.getCardType());
        cardStateHolder.getCard().setAnnualFees(card.getAnnualFees());
        cardStateHolder.getCard().setMultiplier(card.getMultiplier());
        cardStateHolder.getCard().setRewards(card.getRewards());

        changeDataSet(null, this.masterCardStateHolderMap.values());

        changePositions(position, card, this.currentCardStateHolders);

        saveCard(card);
    }

    private Card getSavedCard(
            final String cardId) {
        return getBestCardActivity().getDataLoader().getSavedCardsMap().get(cardId).clone();
    }

    private void deleteCard(
            final Card card) {
        ((BestCardActivity) appCompatActivity).getDataLoader().deleteCard(card);
    }

    private void saveCard(
            final Card card) {
        ((BestCardActivity) appCompatActivity).getDataLoader().saveCard(card);
    }

    private void saveCards(
            final List<Card> cards) {
        ((BestCardActivity) appCompatActivity).getDataLoader().saveCards(cards);

        final Set<String> categoryIdsToAdd = new HashSet<>();

        cards.forEach(card -> {
            categoryIdsToAdd.addAll(card.getRewards().stream().map(Reward::getCategoryId).collect(Collectors.toList()));
        });

        getBestCardActivity()
                .getDataLoader()
                .saveCategories(
                        categoryIdsToAdd.stream()
                                .filter(categoryId -> !getBestCardActivity().getDataLoader().getSavedCategoriesMap().containsKey(categoryId))
                                .map(DataUtil::getCategory)
                                .collect(Collectors.toList()));
    }

    private void changePositions(
            final int oldPosition,
            final Card card,
            final List<CardStateHolder> newCardStateHolders) {
        int newPosition = -1;

        for (int i = 0; i < newCardStateHolders.size(); ++i) {
            if (newCardStateHolders.get(i).getCard().getId().equals(card.getId())) {
                newPosition = i;
                break;
            }
        }

        if (newPosition != -1) {
            notifyItemMoved(oldPosition, newPosition);

            if (oldPosition < newPosition) {
                notifyItemRangeChanged(oldPosition, newPosition - oldPosition + 1);
            } else {
                notifyItemRangeChanged(newPosition, oldPosition - newPosition + 1);
            }

            this.recyclerView.scrollToPosition(newPosition);
        } else {
            notifyDataSetChanged();
        }
    }

    private BestCardActivity getBestCardActivity() {
        return (BestCardActivity) this.appCompatActivity;
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
                .getCardIcon()
                .setImageResource(
                        this.appCompatActivity
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
                        this.appCompatActivity
                                .getResources()
                                .getIdentifier(
                                        card.getCardType().name().toLowerCase(),
                                        "string",
                                        "com.ankitsingh.bestcard"));

        cardViewHolder
                .getCardMultiplier()
                .setVisibility(
                        Objects.nonNull(card.getMultiplier()) && card.getMultiplier() != 1
                                ? View.VISIBLE : View.GONE);

        if (Objects.nonNull(card.getMultiplier()) && card.getMultiplier() != 1) {
            cardViewHolder
                    .getCardMultiplier()
                    .setText(Html.fromHtml("<span>&#x00D7</span> " + new DecimalFormat("0.##").format(card.getMultiplier()), Html.FROM_HTML_MODE_COMPACT));
            cardViewHolder
                    .getCardMultiplier()
                    .setOnClickListener(this.onMultiplierClickListener);
        }

        cardViewHolder
                .getCardAnnualFees()
                .setVisibility(
                        Objects.nonNull(card.getAnnualFees()) && card.getAnnualFees() != 0
                                ? View.VISIBLE : View.GONE);

        if (Objects.nonNull(card.getAnnualFees())) {
            cardViewHolder
                    .getCardAnnualFees()
                    .setText(Html.fromHtml("<span>&#36;</span> " + card.getAnnualFees(), Html.FROM_HTML_MODE_COMPACT));
            cardViewHolder
                    .getCardAnnualFees()
                    .setOnClickListener(this.onAnnualFeesClickListener);
        }

        cardViewHolder
                .getCardExpandCollapse()
                .setOnClickListener(this.onExpandCollapseClickListener);

        cardViewHolder
                .getCardEnabled()
                .setOnClickListener(this.onCardEnabledClickListener);

        setDisabled(cardViewHolder, card.getDisabled());

        CardViewUtil.createCardRewards(
                appCompatActivity,
                cardViewHolder.getCardRewards(),
                cardStateHolder);

        cardViewHolder
                .getCardDelete()
                .setOnClickListener(this.onDeleteClickListener);

        cardViewHolder
                .getCardEdit()
                .setOnClickListener(this.onEditClickListener);

        cardViewHolder
                .getCardExpanded()
                .setVisibility(cardStateHolder.isExpanded() ? View.VISIBLE : View.GONE);

        cardViewHolder
                .getCardActionContainer()
                .setVisibility(StringUtils.isBlank(cardStateHolder.getQuery()) ? View.VISIBLE : View.GONE);

        cardViewHolder
                .getCardExpandCollapse()
                .setImageResource(cardStateHolder.isExpanded() ? R.drawable.collapse : R.drawable.expand);

        if (cardStateHolder.isExpanded()) {
            if (StringUtils.isBlank(cardStateHolder.getQuery())) {
                cardViewHolder
                        .getCardExpandCollapse()
                        .setVisibility(View.VISIBLE);
                cardViewHolder
                        .getCardEnabled()
                        .setVisibility(View.VISIBLE);
                cardViewHolder
                        .getCardRow()
                        .setCardElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, appCompatActivity.getResources().getDisplayMetrics()));

                final TypedValue colorBackground = new TypedValue();
                this.appCompatActivity.getTheme().resolveAttribute(android.R.attr.windowBackground, colorBackground, true);
                cardViewHolder
                        .getCardRow()
                        .setCardBackgroundColor(colorBackground.data);

                cardViewHolder
                        .getCardRow()
                        .setOnClickListener(this.onCardRowClickListener);
            } else {
                cardViewHolder
                        .getCardExpandCollapse()
                        .setVisibility(View.INVISIBLE);
                cardViewHolder
                        .getCardEnabled()
                        .setVisibility(View.INVISIBLE);
                cardViewHolder
                        .getCardRow()
                        .setCardElevation(0);
                cardViewHolder
                        .getCardRow()
                        .setCardBackgroundColor(Color.TRANSPARENT);

                cardViewHolder
                        .getCardRow()
                        .setOnClickListener(null);
            }
        } else {
            cardViewHolder
                    .getCardExpandCollapse()
                    .setVisibility(View.VISIBLE);
            cardViewHolder
                    .getCardEnabled()
                    .setVisibility(View.VISIBLE);
            cardViewHolder
                    .getCardRow()
                    .setCardElevation(0);
            cardViewHolder
                    .getCardRow()
                    .setCardBackgroundColor(Color.TRANSPARENT);

            cardViewHolder
                    .getCardRow()
                    .setOnClickListener(this.onCardRowClickListener);
        }
    }

    private void setDisabled(
            final CardViewHolder cardViewHolder,
            final boolean isDisabled) {
        final float alpha = isDisabled ? 0.3f : 1f;

        cardViewHolder
                .getCardIcon()
                .setAlpha(alpha);
        cardViewHolder
                .getCardName()
                .setAlpha(alpha);
        cardViewHolder
                .getCardType()
                .setAlpha(alpha);
        cardViewHolder
                .getCardMultiplier()
                .setAlpha(alpha);
        cardViewHolder
                .getCardAnnualFees()
                .setAlpha(alpha);
        cardViewHolder
                .getCardRewards()
                .setAlpha(alpha);
        cardViewHolder
                .getCardEdit()
                .setAlpha(alpha);
        cardViewHolder
                .getCardDelete()
                .setAlpha(alpha);
        cardViewHolder
                .getCardExpandCollapse()
                .setAlpha(alpha);

        cardViewHolder
                .getCardEnabled()
                .setChecked(!isDisabled);
    }
}
