package com.ankitsingh.bestcard.util;

import android.app.AlertDialog;
import android.graphics.Typeface;
import android.text.Html;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.CardAdapter;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.model.interval.TimeInterval;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CardViewUtil {

    public static int createCardRewards(
            final BestCardActivity bestCardActivity,
            final LinearLayout cardRewards,
            final CardAdapter.CardStateHolder cardStateHolder) {
        int height = 0;

        cardRewards.removeAllViews();

        final Space cardRewardsPaddingTop = new Space(bestCardActivity);
        cardRewardsPaddingTop.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics())));
        height += (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics());

        cardRewards.addView(cardRewardsPaddingTop);

        final List<Pair<Double, List<Reward>>> groupedCardRewards =
                groupedCardRewards(cardStateHolder);

        for (int i = 0; i < groupedCardRewards.size(); ++i) {
            final Double rewardValue = groupedCardRewards.get(i).getKey();
            final List<Reward> groupedRewards = groupedCardRewards.get(i).getValue();

            final LinearLayout groupedRewardContainer = new LinearLayout(bestCardActivity);

            groupedRewardContainer.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            groupedRewardContainer.setOrientation(LinearLayout.HORIZONTAL);

            createCardRewardCategories(bestCardActivity, groupedRewardContainer, cardStateHolder.getQuery(), rewardValue, groupedRewards);

            groupedRewardContainer.measure(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            height +=groupedRewardContainer.getMeasuredHeight();

            cardRewards.addView(groupedRewardContainer);

            if (i != groupedCardRewards.size() - 1) {
                final Space groupedRewardContainerBottomSpace = new Space(bestCardActivity);
                groupedRewardContainerBottomSpace.setLayoutParams(
                        new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics())));
                height += (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics());

                cardRewards.addView(groupedRewardContainerBottomSpace);
            }
        }

        final Space cardRewardsPaddingBottom = new Space(bestCardActivity);
        cardRewardsPaddingBottom.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics())));
        height += (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics());

        cardRewards.addView(cardRewardsPaddingBottom);

        return height;
    }

    private static List<Pair<Double, List<Reward>>> groupedCardRewards(
            final CardAdapter.CardStateHolder cardStateHolder) {
        final List<Pair<Double, List<Reward>>> sortedGroupedCardRewards = new ArrayList<>();

        cardStateHolder.getCard().getRewards().stream()
                .collect(Collectors.groupingBy(reward -> reward.getValue()*cardStateHolder.getCard().getMultiplier()))
                .forEach((rewardValue, rewards) -> {
                    sortedGroupedCardRewards.add(
                            Pair.of(
                                    rewardValue,
                                    rewards.stream().sorted(Comparator.comparing(reward -> reward.getCategory().getName())).collect(Collectors.toList())));
                });

        Collections.sort(sortedGroupedCardRewards, (o1, o2) -> o2.getKey().compareTo(o1.getKey()));

        return sortedGroupedCardRewards;
    }

    private static void createCardRewardCategories(
            final BestCardActivity bestCardActivity,
            final LinearLayout groupedRewardContainer,
            final String query,
            final Double rewardValue,
            final List<Reward> groupedRewards) {
        final TextView rewardValueText = new TextView(bestCardActivity);

        rewardValueText.setLayoutParams(
                new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 96, bestCardActivity.getResources().getDisplayMetrics()),
                        LinearLayout.LayoutParams.MATCH_PARENT));
        rewardValueText.setPadding(
                0, 0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics()),
                0);
        rewardValueText.setGravity(Gravity.CENTER_VERTICAL|Gravity.END);
        rewardValueText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        rewardValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        rewardValueText.setText(
                new StringBuilder()
                        .append(new DecimalFormat("0.##").format(rewardValue))
                        .append(" %")
                        .toString());

        groupedRewardContainer.addView(rewardValueText);

        final Space groupedRewardSeparatorLeft = new Space(bestCardActivity);
        groupedRewardSeparatorLeft.setLayoutParams(
                new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, bestCardActivity.getResources().getDisplayMetrics()),
                        LinearLayout.LayoutParams.MATCH_PARENT));
        groupedRewardSeparatorLeft.setBackground(bestCardActivity.getDrawable(R.drawable.group_indicator));

        final TextView groupedRewardIndicator = new TextView(bestCardActivity);
        groupedRewardIndicator.setLayoutParams(
                new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, bestCardActivity.getResources().getDisplayMetrics()),
                        LinearLayout.LayoutParams.MATCH_PARENT));
        groupedRewardIndicator.setBackground(bestCardActivity.getDrawable(R.drawable.group_indicator));
        groupedRewardIndicator.setBackgroundResource(R.drawable.border);

        final Space groupedRewardSeparatorRight = new Space(bestCardActivity);
        groupedRewardSeparatorRight.setLayoutParams(
                new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, bestCardActivity.getResources().getDisplayMetrics()),
                        LinearLayout.LayoutParams.MATCH_PARENT));
        groupedRewardSeparatorRight.setBackground(bestCardActivity.getDrawable(R.drawable.group_indicator));

        groupedRewardContainer.addView(groupedRewardSeparatorLeft);
        groupedRewardContainer.addView(groupedRewardIndicator);
        groupedRewardContainer.addView(groupedRewardSeparatorRight);

        final LinearLayout groupedRewardCategoryContainer = new LinearLayout(bestCardActivity);

        groupedRewardCategoryContainer.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));
        groupedRewardCategoryContainer.setOrientation(LinearLayout.VERTICAL);

        for (final Reward reward : groupedRewards) {
            String validFrom = "";
            String validUntil = "";

            if (reward.getInterval() instanceof TimeInterval) {
                final TimeInterval timeInterval = (TimeInterval) reward.getInterval();

                if (timeInterval.getStartDate() != null) {
                    validFrom = "<b>" + timeInterval.getStartDate().format(DateTimeFormatter.ofPattern("MMM")) + "</b>";
                }

                if (timeInterval.getEndDate() != null) {
                    validUntil = "<b>" + timeInterval.getEndDate().format(DateTimeFormatter.ofPattern("MMM")) + "</b>";
                }
            }


            final TextView rewardCategoryTextView = new TextView(bestCardActivity);

            rewardCategoryTextView.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            rewardCategoryTextView.setPadding(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics()),
                    0, 0, 0);
            rewardCategoryTextView.setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
            rewardCategoryTextView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
            rewardCategoryTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

            final StringBuilder rewardCategoryText =
                    new StringBuilder()
                            .append(getFormattedCategoryName(query, reward.getCategory().getName()))
                            .append(getFormattedInterval(validFrom, validUntil));

            rewardCategoryTextView.setText(Html.fromHtml(rewardCategoryText.toString(), Html.FROM_HTML_MODE_COMPACT));

            groupedRewardCategoryContainer.addView(rewardCategoryTextView);

        }

        groupedRewardContainer.addView(groupedRewardCategoryContainer);
    }

    private static String getFormattedCategoryName(
            final String query,
            final String categoryName) {
        if (StringUtils.isNotBlank(query)) {
            final StringBuilder formattedCategoryName = new StringBuilder();

            final int startIndex = categoryName.toLowerCase().indexOf(query);
            final int endIndex = startIndex + query.length();

            final String start = categoryName.substring(0, startIndex);
            final String middle = categoryName.substring(startIndex, endIndex);
            final String end = categoryName.substring(endIndex);

            return formattedCategoryName
                    .append(start)
                    .append("<b><u>")
                    .append(middle)
                    .append("</u></b>")
                    .append(end)
                    .toString();
        } else {
            return categoryName;
        }
    }

    private static String getFormattedInterval(
            final String validFrom,
            final String validUntil) {
        final StringBuilder formattedInterval =
                new StringBuilder();

        if (StringUtils.isNotBlank(validFrom) || StringUtils.isNotBlank(validUntil)) {
            formattedInterval.append(" (");

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

            formattedInterval.append(")");
        }

        return formattedInterval.toString();
    }

    public static AlertDialog createDeleteDialog(
            final BestCardActivity bestCardActivity,
            final CardAdapter cardAdapter,
            final int position) {
        final AlertDialog.Builder builder =
                new AlertDialog.Builder(bestCardActivity)
                        .setTitle(R.string.delete_card)
                        .setPositiveButton(
                                R.string.delete,
                                (dialog, id) -> cardAdapter.delete(position))
                        .setNegativeButton(
                                R.string.cancel,
                                (dialog, id) -> {});

        return builder.create();
    }
}
