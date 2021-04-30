package com.ankitsingh.bestcard.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.holders.CardRewardStateHolder;
import com.ankitsingh.bestcard.adapters.holders.CardRewardViewHolder;
import com.ankitsingh.bestcard.model.Category;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.model.interval.DefaultInterval;
import com.ankitsingh.bestcard.model.interval.Interval;
import com.ankitsingh.bestcard.model.interval.TimeInterval;
import com.ankitsingh.bestcard.util.CardRewardViewUtil;
import com.ankitsingh.bestcard.util.CardUtil;
import com.ankitsingh.bestcard.util.CardViewUtil;
import com.ankitsingh.bestcard.util.DataUtil;
import com.google.android.material.checkbox.MaterialCheckBox;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;

public class CardRewardsAdapter extends ListAdapter<CardRewardStateHolder, CardRewardViewHolder> {

    private final MaterialCheckBox.OnCheckedChangeListener onCardRewardEnabledCheckedListener =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(
                        final CompoundButton buttonView,
                        final boolean isChecked) {
                    final int position = recyclerView.getChildAdapterPosition((View) buttonView.getParent());

                    enable(isChecked, position);
                }
            };

    private final View.OnClickListener onEditClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v) {
                    final int position = recyclerView.getChildAdapterPosition((View) v.getParent());

                    CardRewardViewUtil
                            .createCardRewardEditDialog(
                                    appCompatActivity,
                                    CardRewardsAdapter.this,
                                    position,
                                    getItem(position).getReward());
                }
            };

    private final View.OnClickListener onDeleteClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v) {
                    final int position = recyclerView.getChildAdapterPosition((View) v.getParent());

                    CardRewardViewUtil
                            .createCardRewardDeleteDialog(
                                    appCompatActivity,
                                    CardRewardsAdapter.this,
                                    position,
                                    getItem(position).getReward());
                }
            };

    private final RecyclerView recyclerView;
    private final AppCompatActivity appCompatActivity;

    @Getter private final List<CardRewardStateHolder> currentCardRewardStateHolders = new ArrayList<>();

    public static final DiffUtil.ItemCallback<CardRewardStateHolder> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CardRewardStateHolder>() {
                @Override
                public boolean areItemsTheSame(
                        final CardRewardStateHolder oldItem,
                        final CardRewardStateHolder newItem) {
                    return oldItem.getReward().equals(newItem.getReward());
                }

                @Override
                public boolean areContentsTheSame(
                        final CardRewardStateHolder oldItem,
                        final CardRewardStateHolder newItem) {
                    return oldItem.getReward().equals(newItem.getReward());
                }
            };

    public CardRewardsAdapter(
            final RecyclerView recyclerView,
            final Context context,
            final List<CardRewardStateHolder> cardRewardStateHolders) {
        super(DIFF_CALLBACK);

        this.recyclerView = recyclerView;
        this.appCompatActivity = (AppCompatActivity) context;

        changeDataSet(cardRewardStateHolders);

        notifyDataSetChanged();
    }

    private void changeDataSet(
            final Collection<CardRewardStateHolder> cardRewardStateHolders) {
        final List<CardRewardStateHolder> sortedCardRewardStateHolders =
                CardUtil.sortByCategoryAndRewardValue(
                        cardRewardStateHolders.stream()
                                .filter(cardRewardStateHolder -> !CardUtil.isPastReward(cardRewardStateHolder.getReward()))
                                .collect(Collectors.toList()));

        this.currentCardRewardStateHolders.clear();
        this.currentCardRewardStateHolders.addAll(sortedCardRewardStateHolders);

        submitList(this.currentCardRewardStateHolders);
    }

    public void delete(
            final int position) {
        this.currentCardRewardStateHolders.remove(position);

        submitList(this.currentCardRewardStateHolders);

        notifyItemRemoved(position);

        notifyParentViewOfChange();
    }

    public void add(
            final Reward reward) {
        final List<CardRewardStateHolder> newCardRewardStateHolders =
                this.currentCardRewardStateHolders.stream().collect(Collectors.toList());

        newCardRewardStateHolders.add(CardRewardStateHolder.builder().reward(reward).build());

        changeDataSet(newCardRewardStateHolders);

        notifyDataSetChanged();

        notifyParentViewOfChange();
    }

    private void enable(
            final boolean enable,
            final int position) {
        this.currentCardRewardStateHolders.get(position).getReward().setDisabled(!enable);

        notifyItemChanged(position);

        notifyParentViewOfChange();
    }

    public void update(
            final int position,
            final Reward reward) {
        final CardRewardStateHolder cardRewardStateHolderToUpdate =
                this.currentCardRewardStateHolders.get(position);

        cardRewardStateHolderToUpdate.setReward(reward);

        changeDataSet(this.currentCardRewardStateHolders);

        changePositions(position, reward, this.currentCardRewardStateHolders);

        notifyParentViewOfChange();
    }

    private void notifyParentViewOfChange() {
        this.recyclerView.callOnClick();
    }

    private void changePositions(
            final int oldPosition,
            final Reward reward,
            final List<CardRewardStateHolder> newCardRewardStateHolders) {
        int newPosition = -1;

        for (int i = 0; i < newCardRewardStateHolders.size(); ++i) {
            if (newCardRewardStateHolders.get(i).getReward().getCategoryId().equals(reward.getCategoryId())) {
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

    @NonNull
    @Override
    public CardRewardViewHolder onCreateViewHolder(
            @NonNull final ViewGroup parent,
            int viewType) {
        final View cardRewardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_card_reward, parent, false);

        return new CardRewardViewHolder(cardRewardView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull final CardRewardViewHolder cardRewardViewHolder,
            final int position) {
        final CardRewardStateHolder cardRewardStateHolder = getItem(position);
        final Reward reward = cardRewardStateHolder.getReward();
        final Category category = DataUtil.getCategory(reward.getCategoryId());
        final Interval interval = reward.getInterval();

        cardRewardViewHolder
                .getCardRewardEnabled()
                .setOnCheckedChangeListener(null);
        cardRewardViewHolder
                .getCardRewardEnabled()
                .setChecked(!reward.getDisabled());
        cardRewardViewHolder
                .getCardRewardEnabled()
                .setOnCheckedChangeListener(this.onCardRewardEnabledCheckedListener);

        cardRewardViewHolder
                .getCategoryIcon()
                .setImageResource(
                        this.appCompatActivity
                                .getResources()
                                .getIdentifier(
                                        category.getIconName(),
                                        "drawable",
                                        "com.ankitsingh.bestcard"));

        cardRewardViewHolder
                .getCategoryName()
                .setText(category.getName());

        cardRewardViewHolder
                .getCategoryRewardValue()
                .setText(Html.fromHtml(new DecimalFormat("0.##").format(reward.getValue()) + " <span>&#37;</span>", Html.FROM_HTML_MODE_COMPACT));

        if (interval instanceof DefaultInterval) {
            cardRewardViewHolder.getCategoryInterval().setVisibility(View.GONE);
        } else if (interval instanceof TimeInterval) {
            cardRewardViewHolder.getCategoryInterval().setVisibility(View.VISIBLE);

            cardRewardViewHolder
                    .getCategoryInterval()
                    .setText(
                            Html.fromHtml(
                                    CardViewUtil.getFormattedDescriptiveInterval(interval, false),
                                    Html.FROM_HTML_MODE_COMPACT));
        }

        setDisabled(cardRewardViewHolder, reward.getDisabled());

        cardRewardViewHolder
                .getCategoryEdit()
                .setOnClickListener(this.onEditClickListener);

        cardRewardViewHolder
                .getCategoryDelete()
                .setOnClickListener(this.onDeleteClickListener);
    }

    private void setDisabled(
            final CardRewardViewHolder cardRewardViewHolder,
            final boolean isDisabled) {
        final float alpha = isDisabled ? 0.3f : 1f;

        cardRewardViewHolder
                .getCategoryIcon()
                .setAlpha(alpha);
        cardRewardViewHolder
                .getCategoryName()
                .setAlpha(alpha);
        cardRewardViewHolder
                .getCategoryRewardValue()
                .setAlpha(alpha);
        cardRewardViewHolder
                .getCategoryInterval()
                .setAlpha(alpha);
    }
}
