package com.ankitsingh.bestcard.util;

import com.ankitsingh.bestcard.adapters.CardAdapter;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.model.interval.DefaultInterval;
import com.ankitsingh.bestcard.model.interval.TimeInterval;
import com.google.common.util.concurrent.AtomicDouble;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class CardUtil {

    public static List<CardAdapter.CardStateHolder> queryAndSortByRewardValue(
            final String query,
            final Collection<CardAdapter.CardStateHolder> currentCardStateHolders) {
        final List<CardAdapter.CardStateHolder> sortedCardStateHolders = new ArrayList<>();

        currentCardStateHolders.forEach(cardStateHolder -> {
            final Optional<CardAdapter.CardStateHolder> currentCardStateHolder =
                    getActiveCardStateHolder(query, cardStateHolder.getCard());

            if (currentCardStateHolder.isPresent()) {
                sortedCardStateHolders.add(currentCardStateHolder.get());
            }
        });

        Collections.sort(sortedCardStateHolders, (o1, o2) -> o2.getMaxReward().compareTo(o1.getMaxReward()));

        return sortedCardStateHolders;
    }

    private static Optional<CardAdapter.CardStateHolder> getActiveCardStateHolder(
            final String query,
            final Card card) {
        final List<Reward> activeRewards = new ArrayList<>();
        final AtomicDouble maxRewardValue = new AtomicDouble(0);

        card.getRewards().forEach(reward -> {
            if (isRewardActive(query, reward)) {
                activeRewards.add(reward);

                final Double currentReward = reward.getValue() * card.getMultiplier();

                if (maxRewardValue.get() < currentReward) {
                    maxRewardValue.set(currentReward);
                }
            }
        });

        if (!activeRewards.isEmpty()) {
            return Optional.of(
                    CardAdapter.CardStateHolder.builder()
                            .card(
                                    Card.builder()
                                            .id(card.getId())
                                            .name(card.getName())
                                            .iconName(card.getIconName())
                                            .cardType(card.getCardType())
                                            .multiplier(card.getMultiplier())
                                            .rewards(activeRewards)
                                            .build())
                            .isExpanded(StringUtils.isBlank(query) ? false : true)
                            .maxReward(maxRewardValue.get())
                            .query(query)
                            .build());
        } else {
            return Optional.empty();
        }
    }

    private static boolean isRewardActive(
            final String query,
            final Reward reward) {
        if (StringUtils.isBlank(query)
                || reward.getCategory().getName().toLowerCase().contains(query)) {
            if (reward.getInterval() instanceof DefaultInterval) {
                return true;
            } else if (reward.getInterval() instanceof TimeInterval) {
                final LocalDate startDate = ((TimeInterval) reward.getInterval()).getStartDate() == null ? LocalDate.MIN : ((TimeInterval) reward.getInterval()).getStartDate();
                final LocalDate endDate = ((TimeInterval) reward.getInterval()).getEndDate() == null ? LocalDate.MAX : ((TimeInterval) reward.getInterval()).getEndDate();
                final LocalDate currentDate = LocalDate.now();
                if (startDate.toEpochDay() < currentDate.toEpochDay() && endDate.toEpochDay() > currentDate.toEpochDay()) {
                    return true;
                }
            }
        }

        return false;
    }
}
