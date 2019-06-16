package com.ankitsingh.bestcard;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.ankitsingh.bestcard.adapters.ActivityPagerAdapter;
import com.ankitsingh.bestcard.adapters.CardAdapter;
import com.ankitsingh.bestcard.fragments.CardsFragment;
import com.ankitsingh.bestcard.fragments.CategoriesFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.apache.commons.lang3.StringUtils;

import lombok.Getter;

@Getter
public class BestCardActivity extends AppCompatActivity {

    private final CardsFragment cardsFragment = new CardsFragment();
    private final CategoriesFragment categoriesFragment = new CategoriesFragment();

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private SearchView searchView;

    private final BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(
                        final MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_cards:
                            viewPager.setCurrentItem(0);
                            return true;
                        case R.id.navigation_categories:
                            viewPager.setCurrentItem(1);
                            return true;
                    }
                    return false;
                }
            };

    private final ViewPager.OnPageChangeListener onPageChangeListener =
            new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(
                        final int position,
                        final float positionOffset,
                        final int positionOffsetPixels) {}

                @Override
                public void onPageScrollStateChanged(
                        final int state) {}

                @Override
                public void onPageSelected(
                        final int position) {
                    switch (position) {
                        case 0:
                            bottomNavigationView.setSelectedItemId(R.id.navigation_cards);
                            break;
                        case 1:
                            bottomNavigationView.setSelectedItemId(R.id.navigation_categories);
                            break;
                    }
                }
            };

    private final SearchView.OnQueryTextListener onQueryTextListener =
            new SearchView.OnQueryTextListener() {

                @Override
                public boolean onQueryTextSubmit(
                        final String query) {
                    viewPager.setCurrentItem(0);

                    if (cardsFragment.getView() != null) {
                        final RecyclerView listCards = cardsFragment.getView().findViewById(R.id.list_cards);

                        ((CardAdapter) listCards.getAdapter()).filter(query);
                    }

                    return true;
                }

                @Override
                public boolean onQueryTextChange(
                        final String newText) {
                    viewPager.setCurrentItem(0);

                    if (cardsFragment.getView() != null) {
                        final RecyclerView listCards = cardsFragment.getView().findViewById(R.id.list_cards);

                        if (StringUtils.isBlank(newText)) {
                            ((CardAdapter) listCards.getAdapter()).filter(newText);
                        }

                        cardsFragment.getCardAdd().hide();
                    }

                    return true;
                }
            };

    private final SearchView.OnClickListener onSearchClickListener =
            new SearchView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewPager.setCurrentItem(0);

                    cardsFragment.getCardAdd().hide();
                }
            };

    private final SearchView.OnCloseListener onSearchCloseListener =
            () -> {
                if (cardsFragment.getView() != null) {
                    final RecyclerView listCards = cardsFragment.getView().findViewById(R.id.list_cards);

                    ((CardAdapter) listCards.getAdapter()).filter(null);
                }

                return false;
            };

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.bottomNavigationView = findViewById(R.id.bottom_navigation);
        this.bottomNavigationView.setOnNavigationItemSelectedListener(this.onNavigationItemSelectedListener);

        this.viewPager = findViewById(R.id.activity_pager);

        setupActivityPager(getSupportFragmentManager(), this.viewPager);

        this.viewPager.setCurrentItem(0);
        this.viewPager.addOnPageChangeListener(this.onPageChangeListener);

        this.searchView = findViewById(R.id.search_bar);

        searchView.setOnQueryTextListener(this.onQueryTextListener);
        searchView.setOnSearchClickListener(this.onSearchClickListener);
        searchView.setOnCloseListener(this.onSearchCloseListener);
    }

    private void setupActivityPager(
            final FragmentManager fragmentManager,
            final ViewPager viewPager) {
        final ActivityPagerAdapter activityPagerAdapter = new ActivityPagerAdapter(fragmentManager);

        activityPagerAdapter.addFragment(this.cardsFragment);
        activityPagerAdapter.addFragment(this.categoriesFragment);

        viewPager.setAdapter(activityPagerAdapter);
    }
}
