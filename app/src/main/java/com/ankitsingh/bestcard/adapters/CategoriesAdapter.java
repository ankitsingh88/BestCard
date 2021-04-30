package com.ankitsingh.bestcard.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.holders.CategoryStateHolder;
import com.ankitsingh.bestcard.adapters.holders.CategoryViewHolder;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Category;
import com.ankitsingh.bestcard.util.CardUtil;
import com.ankitsingh.bestcard.util.CategoryViewUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

public class CategoriesAdapter extends ListAdapter<CategoryStateHolder, CategoryViewHolder> {

    private final View.OnClickListener onCategoryRowClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = recyclerView.getChildAdapterPosition(v);

                    final CategoryStateHolder currentCategoryStateHolder = getItem(position);
                    final boolean isExpanded = currentCategoryStateHolder.isExpanded();

                    if (isExpanded) {
                        currentCategoryStateHolder.setExpanded(false);

                        notifyItemChanged(position);

                        recyclerView.scrollToPosition(position);
                    } else {
                        for (int i = 0; i < getCurrentList().size(); ++i) {
                            final CategoryStateHolder categoryStateHolder = getCurrentList().get(i);

                            if (position != i) {

                                if (categoryStateHolder.isExpanded()) {
                                    categoryStateHolder.setExpanded(false);

                                    notifyItemChanged(i);
                                }
                            } else {
                                categoryStateHolder.setExpanded(true);

                                notifyItemChanged(position);

                                recyclerView.scrollToPosition(position);
                            }
                        }
                    }
                }
            };

    private final View.OnClickListener onExpandCollapseClickListener =
            v -> ((View) v.getParent().getParent()).callOnClick();

    private final View.OnClickListener onAddTagClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final int position = recyclerView.getChildAdapterPosition((View) v.getParent().getParent().getParent().getParent());

                    CategoryViewUtil
                            .createAddTagDialog(
                                    bestCardActivity,
                                    CategoriesAdapter.this,
                                    position,
                                    getItem(position).getCategory());
                }
            };

    private final RecyclerView recyclerView;
    private final BestCardActivity bestCardActivity;

    private final Map<String, CategoryStateHolder> masterCategoryStateHolderMap = new HashMap<>();
    @Getter private final List<CategoryStateHolder> currentCategoryStateHolders = new ArrayList<>();

    public static final DiffUtil.ItemCallback<CategoryStateHolder> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<CategoryStateHolder>() {
                @Override
                public boolean areItemsTheSame(
                        final CategoryStateHolder oldItem,
                        final CategoryStateHolder newItem) {
                    return oldItem.getCategory().getId().equals(newItem.getCategory().getId());
                }
                @Override
                public boolean areContentsTheSame(
                        final CategoryStateHolder oldItem,
                        final CategoryStateHolder newItem) {
                    return oldItem.getCategory().getId().equals(newItem.getCategory().getId());
                }
            };

    public CategoriesAdapter(
            final RecyclerView recyclerView,
            final Context context,
            final List<CategoryStateHolder> categoryStateHolders) {
        super(DIFF_CALLBACK);

        this.recyclerView = recyclerView;
        this.bestCardActivity = (BestCardActivity) context;

        categoryStateHolders.forEach(categoryStateHolder -> {
            this.masterCategoryStateHolderMap.put(categoryStateHolder.getCategory().getId(), categoryStateHolder);
        });

        changeDataSet(this.masterCategoryStateHolderMap.values());

        notifyDataSetChanged();
    }

    private void changeDataSet(
            final Collection<CategoryStateHolder> masterCategoryStateHolders) {
        final Map<String, Long> searchedSuggestionsMap = this.bestCardActivity.getDataLoader().getSavedCategoryRankMap();
        final List<CategoryStateHolder> sortedCategoryStateHolder = new ArrayList<>(masterCategoryStateHolders);

        Collections.sort(
                sortedCategoryStateHolder,
                (o1, o2) -> {
                    return searchedSuggestionsMap.getOrDefault(o2.getCategory().getId(), 0L)
                            .compareTo(searchedSuggestionsMap.getOrDefault(o1.getCategory().getId(), 0L));
                });

        this.currentCategoryStateHolders.clear();
        this.currentCategoryStateHolders.addAll(sortedCategoryStateHolder);

        submitList(this.currentCategoryStateHolders);
    }

    public void addCategories(
            final Set<String> categoryIds) {
        getCategoryStateHolders(categoryIds).forEach(this.masterCategoryStateHolderMap::put);

        changeDataSet(this.masterCategoryStateHolderMap.values());
    }

    private Map<String, CategoryStateHolder> getCategoryStateHolders(
            final Set<String> categoryIds) {
        final Map<String, Map<String, Double>> categoryIdToCardIdsMap = new HashMap<>();
        final Map<String, Card> savedCardMap = this.bestCardActivity.getDataLoader().getSavedCardsMap();
        final Map<String, Category> savedCategoryMap = this.bestCardActivity.getDataLoader().getSavedCategoriesMap();

        savedCardMap.forEach((cardId, savedCard) -> {
            savedCard.getRewards().forEach(reward -> {
                if (CardUtil.isRewardActive(
                        savedCategoryMap,
                        null,
                        reward)) {
                    final Map<String, Double> cardIdToRewardValueMap =
                            categoryIdToCardIdsMap.getOrDefault(reward.getCategoryId(), new HashMap<>());

                    cardIdToRewardValueMap.put(
                            cardId,
                            savedCard.getMultiplier()*reward.getValue());

                    categoryIdToCardIdsMap.put(reward.getCategoryId(), cardIdToRewardValueMap);
                }
            });
        });

        final Map<String, CategoryStateHolder> categoryStateHolderMap = new HashMap<>();

        categoryIds.forEach(categoryId -> {
            categoryStateHolderMap.put(
                    categoryId,
                    CategoryStateHolder.builder()
                            .category(savedCategoryMap.get(categoryId).clone())
                            .cardIdToRewardValueMap(categoryIdToCardIdsMap.get(categoryId))
                            .build());
        });

        return categoryStateHolderMap;
    }

    public void addTag(
            final int position,
            final String tag) {
        final Category currentCategory = this.masterCategoryStateHolderMap.get(getItem(position).getCategory().getId()).getCategory();

        final Set<String> tags = new HashSet<>(currentCategory.getTags());

        tags.add(tag);

        currentCategory.setTags(tags);

        changeDataSet(this.masterCategoryStateHolderMap.values());

        saveCategory(currentCategory);

        notifyItemChanged(position);
    }

    public void deleteTag(
            final int position,
            final String tag) {
        final Category currentCategory = this.masterCategoryStateHolderMap.get(getItem(position).getCategory().getId()).getCategory();

        final Set<String> tags = new HashSet<>(currentCategory.getTags());

        tags.remove(tag);

        currentCategory.setTags(tags);

        changeDataSet(this.masterCategoryStateHolderMap.values());

        saveCategory(currentCategory);

        notifyItemChanged(position);
    }

    private void saveCategory(
            final Category category) {
        bestCardActivity.getDataLoader().saveCategory(category);
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(
            @NonNull final ViewGroup parent,
            int viewType) {
        final View categoryView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category, parent, false);

        return new CategoryViewHolder(categoryView);
    }

    @Override
    public void onBindViewHolder(
            @NonNull final CategoryViewHolder categoryViewHolder,
            final int position) {
        final CategoryStateHolder categoryStateHolder = getItem(position);
        final Category category = categoryStateHolder.getCategory();

        categoryViewHolder
                .getCategoryRow()
                .setOnClickListener(this.onCategoryRowClickListener);

        categoryViewHolder
                .getCategoryIcon()
                .setImageResource(
                        this.bestCardActivity
                                .getResources()
                                .getIdentifier(
                                        category.getIconName(),
                                        "drawable",
                                        "com.ankitsingh.bestcard"));

        categoryViewHolder
                .getCategoryName()
                .setText(category.getName());

        categoryViewHolder
                .getCategoryExpandCollapse()
                .setOnClickListener(this.onExpandCollapseClickListener);

        CategoryViewUtil.createCategoryCards(
                bestCardActivity,
                categoryViewHolder.getCategoryCards(),
                categoryStateHolder);

        CategoryViewUtil.createCategoryTags(
                bestCardActivity,
                this,
                position,
                categoryViewHolder.getCategoryTags(),
                categoryStateHolder);

        categoryViewHolder
                .getCategoryAddTag()
                .setOnClickListener(this.onAddTagClickListener);

        categoryViewHolder
                .getCategoryExpanded()
                .setVisibility(categoryStateHolder.isExpanded() ? View.VISIBLE : View.GONE);

        categoryViewHolder
                .getCategoryExpandCollapse()
                .setImageResource(categoryStateHolder.isExpanded() ? R.drawable.collapse : R.drawable.expand);

        if (categoryStateHolder.isExpanded()) {
            categoryViewHolder
                    .getCategoryRow()
                    .setCardElevation(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics()));

            final TypedValue colorBackground = new TypedValue();
            this.bestCardActivity.getTheme().resolveAttribute(android.R.attr.windowBackground, colorBackground, true);
            categoryViewHolder
                    .getCategoryRow()
                    .setCardBackgroundColor(colorBackground.data);

            categoryViewHolder
                    .getCategoryIconContainer()
                    .setVisibility(View.GONE);

            categoryViewHolder
                    .getCategoryDetailsVerticalSpace()
                    .setVisibility(View.GONE);
        } else {
            categoryViewHolder
                    .getCategoryRow()
                    .setCardElevation(0);

            categoryViewHolder
                    .getCategoryRow()
                    .setCardBackgroundColor(Color.TRANSPARENT);

            categoryViewHolder
                    .getCategoryIconContainer()
                    .setVisibility(View.VISIBLE);

            categoryViewHolder
                    .getCategoryDetailsVerticalSpace()
                    .setVisibility(View.VISIBLE);
        }
    }
}
