package com.ankitsingh.bestcard.util;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.CategoriesAdapter;
import com.ankitsingh.bestcard.adapters.holders.CategoryStateHolder;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Category;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CategoryViewUtil {

    public static void createCategoryTags(
            final AppCompatActivity appCompatActivity,
            final CategoriesAdapter categoriesAdapter,
            final int position,
            final LinearLayout categoryTags,
            final CategoryStateHolder categoryStateHolder) {
        categoryTags.removeAllViews();

        if (categoryStateHolder.getCategory().getTags().isEmpty()) {
            return;
        }

        final Space categoryTagsPaddingTop = new Space(appCompatActivity);
        categoryTagsPaddingTop.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, appCompatActivity.getResources().getDisplayMetrics())));

        categoryTags.addView(categoryTagsPaddingTop);

        final ChipGroup tagsGroup = new ChipGroup(appCompatActivity);
        final LinearLayout.LayoutParams tagsGroupLayoutParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        tagsGroupLayoutParams.gravity = Gravity.CENTER;
        tagsGroup.setLayoutParams(tagsGroupLayoutParams);
        tagsGroup.setChipSpacing(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, appCompatActivity.getResources().getDisplayMetrics()));

        final List<String> tags = categoryStateHolder.getCategory().getTags().stream().collect(Collectors.toList());

        for (int i = 0; i < tags.size(); ++i) {
            final Chip tagChip =
                    new Chip(
                            appCompatActivity,
                            null,
                            R.style.Widget_MaterialComponents_Chip_Entry);
            tagChip.setChipBackgroundColorResource(R.color.colorPrimary);
            tagChip.setClickable(true);
            tagChip.setCloseIconVisible(true);
            tagChip.setCloseIconResource(R.drawable.cancel);
            tagChip.setCloseIconTintResource(R.color.white);
            tagChip.setOnCloseIconClickListener(
                    v -> createCategoryDeleteTagDialog(
                            appCompatActivity,
                            categoriesAdapter,
                            position,
                            categoryStateHolder.getCategory(),
                            ((Chip) v).getText().toString())
            );
            tagChip.setTextColor(Color.WHITE);
            tagChip.setText(tags.get(i));

            tagsGroup.addView(
                    tagChip,
                    i,
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
        }

        categoryTags.addView(tagsGroup);

        final Space categoryTagsPaddingBottom = new Space(appCompatActivity);
        categoryTagsPaddingBottom.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, appCompatActivity.getResources().getDisplayMetrics())));

        categoryTags.addView(categoryTagsPaddingBottom);
    }

    public static void createAddTagDialog(
            final AppCompatActivity appCompatActivity,
            final CategoriesAdapter categoriesAdapter,
            final int position,
            final Category category) {
        final View view = appCompatActivity.getLayoutInflater().inflate(R.layout.form_category, null);

        final AlertDialog alertDialog =
                new MaterialAlertDialogBuilder(appCompatActivity, R.style.AlertDialog)
                        .setIcon(R.drawable.add)
                        .setTitle(
                                Html.fromHtml(
                                        "Add tag to <b>" + category.getName() + "</b>?",
                                        Html.FROM_HTML_MODE_COMPACT))
                        .setView(view)
                        .setPositiveButton(
                                R.string.add,
                                (dialog, id) -> {
                                    final TextInputEditText addTag = ((AlertDialog) dialog).findViewById(R.id.category_form_tag);

                                    categoriesAdapter.addTag(
                                            position,
                                            addTag.getText().toString());
                                })
                        .setNegativeButton(
                                R.string.cancel,
                                (dialog, id) -> {})
                        .create();

        final Map<Integer, Button> dialogButtonMap = new HashMap<>();
        alertDialog.setOnShowListener(dialog -> {
            dialogButtonMap.put(
                    AlertDialog.BUTTON_POSITIVE,
                    ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE));

            dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        });

        final TextInputEditText cardMultiplierTextInput = view.findViewById(R.id.category_form_tag);

        cardMultiplierTextInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(
                    final CharSequence s,
                    final int start,
                    final int count,
                    final int after) {}

            @Override
            public void onTextChanged(
                    final CharSequence s,
                    final int start,
                    final int before,
                    final int count) {}

            @Override
            public void afterTextChanged(
                    final Editable s) {
                if (StringUtils.isNotBlank(s.toString())
                        && CategoryUtil.isEqualTag(s.toString(), category.getTags())) {
                    cardMultiplierTextInput.setError("Existing tag");

                    dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else if (StringUtils.isBlank(s.toString())) {
                    cardMultiplierTextInput.setError("Invalid tag");

                    dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                } else {
                    dialogButtonMap.get(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                }
            }
        });

        alertDialog.show();
    }

    public static void createCategoryDeleteTagDialog(
            final AppCompatActivity appCompatActivity,
            final CategoriesAdapter categoriesAdapter,
            final int position,
            final Category category,
            final String tag) {
        new MaterialAlertDialogBuilder(appCompatActivity, R.style.AlertDialog)
                .setIcon(R.drawable.delete)
                .setMessage(
                        Html.fromHtml(
                                "Are you sure you want to remove this tag from <b>" + category.getName() + "</b>?",
                                Html.FROM_HTML_MODE_COMPACT))
                .setTitle(
                        Html.fromHtml(
                                "Remove tag <b>" + tag + "</b>?",
                                Html.FROM_HTML_MODE_COMPACT))
                .setPositiveButton(
                        R.string.delete,
                        (dialog, id) -> {
                            categoriesAdapter.deleteTag(
                                    position,
                                    tag);
                        })
                .setNegativeButton(
                        R.string.cancel,
                        (dialog, id) -> {})
                .show();
    }

    public static void createCategoryCards(
            final BestCardActivity bestCardActivity,
            final LinearLayout categoryCards,
            final CategoryStateHolder categoryStateHolder) {
        categoryCards.removeAllViews();

        if (categoryStateHolder.getCardIdToRewardValueMap().isEmpty()) {
            return;
        }

        final Space categoryCardsPaddingTop = new Space(bestCardActivity);
        categoryCardsPaddingTop.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, bestCardActivity.getResources().getDisplayMetrics())));

        categoryCards.addView(categoryCardsPaddingTop);

        final List<Pair<Card, Double>> cardsAndRewardValueList = new ArrayList<>();

        categoryStateHolder.getCardIdToRewardValueMap().forEach((cardId, rewardValue) -> {
            cardsAndRewardValueList.add(
                    Pair.of(
                            bestCardActivity.getDataLoader().getSavedCardsMap().get(cardId),
                            rewardValue));
        });

        Collections.sort(cardsAndRewardValueList, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        final LinearLayout categoryCardsContainer = new LinearLayout(bestCardActivity);

        categoryCardsContainer.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        categoryCardsContainer.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < cardsAndRewardValueList.size(); ++i) {
            final LinearLayout cardContainer = new LinearLayout(bestCardActivity);

            cardContainer.setLayoutParams(
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            cardContainer.setOrientation(LinearLayout.HORIZONTAL);
            cardContainer.setPadding(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics()),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics()));

            createCategoryCard(
                    bestCardActivity,
                    cardContainer,
                    cardsAndRewardValueList.get(i).getKey(),
                    cardsAndRewardValueList.get(i).getValue());

            categoryCardsContainer.addView(cardContainer);
        }

        categoryCards.addView(categoryCardsContainer);

        final Space categoryCardsPaddingBottom = new Space(bestCardActivity);
        categoryCardsPaddingBottom.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, bestCardActivity.getResources().getDisplayMetrics())));

        categoryCards.addView(categoryCardsPaddingBottom);
    }

    private static void createCategoryCard(
            final AppCompatActivity appCompatActivity,
            final LinearLayout cardContainer,
            final Card card,
            final Double rewardValue) {
        final MaterialCardView cardViewContainer = new MaterialCardView(appCompatActivity);

        final LinearLayout.LayoutParams cardViewContainerLayoutParams =
                new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 82, appCompatActivity.getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, appCompatActivity.getResources().getDisplayMetrics()));
        cardViewContainerLayoutParams.setMargins(
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, appCompatActivity.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, appCompatActivity.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, appCompatActivity.getResources().getDisplayMetrics()),
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, appCompatActivity.getResources().getDisplayMetrics()));

        cardViewContainer.setLayoutParams(cardViewContainerLayoutParams);
        cardViewContainer.setRadius(
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, appCompatActivity.getResources().getDisplayMetrics()));

        final ImageView cardIconView = new ImageView(appCompatActivity);

        cardIconView.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT));

        cardIconView.setScaleType(ImageView.ScaleType.FIT_XY);
        cardIconView.setImageResource(
                appCompatActivity
                        .getResources()
                        .getIdentifier(
                                card.getIconName(),
                                "drawable",
                                "com.ankitsingh.bestcard"));

        cardViewContainer.addView(cardIconView);

        cardContainer.addView(cardViewContainer);

        final Space cardSpace = new Space(appCompatActivity);

        cardSpace.setLayoutParams(
                new LinearLayout.LayoutParams(
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, appCompatActivity.getResources().getDisplayMetrics()),
                        (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, appCompatActivity.getResources().getDisplayMetrics())));

        cardContainer.addView(cardSpace);

        final LinearLayout cardDetailsContainer = new LinearLayout(appCompatActivity);

        cardDetailsContainer.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        cardDetailsContainer.setOrientation(LinearLayout.VERTICAL);

        final TextView cardNameText = new TextView(appCompatActivity);

        cardNameText.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        cardNameText.setPadding(
                0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, appCompatActivity.getResources().getDisplayMetrics()),
                0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, appCompatActivity.getResources().getDisplayMetrics()));
        cardNameText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

        cardNameText.setText(card.getName());

        final TextView cardNameValueText = new TextView(appCompatActivity);

        cardNameValueText.setLayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        cardNameValueText.setPadding(
                0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, appCompatActivity.getResources().getDisplayMetrics()),
                0,
                (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, appCompatActivity.getResources().getDisplayMetrics()));
        cardNameValueText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        cardNameValueText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

        cardNameValueText.setText(
                new StringBuilder()
                        .append(new DecimalFormat("0.##").format(rewardValue))
                        .append(" %")
                        .toString());

        cardDetailsContainer.addView(cardNameText);
        cardDetailsContainer.addView(cardNameValueText);

        cardContainer.addView(cardDetailsContainer);
    }
}
