package com.ankitsingh.bestcard.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.util.NavigationViewUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class BottomNavigationFragment extends BottomSheetDialogFragment {

    @Getter(AccessLevel.NONE)
    private Dialog dialog;

    private NavigationView navigationView;

    final NavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_cards:
                        getBestCardActivity().setCardsFragment();

                        dismiss();

                        return true;
                    case R.id.navigation_categories:
                        getBestCardActivity().setCategoriesFragment();

                        dismiss();

                        return true;
                }
                return false;
            };

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bottom_navigation, container, false);

        setupNavigationView(view);

        setHighlight();

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        this.dialog = super.onCreateDialog(savedInstanceState);

        NavigationViewUtil.setNavigationBarColor(getActivity(), this.dialog.getWindow(), false);

        return this.dialog;
    }

    private void setupNavigationView(
            final View view) {
        this.navigationView = view.findViewById(R.id.navigation_view);

        this.navigationView.setNavigationItemSelectedListener(this.onNavigationItemSelectedListener);
    }

    private void setHighlight() {
        if (getBestCardActivity().isCardsFragmentVisible()) {
            this.navigationView.getMenu().findItem(R.id.navigation_cards).setCheckable(true);
            this.navigationView.getMenu().findItem(R.id.navigation_cards).setChecked(true);
        }

        if (getBestCardActivity().isCategoriesFragmentVisible()) {
            this.navigationView.getMenu().findItem(R.id.navigation_categories).setCheckable(true);
            this.navigationView.getMenu().findItem(R.id.navigation_categories).setChecked(true);
        }
    }

    private BestCardActivity getBestCardActivity() {
        return (BestCardActivity) getActivity();
    }
}
