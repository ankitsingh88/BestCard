package com.ankitsingh.bestcard.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.holders.CardRepositoryViewHolder;
import com.ankitsingh.bestcard.adapters.holders.CardStateHolder;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.util.CardUtil;
import com.ankitsingh.bestcard.util.CardViewUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;


public class CardsRepositoryAdapter extends ListAdapter<CardStateHolder, CardRepositoryViewHolder> {

    private final View.OnClickListener onCardRowClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (inSelectionMode) {
                        final int position = recyclerView.getChildAdapterPosition(v);
                        final CardStateHolder currentCardStateHolder = getItem(position);

                        if (currentCardStateHolder.isSelected()) {
                            removeSelected(position, currentCardStateHolder);
                        } else {
                            addSelected(position, currentCardStateHolder);
                        }
                    }

                    ((View) v.getParent()).callOnClick();
                }
            };

    private final View.OnLongClickListener onCardRowLongClickListener =
            new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(final View v) {
                    if (!inSelectionMode) {
                        inSelectionMode = true;
                    }

                    final int position = recyclerView.getChildAdapterPosition(v);
                    final CardStateHolder currentCardStateHolder = getItem(position);

                    if (currentCardStateHolder.isSelected()) {
                        removeSelected(position, currentCardStateHolder);
                    } else {
                        addSelected(position, currentCardStateHolder);
                    }

                    ((View) v.getParent()).callOnClick();

                    return true;
                }
            };

    private final ImageView.OnClickListener onCardIconClickListener =
            new ImageView.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    if (!inSelectionMode) {
                        inSelectionMode = true;
                    }

                    ((View) v.getParent().getParent().getParent()).callOnClick();
                }
            };

    private final View.OnClickListener onAnnualFeesClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = recyclerView.getChildAdapterPosition((View) v.getParent().getParent().getParent());

                    CardViewUtil
                            .createCardAnnualFeesDialog(
                                    appCompatActivity,
                                    getItem(position).getCard());
                }
            };

    private final RecyclerView recyclerView;
    private final AppCompatActivity appCompatActivity;

    private final Map<String, CardStateHolder> masterCardStateHolderMap = new HashMap<>();
    private final List<CardStateHolder> currentCardStateHolders = new ArrayList<>();

    @Getter
    private boolean inSelectionMode = false;

    private static final DiffUtil.ItemCallback<CardStateHolder> DIFF_CALLBACK =
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

    public CardsRepositoryAdapter(
            final RecyclerView recyclerView,
            final Context context,
            final Set<String> currentCardIds,
            final List<CardStateHolder> cardStateHolders) {
        super(DIFF_CALLBACK);

        this.recyclerView = recyclerView;
        this.appCompatActivity = (AppCompatActivity) context;

        final List<CardStateHolder> filteredCardStateHolders = new ArrayList<>();

        cardStateHolders.forEach(cardStateHolder -> {
            if (!currentCardIds.contains(cardStateHolder.getCard().getId())) {
                this.masterCardStateHolderMap.put(cardStateHolder.getCard().getId(), cardStateHolder);

                filteredCardStateHolders.add(cardStateHolder);
            }
        });

        changeDataSet(null, filteredCardStateHolders);

        notifyDataSetChanged();
    }

    public void filter(
            final String originalQuery) {
        final String query = originalQuery == null ? null : originalQuery.toLowerCase(Locale.getDefault());

        changeDataSet(query, this.masterCardStateHolderMap.values());

        notifyDataSetChanged();
    }

    private void changeDataSet(
            final String query,
            final Collection<CardStateHolder> masterCardStateHolders) {
        final List<CardStateHolder> sortedCardStateHolders =
                CardUtil.queryByCardNameAndSortByRewardValue(query, masterCardStateHolders);

        this.currentCardStateHolders.clear();
        this.currentCardStateHolders.addAll(sortedCardStateHolders);

        submitList(this.currentCardStateHolders);
    }

    private void removeSelected(
            final int position,
            final CardStateHolder cardStateHolder) {
        cardStateHolder.setSelected(false);

        final List<CardStateHolder> currentSelectedCardStateHolders =
                this.currentCardStateHolders.stream()
                        .filter(CardStateHolder::isSelected)
                        .collect(Collectors.toList());

        if (currentSelectedCardStateHolders.isEmpty()) {
            inSelectionMode = false;
        }

        notifyItemChanged(position);
    }

    public void clearSelected() {
        this.currentCardStateHolders.forEach(cardStateHolder -> {
            cardStateHolder.setSelected(false);
        });

        notifyDataSetChanged();
    }

    private void addSelected(
            final int position,
            final CardStateHolder cardStateHolder) {
        cardStateHolder.setSelected(true);

        notifyItemChanged(position);
    }

    public List<CardStateHolder> getCardStateHolders() {
        return new ArrayList<>(this.masterCardStateHolderMap.values());
    }

    public List<CardStateHolder> getSelectedCardStateHolders() {
        return this.masterCardStateHolderMap.values().stream()
                .filter(CardStateHolder::isSelected)
                .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public CardRepositoryViewHolder onCreateViewHolder(
            @NonNull final ViewGroup parent,
            int viewType) {
        final View cardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card_repository, parent, false);

        return new CardRepositoryViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull final CardRepositoryViewHolder cardRepositoryViewHolder,
            final int position) {
        final CardStateHolder cardStateHolder = getItem(position);
        final Card card = cardStateHolder.getCard();

        cardRepositoryViewHolder
                .getCardRow()
                .setOnClickListener(this.onCardRowClickListener);

        cardRepositoryViewHolder
                .getCardRow()
                .setOnLongClickListener(this.onCardRowLongClickListener);

        cardRepositoryViewHolder
                .getCardIcon()
                .setImageResource(
                        this.appCompatActivity
                                .getResources()
                                .getIdentifier(
                                        card.getIconName(),
                                        "drawable",
                                        "com.ankitsingh.bestcard"));
        cardRepositoryViewHolder
                .getCardIcon()
                .setOnClickListener(this.onCardIconClickListener);

        if (cardStateHolder.isSelected()) {
            cardRepositoryViewHolder
                    .getCardRow()
                    .setBackgroundResource(R.drawable.item_selected);

            cardRepositoryViewHolder
                    .getCardIcon()
                    .setForeground(this.appCompatActivity.getDrawable(R.drawable.icon_selected));
        } else {
            cardRepositoryViewHolder
                    .getCardRow()
                    .setBackgroundResource(R.drawable.unselected);

            cardRepositoryViewHolder
                    .getCardIcon()
                    .setForeground(this.appCompatActivity.getDrawable(R.drawable.icon_to_select));
        }

        cardRepositoryViewHolder
                .getCardName()
                .setText(card.getName());

        cardRepositoryViewHolder
                .getCardType()
                .setText(
                        this.appCompatActivity
                                .getResources()
                                .getIdentifier(
                                        card.getCardType().name().toLowerCase(),
                                        "string",
                                        "com.ankitsingh.bestcard"));

        cardRepositoryViewHolder
                .getCardAnnualFees()
                .setVisibility(
                        Objects.nonNull(card.getAnnualFees()) && card.getAnnualFees() != 0
                                ? View.VISIBLE : View.GONE);

        if (Objects.nonNull(card.getAnnualFees())) {
            cardRepositoryViewHolder
                    .getCardAnnualFees()
                    .setText(Html.fromHtml("<span>&#36;</span> " + card.getAnnualFees(), Html.FROM_HTML_MODE_COMPACT));
            cardRepositoryViewHolder
                    .getCardAnnualFees()
                    .setOnClickListener(this.onAnnualFeesClickListener);
        }
    }
}
