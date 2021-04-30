package com.ankitsingh.bestcard.util;

import android.graphics.Typeface;
import android.text.Html;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.CardsAdapter;
import com.ankitsingh.bestcard.adapters.holders.CardStateHolder;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.model.interval.Interval;
import com.ankitsingh.bestcard.model.interval.TimeInterval;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CardViewUtil {

    public static void createCardRewards(
            final AppCompatActivity appCompatActivity,
            final LinearLayout cardRewards,
            final CardStateHolder cardStateHolder) {
        cardRewards.removeAllViews();

        final Space cardRewardsPaddingTop = new Space(appCompatActivity);
        cardRewardsPaddingTop.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, appCompatActivity.getResources().getDisplayMetrics())));

        cardRewards.addView(cardRewardsPaddingTop);

        final List<Pair<Double, List<Reward>>> groupedCardRewards =
                groupedCardRewards(cardStateHolder);

        for (int i = 0; i < groupedCardRewards.size(); ++i) {
            final Double rewardValue = groupedCardRewards.get(i).getKey();
            final List<Reward> groupedRewards = groupedCardRewards.get(i).getValue();

            final LinearLayout groupedRewardContainer = new LinearLayout(appCompatActivity);

            groupedRewardContainer.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            groupedRewardContainer.setOrientation(LinearLayout.HORIZONTAL);

            createCardRewardCategories(appCompatActivity, groupedRewardContainer, cardStateHolder.getQuery(), rewardValue, groupedRewards);

            groupedRewardContainer.measure(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            cardRewards.addView(groupedRewardContainer);

            if (i != groupedCardRewards.size() - 1) {
                final Space groupedRewardContainerBottomSpace = new Space(appCompatActivity);
                groupedRewardContainerBottomSpace.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, appCompatActivity.getResources().getDisplayMetrics())));

                cardRewards.addView(groupedRewardContainerBottomSpace);
            }
        }

        final Space cardRewardsPaddingBottom = new Space(appCompatActivity);
        cardRewardsPaddingBottom.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, appCompatActivity.getResources().getDisplayMetrics())));

        cardRewards.addView(cardRewardsPaddingBottom);
    }

    private static List<Pair<Double, List<Reward>>> groupedCardRewards(
            final CardStateHolder cardStateHolder) {
        final List<Pair<Double, List<Reward>>> sortedGroupedCardRewards = new ArrayList<>();

        cardStateHolder.getCard().getRewards().stream()
                .collect(Collectors.groupingBy(reward -> reward.getValue()*cardStateHolder.getCard().getMultiplier()))
                .forEach((rewardValue, rewards) -> {
                    sortedGroupedCardRewards.add(
                            Pair.of(
                                    rewardValue,
                                    rewards.stream()
                                            .sorted(Comparator.comparing(reward -> DataUtil.getCategory(reward.getCategoryId()).getName())).collect(Collectors.toList())));
                });

        Collections.sort(sortedGroupedCardRewards, (o1, o2) -> o2.getKey().compareTo(o1.getKey()));

        return sortedGroupedCardRewards;
    }

    private static void createCardRewardCategories(
            final AppCompatActivity appCompatActivity,
            final LinearLayout groupedRewardContainer,
            final String query,
            final Double rewardValue,
            final List<Reward> groupedRewards) {
        final TextView rewardValueText = new TextView(appCompatActivity);

        rewardValueText.setLayoutParams(
                new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96, appCompatActivity.getResources().getDisplayMetrics()),
                        LinearLayout.LayoutParams.MATCH_PARENT));
        rewardValueText.setPadding(
                0, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, appCompatActivity.getResources().getDisplayMetrics()),
                0);
        rewardValueText.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);
        rewardValueText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        rewardValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        rewardValueText.setText(
                Html.fromHtml(
                        new DecimalFormat("0.##").format(rewardValue) + " <span>&#37;</span>",
                        Html.FROM_HTML_MODE_COMPACT));

        groupedRewardContainer.addView(rewardValueText);

        final Space groupedRewardSeparatorLeft = new Space(appCompatActivity);
        groupedRewardSeparatorLeft.setLayoutParams(
                new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, appCompatActivity.getResources().getDisplayMetrics()),
                        LinearLayout.LayoutParams.MATCH_PARENT));

        final TextView groupedRewardIndicator = new TextView(appCompatActivity);
        groupedRewardIndicator.setLayoutParams(
                new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, appCompatActivity.getResources().getDisplayMetrics()),
                        LinearLayout.LayoutParams.MATCH_PARENT));
        groupedRewardIndicator.setBackgroundResource(R.drawable.group_indicator);

        final Space groupedRewardSeparatorRight = new Space(appCompatActivity);
        groupedRewardSeparatorRight.setLayoutParams(
                new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, appCompatActivity.getResources().getDisplayMetrics()),
                        LinearLayout.LayoutParams.MATCH_PARENT));

        groupedRewardContainer.addView(groupedRewardSeparatorLeft);
        groupedRewardContainer.addView(groupedRewardIndicator);
        groupedRewardContainer.addView(groupedRewardSeparatorRight);

        final LinearLayout groupedRewardCategoryContainer = new LinearLayout(appCompatActivity);

        groupedRewardCategoryContainer.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        groupedRewardCategoryContainer.setOrientation(LinearLayout.VERTICAL);

        for (final Reward reward : groupedRewards) {
            final TextView rewardCategoryTextView = new TextView(appCompatActivity);

            rewardCategoryTextView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            rewardCategoryTextView.setPadding(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, appCompatActivity.getResources().getDisplayMetrics()),
                    0, 0, 0);
            rewardCategoryTextView.setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
            rewardCategoryTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            rewardCategoryTextView.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
            rewardCategoryTextView.setSingleLine(false);

            final String formattedInterval = getFormattedDescriptiveInterval(reward.getInterval(), false);

            final StringBuilder rewardCategoryText =
                    new StringBuilder()
                            .append(getFormattedText(query, DataUtil.getCategory(reward.getCategoryId()).getName()))
                            .append(StringUtils.isBlank(formattedInterval) ? "" : "<br>" + formattedInterval);

            rewardCategoryTextView.setText(Html.fromHtml(rewardCategoryText.toString(), Html.FROM_HTML_MODE_COMPACT));
            rewardCategoryTextView.setEnabled(!reward.getDisabled());

            groupedRewardCategoryContainer.addView(rewardCategoryTextView);
        }

        groupedRewardContainer.addView(groupedRewardCategoryContainer);
    }

    public static String getFormattedText(
            final String query,
            final String text) {
        if (StringUtils.isNotBlank(query)
                && text.toLowerCase().contains(query)) {
            final StringBuilder formattedCategoryName = new StringBuilder();

            final int startIndex = text.toLowerCase().indexOf(query);
            final int endIndex = startIndex + query.length();

            final String start = text.substring(0, startIndex);
            final String middle = text.substring(startIndex, endIndex);
            final String end = text.substring(endIndex);

            return formattedCategoryName
                    .append(start)
                    .append("<b><u>")
                    .append(middle)
                    .append("</u></b>")
                    .append(end)
                    .toString();
        } else {
            return text;
        }
    }

    public static String getFormattedDescriptiveInterval(
            final Interval interval,
            final boolean addBraces) {
        String validFrom = "";
        String validUntil = "";

        if (interval instanceof TimeInterval) {
            final TimeInterval timeInterval = (TimeInterval) interval;

            if (Objects.nonNull(timeInterval.getStartDate())) {
                if (Objects.nonNull(timeInterval.getEndDate())
                        && timeInterval.getStartDate().getYear() == timeInterval.getEndDate().getYear()) {
                    validFrom = "<b><i>" + timeInterval.getStartDate().format(DateTimeFormatter.ofPattern("MMM d")) + "</i></b>";
                } else {
                    validFrom = "<b><i>" + timeInterval.getStartDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")) + "</i></b>";
                }
            }

            if (Objects.nonNull(timeInterval.getEndDate())) {
                validUntil = "<b><i>" + timeInterval.getEndDate().format(DateTimeFormatter.ofPattern("MMM d, yyyy")) + "</i></b>";
            }
        }

        final StringBuilder formattedInterval =
                new StringBuilder();

        if (StringUtils.isNotBlank(validFrom) || StringUtils.isNotBlank(validUntil)) {
            if (addBraces) {
                formattedInterval.append("(");
            }

            if (StringUtils.isNotBlank(validFrom)) {
                formattedInterval.append(validFrom);
            } else {
                formattedInterval.append("-<b><span>&#8734;</span></b>");
            }

            formattedInterval.append(" - ");

            if (StringUtils.isNotBlank(validUntil)) {
                formattedInterval.append(validUntil);
            } else {
                formattedInterval.append("+<b><span>&#8734;</span></b>");
            }

            if (addBraces) {
                formattedInterval.append(")");
            }
        }

        return formattedInterval.toString();
    }

    public static void createCardDeleteDialog(
            final AppCompatActivity appCompatActivity,
            final CardsAdapter cardsAdapter,
            final int position,
            final Card card) {
        new MaterialAlertDialogBuilder(appCompatActivity, R.style.AlertDialog)
                .setIcon(R.drawable.delete)
                .setMessage("Are you sure you want to delete this card?")
                .setTitle(
                        Html.fromHtml(
                                "Delete <b>" + card.getName() + "</b>?",
                                Html.FROM_HTML_MODE_COMPACT))
                .setPositiveButton(
                        R.string.delete,
                        (dialog, id) -> cardsAdapter.delete(position))
                .setNegativeButton(
                        R.string.cancel,
                        (dialog, id) -> {})
                .show();
    }

    public static void createCardAnnualFeesDialog(
            final AppCompatActivity appCompatActivity,
            final Card card) {
        new MaterialAlertDialogBuilder(appCompatActivity, R.style.AlertDialog)
                .setIcon(R.drawable.info)
                .setMessage(
                        Html.fromHtml(
                                "This card has an annual fees of <b>$" + card.getAnnualFees() + "</b>.",
                                Html.FROM_HTML_MODE_COMPACT))
                .setTitle(
                        Html.fromHtml(
                                "<b>" + card.getName() + "</b>",
                                Html.FROM_HTML_MODE_COMPACT))
                .setNeutralButton(
                        R.string.ok,
                        (dialog, id) -> {})
                .show();
    }

    public static void createCardMultiplierDialog(
            final AppCompatActivity appCompatActivity,
            final Card card) {
        new MaterialAlertDialogBuilder(appCompatActivity, R.style.AlertDialog)
                .setIcon(R.drawable.info)
                .setMessage(
                        Html.fromHtml(
                                "A multiplier of <span>&#x00D7</span> <b>" + card.getMultiplier() + "</b> is applied to rewards earned by this card.",
                                Html.FROM_HTML_MODE_COMPACT))
                .setTitle(
                        Html.fromHtml(
                                "<b>" + card.getName() + "</b>",
                                Html.FROM_HTML_MODE_COMPACT))
                .setNeutralButton(
                        R.string.ok,
                        (dialog, id) -> {})
                .show();
    }
}
