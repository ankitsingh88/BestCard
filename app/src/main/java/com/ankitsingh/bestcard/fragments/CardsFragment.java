package com.ankitsingh.bestcard.fragments;

import android.app.SearchManager;
import android.content.res.Configuration;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.CardsAdapter;
import com.ankitsingh.bestcard.adapters.HtmlCursorAdapter;
import com.ankitsingh.bestcard.adapters.holders.CardStateHolder;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.model.Category;
import com.ankitsingh.bestcard.model.Reward;
import com.ankitsingh.bestcard.model.interval.TimeInterval;
import com.ankitsingh.bestcard.util.CardUtil;
import com.ankitsingh.bestcard.util.CardViewUtil;
import com.ankitsingh.bestcard.util.DataUtil;
import com.ankitsingh.bestcard.util.NightModeUtil;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class CardsFragment extends Fragment {

    private CardsAdapter cardsAdapter;
    private RecyclerView listCards;
    private TextView title;
    private TextView hiddenTitle;

    private String[] suggestionColumns =
            {
                    BaseColumns._ID,
                    SearchManager.SUGGEST_COLUMN_TEXT_1
            };
    private List<String> currentSuggestions = new ArrayList<>();
    private HtmlCursorAdapter htmlCursorAdapter;
    private ImageView searchCloseButton;
    private SearchView searchBar;

    private BottomAppBar bottomAppBar;
    private FloatingActionButton bottomAppBarAction;
    private TextView bottomAppBarTitle;

    private final View.OnClickListener onSearchCloseButtonClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v) {
                    searchBar.setQuery("", false);
                    searchBar.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.VISIBLE);

                    cardsAdapter.filter("");

                    getBestCardActivity().setCardsFragment();
                }
            };

    private final SearchView.OnSuggestionListener onSuggestionListener =
            new SearchView.OnSuggestionListener() {
                @Override
                public boolean onSuggestionSelect(
                        final int position) {
                    return false;
                }

                @Override
                public boolean onSuggestionClick(
                        final int position) {
                    final String query = currentSuggestions.get(position);

                    searchBar.setQuery(query, true);
                    searchBar.clearFocus();

                    getBestCardActivity().getDataLoader().saveCategoryRankMap(getSavedCategoryRankMapToSave(query));

                    cardsAdapter.filter(query);

                    return true;
                }
            };

    private final SearchView.OnQueryTextListener onQueryTextListener =
            new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(
                        final String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(
                        final String newText) {
                    populateSuggestions(newText);

                    return true;
                }
            };

    private final SearchView.OnCloseListener onSearchCloseListener =
            () -> {
                if (getView() != null) {
                    setupSearchMode(false);

                    return true;
                }
                return false;
            };

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cards, container, false);

        this.listCards = view.findViewById(R.id.list_cards);
        this.title = view.findViewById(R.id.title);
        this.hiddenTitle = view.findViewById(R.id.hidden_title);

        this.hiddenTitle.setText(
                Html.fromHtml(
                        "No cards added yet. Use <b><span>&#43;</span></b> to add cards here from available cards.",
                        Html.FROM_HTML_MODE_COMPACT));

        this.cardsAdapter =
                new CardsAdapter(
                        this.listCards,
                        getActivity(),
                        getCardStateHolders());

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        this.listCards.setAdapter(this.cardsAdapter);
        this.listCards.setLayoutManager(linearLayoutManager);

        setupTitleAndList();

        setupSearch(view);

        setupBottomAppBar();

        setupSearchMode(false);

        return view;
    }

    private List<CardStateHolder> getCardStateHolders() {
        return getBestCardActivity().getDataLoader().getSavedCardsMap().values().stream()
                .map(savedCard ->
                        CardStateHolder.builder()
                                .card(savedCard.clone())
                                .build())
                .collect(Collectors.toList());
    }

    public void setupTitleAndList() {
        if (this.cardsAdapter.getCurrentCardStateHolders().isEmpty()) {
            this.hiddenTitle.setVisibility(View.VISIBLE);
            this.listCards.setVisibility(View.INVISIBLE);
        } else {
            this.hiddenTitle.setVisibility(View.INVISIBLE);
            this.listCards.setVisibility(View.VISIBLE);
        }
    }

    public void addCards(
            final Set<String> cardIdsToAdd) {
        this.cardsAdapter.add(cardIdsToAdd.stream().map(DataUtil::getCard).collect(Collectors.toList()));
    }

    private BestCardActivity getBestCardActivity() {
        return (BestCardActivity) getActivity();
    }

    private void setupSearch(
            final View view) {
        this.searchBar = view.findViewById(R.id.search_bar);

        this.searchCloseButton =
                this.searchBar.findViewById(
                        this.searchBar
                                .getContext()
                                .getResources()
                                .getIdentifier("android:id/search_close_btn", null, null));
        this.searchCloseButton.setOnClickListener(this.onSearchCloseButtonClickListener);

        this.htmlCursorAdapter =
                new HtmlCursorAdapter(
                        getActivity(),
                        R.layout.row_suggestion,
                        null,
                        new String[]{SearchManager.SUGGEST_COLUMN_TEXT_1},
                        new int[]{R.id.suggestion_name},
                        0);

        this.searchBar.setSuggestionsAdapter(this.htmlCursorAdapter);

        this.searchBar.setOnSuggestionListener(this.onSuggestionListener);
        this.searchBar.setOnQueryTextListener(this.onQueryTextListener);
        this.searchBar.setOnCloseListener(this.onSearchCloseListener);
    }

    public void setupSearchMode(
            final boolean searchMode) {
        if (searchMode) {
            this.title.setVisibility(View.INVISIBLE);

            this.searchBar.setQuery("", false);
            this.searchBar.setIconified(false);
            this.searchBar.setVisibility(View.VISIBLE);
        } else {
            this.title.setVisibility(View.VISIBLE);

            this.searchBar.setIconified(true);
            this.searchBar.setVisibility(View.INVISIBLE);
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

    private void populateSuggestions(
            final String originalQuery) {
        final String query = originalQuery == null ? null : originalQuery.toLowerCase(Locale.getDefault());

        final MatrixCursor cursor = new MatrixCursor(suggestionColumns);

        final List<String> filteredSuggestions =
                getAllSuggestions().stream()
                        .filter(suggestion -> suggestion.toLowerCase().contains(query))
                        .collect(Collectors.toList());

        this.currentSuggestions.clear();
        this.currentSuggestions.addAll(filteredSuggestions);

        for (int i = 0; i < this.currentSuggestions.size(); ++i) {
            final String[] suggestionRow =
                    {
                            Integer.toString(i),
                            CardViewUtil.getFormattedText(query, this.currentSuggestions.get(i))
                    };

            cursor.addRow(suggestionRow);
        }

        htmlCursorAdapter.swapCursor(cursor);
    }

    private List<String> getAllSuggestions() {
        final Set<String> suggestions = new HashSet<>();

        getBestCardActivity().getDataLoader().getSavedCardsMap().forEach((id, savedCard) -> {
            final List<Reward> rewards =
                    savedCard.getRewards().stream()
                            .filter(reward -> !reward.getDisabled())
                            .filter(reward -> {
                                if (reward.getInterval() instanceof TimeInterval) {
                                    final LocalDate startDate = ((TimeInterval) reward.getInterval()).getStartDate() == null ? LocalDate.MIN : ((TimeInterval) reward.getInterval()).getStartDate();
                                    final LocalDate endDate = ((TimeInterval) reward.getInterval()).getEndDate() == null ? LocalDate.MAX : ((TimeInterval) reward.getInterval()).getEndDate();
                                    final LocalDate currentDate = LocalDate.now();

                                    return startDate.toEpochDay() <= currentDate.toEpochDay() && endDate.toEpochDay() >= currentDate.toEpochDay();
                                } else {
                                    return true;
                                }
                            })
                            .collect(Collectors.toList());

            rewards.forEach(reward -> {
                if (!getBestCardActivity().getDataLoader().getSavedCategoriesMap().containsKey(reward.getCategoryId())) {
                    getBestCardActivity().getDataLoader().saveCategory(DataUtil.getCategory(reward.getCategoryId()));
                }

                final Category category = getBestCardActivity().getDataLoader().getSavedCategoriesMap().get(reward.getCategoryId());

                suggestions.add(category.getName());
                suggestions.addAll(category.getTags());
            });
        });

        return new ArrayList<>(suggestions);
    }

    private Map<String, Long> getSavedCategoryRankMapToSave(
            final String suggestion) {
        final Map<String, Long> savedCategoryRankMap = new HashMap<>();

        final Map<String, Card> savedCardMap = getBestCardActivity().getDataLoader().getSavedCardsMap();
        final Map<String, Category> savedCategoryMap = getBestCardActivity().getDataLoader().getSavedCategoriesMap();

        savedCardMap.forEach((id, savedCard) -> {
            savedCard.getRewards().forEach(reward -> {
                if (CardUtil.isRewardActive(
                        savedCategoryMap,
                        null,
                        reward)) {
                    final Set<String> tags = new HashSet<>();
                    final String categoryName = DataUtil.getCategory(reward.getCategoryId()).getName();

                    if (savedCategoryMap.containsKey(reward.getCategoryId())) {
                        tags.addAll(savedCategoryMap.get(reward.getCategoryId()).getTags());
                    } else {
                        tags.addAll(DataUtil.getCategory(reward.getCategoryId()).getTags());
                    }

                    final String query = suggestion.toLowerCase(Locale.getDefault());

                    if (categoryName.toLowerCase(Locale.getDefault()).equals(query)
                            || tags.contains(suggestion)) {
                        if (!savedCategoryRankMap.containsKey(reward.getCategoryId())) {
                            savedCategoryRankMap.put(reward.getCategoryId(), 1L);
                        }
                    }
                }
            });
        });

        final Map<String, Long> currentSavedCategoryRankMap = getBestCardActivity().getDataLoader().getSavedCategoryRankMap();

        currentSavedCategoryRankMap.forEach((categoryId, suggestionCounter) -> {
            savedCategoryRankMap.merge(categoryId, suggestionCounter, (suggestionCounter1, suggestionCounter2) -> suggestionCounter1 + suggestionCounter2);
        });

        return savedCategoryRankMap;
    }
}
