package com.ankitsingh.bestcard.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.CategoriesAdapter;
import com.ankitsingh.bestcard.adapters.holders.CategoryStateHolder;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Category;
import com.ankitsingh.bestcard.util.CardUtil;
import com.ankitsingh.bestcard.util.DataUtil;
import com.ankitsingh.bestcard.util.NightModeUtil;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

public class CategoriesFragment extends Fragment {

    @Getter
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView listTopCategories;
    private TextView hiddenTitle;

    private BottomAppBar bottomAppBar;
    private FloatingActionButton bottomAppBarAction;
    private TextView bottomAppBarTitle;

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_categories, container, false);

        this.listTopCategories = view.findViewById(R.id.list_top_categories);
        this.hiddenTitle = view.findViewById(R.id.hidden_title);

        this.hiddenTitle.setText(
                Html.fromHtml(
                        "No cards added yet. Use <b><span>&#43;</span></b> to add cards here from available cards.",
                        Html.FROM_HTML_MODE_COMPACT));

        this.categoriesAdapter =
                new CategoriesAdapter(
                        this.listTopCategories,
                        getActivity(),
                        getCategoryStateHolders());

        this.listTopCategories = view.findViewById(R.id.list_top_categories);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        this.listTopCategories.setAdapter(this.categoriesAdapter);
        this.listTopCategories.setLayoutManager(linearLayoutManager);

        setupTitleAndList();

        setupBottomAppBar();

        return view;
    }

    private BestCardActivity getBestCardActivity() {
        return (BestCardActivity) getActivity();
    }

    private void setupTitleAndList() {
        if (this.categoriesAdapter.getCurrentCategoryStateHolders().isEmpty()) {
            this.hiddenTitle.setVisibility(View.VISIBLE);
            this.listTopCategories.setVisibility(View.INVISIBLE);
        } else {
            this.hiddenTitle.setVisibility(View.INVISIBLE);
            this.listTopCategories.setVisibility(View.VISIBLE);
        }
    }

    private void setupBottomAppBar() {
        this.bottomAppBarAction = getBestCardActivity().getBottomAppBarAction();
        this.bottomAppBarTitle = getBestCardActivity().getBottomAppBarTitle();
        this.bottomAppBar = getBestCardActivity().getBottomAppBar();

        this.bottomAppBarAction.setImageResource(R.drawable.add);

        this.bottomAppBarAction.setBackgroundTintList(
                ContextCompat.getColorStateList(
                        getActivity().getApplicationContext(),
                        NightModeUtil.getColorPrimary(
                                NightModeUtil.getIntendedThemeMode(
                                        getActivity().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK,
                                        AppCompatDelegate.getDefaultNightMode()))));

        this.bottomAppBarAction.setOnClickListener(getBestCardActivity().getOnAddClickListener());

        this.bottomAppBarTitle.setVisibility(View.GONE);

        this.bottomAppBar.replaceMenu(R.menu.bottom_options);
        this.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        this.bottomAppBar.setNavigationIcon(R.drawable.menu);
        this.bottomAppBar.setNavigationOnClickListener(getBestCardActivity().getOnNavigationClickListener());
        this.bottomAppBar.setOnMenuItemClickListener(getBestCardActivity().getOnBottomAppBarMenuItemClickListener());
    }

    private List<CategoryStateHolder> getCategoryStateHolders() {
        final List<CategoryStateHolder> categoryStateHolders = new ArrayList<>();

        final Map<String, Map<String, Double>> categoryIdToCardIdsMap = new HashMap<>();
        final Map<String, Card> savedCardMap = getBestCardActivity().getDataLoader().getSavedCardsMap();
        final Map<String, Category> savedCategoryMap = getBestCardActivity().getDataLoader().getSavedCategoriesMap();

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

        categoryIdToCardIdsMap.forEach((categoryId, cardIdToRewardValueMap) -> {
            final CategoryStateHolder.CategoryStateHolderBuilder categoryStateHolderBuilder = CategoryStateHolder.builder();

            if (savedCategoryMap.containsKey(categoryId)) {
                categoryStateHolderBuilder.category(savedCategoryMap.get(categoryId).clone());
            } else {
                categoryStateHolderBuilder.category(DataUtil.getCategory(categoryId));
            }

            categoryStateHolders.add(
                    categoryStateHolderBuilder
                            .cardIdToRewardValueMap(cardIdToRewardValueMap).build());
        });

        return categoryStateHolders;
    }
}
