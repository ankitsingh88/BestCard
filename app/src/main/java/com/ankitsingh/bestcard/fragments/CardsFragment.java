package com.ankitsingh.bestcard.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.adapters.CardAdapter;
import com.ankitsingh.bestcard.util.DataUtil;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.stream.Collectors;

import lombok.Getter;

@Getter
public class CardsFragment extends Fragment {

    private CardAdapter cardAdapter;
    private RecyclerView listCards;
    private ExtendedFloatingActionButton cardAdd;

    private final ExtendedFloatingActionButton.OnClickListener onAddClickListener =
            v -> {
                System.out.println(v);
            };

    private final RecyclerView.OnScrollListener listCardsOnScrollListener =
            new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(
                        final @NonNull RecyclerView recyclerView,
                        final int newState) {
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        cardAdd.hide();
                    } else {
                        cardAdd.show();
                    }
                }
            };

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cards, container, false);

        this.cardAdd = view.findViewById(R.id.card_add);
        this.listCards = view.findViewById(R.id.list_cards);

        this.cardAdapter =
                new CardAdapter(
                        this,
                        this.listCards,
                        getActivity(),
                        DataUtil.loadCards().stream()
                                .map(card ->
                                        CardAdapter.CardStateHolder.builder()
                                                .card(card)
                                                .isExpanded(false)
                                                .build())
                                .collect(Collectors.toList()));

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        this.listCards.addOnScrollListener(this.listCardsOnScrollListener);
        this.listCards.setAdapter(this.cardAdapter);
        this.listCards.setLayoutManager(linearLayoutManager);
        this.listCards.addItemDecoration(new DividerItemDecoration(this.listCards.getContext(), linearLayoutManager.getOrientation()));
        this.listCards.setHasFixedSize(true);

        ((SimpleItemAnimator) this.listCards.getItemAnimator()).setSupportsChangeAnimations(false);

        this.cardAdd.setOnClickListener(this.onAddClickListener);

        return view;
    }
}
