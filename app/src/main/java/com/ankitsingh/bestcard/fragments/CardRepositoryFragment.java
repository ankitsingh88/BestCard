package com.ankitsingh.bestcard.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.CardsRepositoryAdapter;
import com.ankitsingh.bestcard.adapters.holders.CardStateHolder;
import com.ankitsingh.bestcard.model.Card;
import com.ankitsingh.bestcard.util.DataUtil;
import com.ankitsingh.bestcard.util.NightModeUtil;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class CardRepositoryFragment extends Fragment {

    private CardsRepositoryAdapter cardsRepositoryAdapter;
    private RecyclerView listCards;
    private TextView hiddenTitle;

    private TextView title;
    private SearchView searchBar;
    private RelativeLayout customCardAdd;

    private BottomAppBar bottomAppBar;
    private FloatingActionButton bottomAppBarAction;
    private TextView bottomAppBarTitle;

    private View.OnClickListener onItemClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v) {
                    if (cardsRepositoryAdapter.getSelectedCardStateHolders().isEmpty()) {
                        disableBottomAppBarAction();
                    } else {
                        enableBottomAppBarAction();
                    }
                }
            };

    private final View.OnClickListener onBackClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v) {
                    cardsRepositoryAdapter.clearSelected();

                    searchBar.setQuery("",false);

                    getBestCardActivity().setCardsFragment();
                }
            };

    private final View.OnClickListener onCloseClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v) {
                    if (cardsRepositoryAdapter.isInSelectionMode()) {
                        cardsRepositoryAdapter.clearSelected();

                        disableBottomAppBarAction();
                    }

                    searchBar.setQuery("",false);
                }
            };

    private final View.OnClickListener onAddClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v) {
                    final Set<String> cardIdsToAdd =
                            cardsRepositoryAdapter.getSelectedCardStateHolders().stream()
                                    .map(cardStateHolder -> cardStateHolder.getCard().getId())
                                    .collect(Collectors.toSet());

                    searchBar.setQuery("",false);

                    if (cardIdsToAdd.isEmpty()) {
                        Toast.makeText(getActivity(), "Nothing to add", Toast.LENGTH_SHORT).show();
                    } else {
                        getBestCardActivity().getCardsFragment().addCards(cardIdsToAdd);
                        getBestCardActivity().setCardsFragment();
                    }
                }
            };

    private final View.OnClickListener onCustomCardAddClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(
                        final View v) {
                    CardAddFormDialogFragment.display(
                            getBestCardActivity(),
                            getBestCardActivity().getCardsFragment().getCardsAdapter());
                }
            };

    private final SearchView.OnQueryTextListener onQueryTextListener =
            new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(
                        final String query) {
                    if (getView() != null) {
                        cardsRepositoryAdapter.filter(query);
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(
                        final String newText) {
                    if (getView() != null) {
                        cardsRepositoryAdapter.filter(newText);
                    }
                    return false;
                }
            };

    private final SearchView.OnCloseListener onSearchCloseListener =
            () -> {
                if (getView() != null) {
                    searchBar.setVisibility(View.INVISIBLE);
                    title.setVisibility(View.VISIBLE);

                    return true;
                }
                return false;
            };

    private final BottomAppBar.OnMenuItemClickListener onBottomAppBarMenuItemClickListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.card_repository_bottom_app_bar_search:
                        if (cardsRepositoryAdapter.getCardStateHolders().isEmpty()) {
                            Toast.makeText(getActivity(), "Nothing to search", Toast.LENGTH_SHORT).show();
                        } else {
                            title.setVisibility(View.INVISIBLE);
                            searchBar.setVisibility(View.VISIBLE);
                            searchBar.setIconified(false);
                        }
                        return true;
                }
                return false;
            };

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_card_repository, container, false);

        this.customCardAdd = view.findViewById(R.id.card_add);
        this.listCards = view.findViewById(R.id.list_cards);
        this.hiddenTitle = view.findViewById(R.id.hidden_title);

        this.cardsRepositoryAdapter =
                new CardsRepositoryAdapter(
                        this.listCards,
                        getActivity(),
                        getBestCardActivity().getDataLoader().getSavedCardsMap().values().stream().map(Card::getId).collect(Collectors.toSet()),
                        getCardStateHolders());

        this.hiddenTitle.setText(
                Html.fromHtml(
                        "No cards to add, all cards have been added.",
                        Html.FROM_HTML_MODE_COMPACT));

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        this.listCards.setAdapter(this.cardsRepositoryAdapter);
        this.listCards.setLayoutManager(linearLayoutManager);
        this.listCards.setOnClickListener(this.onItemClickListener);

        this.title = view.findViewById(R.id.title);

        this.customCardAdd.setOnClickListener(this.onCustomCardAddClickListener);

        setupTitleAndList();

        setupTitle(view);

        setupSearch(view);

        setupBottomAppBar();

        return view;
    }

    private List<CardStateHolder> getCardStateHolders() {
        return DataUtil.getCards().stream()
                .map(card ->
                        CardStateHolder.builder()
                                .card(card)
                                .build())
                .collect(Collectors.toList());
    }

    public void setupTitleAndList() {
        if (this.cardsRepositoryAdapter.getCardStateHolders().isEmpty()) {
            this.hiddenTitle.setVisibility(View.VISIBLE);
            this.listCards.setVisibility(View.INVISIBLE);
        } else {
            this.hiddenTitle.setVisibility(View.INVISIBLE);
            this.listCards.setVisibility(View.VISIBLE);
        }
    }

    private BestCardActivity getBestCardActivity() {
        return (BestCardActivity) getActivity();
    }

    private void setupTitle(
            final View view) {
        this.title = view.findViewById(R.id.title);

        this.title.setVisibility(View.VISIBLE);
    }

    private void setupSearch(
            final View view) {
        this.searchBar = view.findViewById(R.id.search_bar);

        this.searchBar.setOnQueryTextListener(this.onQueryTextListener);
        this.searchBar.setOnCloseListener(this.onSearchCloseListener);

        this.searchBar.setVisibility(View.INVISIBLE);
    }

    private void setupBottomAppBar() {
        this.bottomAppBarAction = getBestCardActivity().getBottomAppBarAction();
        this.bottomAppBarTitle = getBestCardActivity().getBottomAppBarTitle();
        this.bottomAppBar = getBestCardActivity().getBottomAppBar();

        this.bottomAppBarAction.setImageResource(R.drawable.add);

        this.bottomAppBarAction.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.colorDisabled));

        this.bottomAppBarAction.setOnClickListener(this.onAddClickListener);

        this.bottomAppBarTitle.setText(R.string.select_cards);
        this.bottomAppBarTitle.setVisibility(View.VISIBLE);

        this.bottomAppBar.replaceMenu(R.menu.card_repository_menu);
        this.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        this.bottomAppBar.setNavigationIcon(R.drawable.back);
        this.bottomAppBar.setNavigationOnClickListener(this.onBackClickListener);
        this.bottomAppBar.setOnMenuItemClickListener(this.onBottomAppBarMenuItemClickListener);
    }

    private void enableBottomAppBarAction() {
        this.bottomAppBarAction.setImageResource(
                this.cardsRepositoryAdapter.getSelectedCardStateHolders().size() > 1 ?
                        R.drawable.done_all : R.drawable.done);

        this.bottomAppBarAction.setBackgroundTintList(
                ContextCompat.getColorStateList(
                        getActivity().getApplicationContext(),
                        NightModeUtil.getColorPrimary(
                                NightModeUtil.getIntendedThemeMode(
                                        getActivity().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK,
                                        AppCompatDelegate.getDefaultNightMode()))));

        this.bottomAppBarTitle.setText(cardsRepositoryAdapter.getSelectedCardStateHolders().size() + " selected");
        this.bottomAppBarTitle.setVisibility(View.VISIBLE);

        this.bottomAppBar.setNavigationIcon(R.drawable.close);
        this.bottomAppBar.setNavigationOnClickListener(this.onCloseClickListener);
    }

    private void disableBottomAppBarAction() {
        this.bottomAppBarAction.setImageResource(R.drawable.add);

        this.bottomAppBarAction.setBackgroundTintList(
                ContextCompat.getColorStateList(getActivity().getApplicationContext(), R.color.colorDisabled));

        this.bottomAppBarTitle.setText(R.string.select_cards);
        this.bottomAppBarTitle.setVisibility(View.VISIBLE);

        this.bottomAppBar.setNavigationIcon(R.drawable.back);
        this.bottomAppBar.setNavigationOnClickListener(this.onBackClickListener);
    }
}
