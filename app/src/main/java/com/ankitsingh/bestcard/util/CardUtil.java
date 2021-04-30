package com.ankitsingh.bestcard.util;

import com.ankitsingh.bestcard.adapters.holders.CardRewardStateHolder;
import com.ankitsingh.bestcard.adapters.holders.CardStateHolder;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Category;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.model.interval.DefaultInterval;
import com.ankitsingh.bestcard.model.interval.TimeInterval;
import com.google.common.util.concurrent.AtomicDouble;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class CardUtil {

    public static List<CardRewardStateHolder> sortByCategoryAndRewardValue(
            final Collection<CardRewardStateHolder> cardRewardStateHolders) {
        final List<CardRewardStateHolder> sortedCardRewardStateHolders = new ArrayList<>();

        sortedCardRewardStateHolders.addAll(cardRewardStateHolders);

        Collections.sort(sortedCardRewardStateHolders, (o1, o2) -> o2.getReward().getValue().compareTo(o1.getReward().getValue()));

        return sortedCardRewardStateHolders;
    }

    public static List<CardStateHolder> queryByCategoryAndSortByRewardValue(
            final Map<String, Category> savedCategoryMap,
            final String query,
            final Collection<CardStateHolder> currentCardStateHolders) {
        final List<CardStateHolder> sortedCardStateHolders = new ArrayList<>();

        currentCardStateHolders.forEach(cardStateHolder -> {
            sortedCardStateHolders.add(
                    getActiveCardStateHolder(
                            savedCategoryMap,
                            query,
                            cardStateHolder.getCard()));
        });

        Collections.sort(sortedCardStateHolders, (o1, o2) -> o2.getMaxReward().compareTo(o1.getMaxReward()));

        return sortedCardStateHolders;
    }

    private static CardStateHolder getActiveCardStateHolder(
            final Map<String, Category> savedCategoryMap,
            final String query,
            final Card card) {
        final List<Reward> activeRewards = new ArrayList<>();
        final AtomicDouble maxRewardValue = new AtomicDouble(0);

        card.getRewards().forEach(reward -> {
            if (isRewardActive(
                    savedCategoryMap,
                    query,
                    reward)) {
                activeRewards.add(reward);

                final Double currentReward = reward.getValue() * card.getMultiplier();

                if (maxRewardValue.get() < currentReward) {
                    maxRewardValue.set(currentReward);
                }
            }
        });

        return CardStateHolder.builder()
                .card(
                        Card.builder()
                                .id(card.getId())
                                .name(card.getName())
                                .iconName(card.getIconName())
                                .cardType(card.getCardType())
                                .annualFees(card.getAnnualFees())
                                .multiplier(card.getMultiplier())
                                .disabled(card.getDisabled())
                                .rewards(activeRewards)
                                .build())
                .isExpanded(!StringUtils.isBlank(query))
                .maxReward(maxRewardValue.get())
                .query(query)
                .build();
    }

    public static boolean isRewardActive(
            final Map<String, Category> savedCategoryMap,
            final String query,
            final Reward reward) {
        if (isMatchingCategory(
                savedCategoryMap,
                query,
                DataUtil.getCategory(reward.getCategoryId()))) {
            if (reward.getInterval() instanceof DefaultInterval) {
                return true;
            } else if (reward.getInterval() instanceof TimeInterval) {
                final LocalDate startDate = ((TimeInterval) reward.getInterval()).getStartDate() == null ? LocalDate.MIN : ((TimeInterval) reward.getInterval()).getStartDate();
                final LocalDate endDate = ((TimeInterval) reward.getInterval()).getEndDate() == null ? LocalDate.MAX : ((TimeInterval) reward.getInterval()).getEndDate();
                final LocalDate currentDate = LocalDate.now();

                return startDate.toEpochDay() <= currentDate.toEpochDay() && endDate.toEpochDay() >= currentDate.toEpochDay();
            }
        }

        return false;
    }

    public static boolean isPastReward(
            final Reward reward) {
        if (reward.getInterval() instanceof TimeInterval) {
            final LocalDate endDate = ((TimeInterval) reward.getInterval()).getEndDate() == null ? LocalDate.MAX : ((TimeInterval) reward.getInterval()).getEndDate();
            final LocalDate currentDate = LocalDate.now();

            if (endDate.toEpochDay() < currentDate.toEpochDay()) {
                return true;
            }
        }

        return false;
    }

    private static boolean isMatchingCategory(
            final Map<String, Category> savedCategoryMap,
            final String query,
            final Category category) {
        final Set<String> tags = new HashSet<>();

        if (Objects.nonNull(savedCategoryMap.get(category.getId()))) {
            tags.addAll(savedCategoryMap.get(category.getId()).getTags());
        } else {
            tags.addAll(category.getTags());
        }

        return StringUtils.isBlank(query)
                || category.getName().toLowerCase().contains(query)
                || isMatchingTag(query, tags);

    }

    private static boolean isMatchingTag(
            final String query,
            final Set<String> tags) {
        for (final String tag : tags) {
            if (tag.toLowerCase().contains(query)) {
                return true;
            }
        }

        return false;
    }

    public static List<CardStateHolder> queryByCardNameAndSortByRewardValue(
            final String query,
            final Collection<CardStateHolder> currentCardStateHolders) {
        final List<CardStateHolder> sortedCardStateHolders = new ArrayList<>();

        currentCardStateHolders.forEach(cardStateHolder -> {
            if (StringUtils.isBlank(query)
                    || cardStateHolder.getCard().getName().toLowerCase().contains(query)
                    || cardStateHolder.isSelected()) {
                sortedCardStateHolders.add(cardStateHolder);
            }
        });

        Collections.sort(sortedCardStateHolders, (o1, o2) -> o1.getCard().getName().compareTo(o2.getCard().getName()));

        return sortedCardStateHolders;
    }
}
