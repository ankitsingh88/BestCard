package com.ankitsingh.bestcard;

import android.content.res.Configuration;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;

import com.ankitsingh.bestcard.fragments.BottomNavigationFragment;
import com.ankitsingh.bestcard.fragments.BottomSettingsFragment;
import com.ankitsingh.bestcard.fragments.CardRepositoryFragment;
import com.ankitsingh.bestcard.fragments.CardsFragment;
import com.ankitsingh.bestcard.fragments.CategoriesFragment;
import com.ankitsingh.bestcard.util.BiometricAuthenticationUtil;
import com.ankitsingh.bestcard.util.DataLoader;
import com.ankitsingh.bestcard.util.NavigationViewUtil;
import com.ankitsingh.bestcard.util.NightModeUtil;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

@Getter
public class BestCardActivity extends AppCompatActivity {

    private final BottomNavigationFragment bottomNavigationFragment = new BottomNavigationFragment();
    private final BottomSettingsFragment bottomSettingsFragment = new BottomSettingsFragment();

    private CoordinatorLayout mainActivity;

    private final CardsFragment cardsFragment = new CardsFragment();
    private final CardRepositoryFragment cardRepositoryFragment = new CardRepositoryFragment();
    private final CategoriesFragment categoriesFragment = new CategoriesFragment();

    private DataLoader dataLoader;

    private BottomAppBar bottomAppBar;
    private FloatingActionButton bottomAppBarAction;
    private TextView bottomAppBarTitle;

    @Setter
    private boolean biometricAuthenticationEnabled = false;

    private BiometricPrompt biometricPrompt;

    private final BiometricPrompt.AuthenticationCallback biometricPromptAuthenticationCallback =
            new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(
                        @NonNull final BiometricPrompt.AuthenticationResult result) {
                    NavigationViewUtil.setNavigationBarColor(BestCardActivity.this, false);

                    Toast.makeText(BestCardActivity.this, "Authenticated", Toast.LENGTH_SHORT).show();

                    mainActivity.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAuthenticationError(
                        final int errorCode,
                        @NonNull CharSequence errString) {
                    NavigationViewUtil.setNavigationBarColor(BestCardActivity.this, false);

                    Toast.makeText(BestCardActivity.this, "Could not authenticate", Toast.LENGTH_SHORT).show();

                    BestCardActivity.this.finish();
                }

                @Override
                public void onAuthenticationFailed() {
                    NavigationViewUtil.setNavigationBarColor(BestCardActivity.this, false);

                    Toast.makeText(BestCardActivity.this, "Could not authenticate", Toast.LENGTH_SHORT).show();

                    BestCardActivity.this.finish();
                }
            };

    private final BottomAppBar.OnMenuItemClickListener onBottomAppBarMenuItemClickListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.bottom_app_bar_search:
                        if (cardsFragment.getCardsAdapter().getCurrentList().isEmpty()) {
                            Toast.makeText(BestCardActivity.this, "Nothing to search", Toast.LENGTH_SHORT).show();

                            return true;
                        } else {
                            if (!isCardsFragmentVisible()) {
                                setCardsFragment();
                            }

                            this.cardsFragment.setupSearchMode(true);

                            return true;
                        }
                    case R.id.bottom_app_bar_settings:
                        bottomSettingsFragment.show(getSupportFragmentManager(), "BottomSettingsFragment");
                        return true;
                }
                return false;
            };

    private final View.OnClickListener onNavigationClickListener =
            v -> bottomNavigationFragment.show(getSupportFragmentManager(), "BottomNavigationFragment");

    private final View.OnClickListener onAddClickListener =
            v -> {
                setCardRepositoryFragment();
            };

    public boolean isCardsFragmentVisible() {
        return this.cardsFragment.isVisible();
    }

    public boolean isCardRepositoryFragmentVisible() {
        return this.cardRepositoryFragment.isVisible();
    }

    public boolean isCategoriesFragmentVisible() {
        return this.categoriesFragment.isVisible();
    }

    public void setCardsFragment() {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction
                .replace(R.id.main_fragment, this.cardsFragment)
                .commitNow();
    }

    public void setCardRepositoryFragment() {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction
                .replace(R.id.main_fragment, this.cardRepositoryFragment)
                .commitNow();
    }

    public void setCategoriesFragment() {
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction
                .detach(this.categoriesFragment)
                .attach(this.categoriesFragment)
                .replace(R.id.main_fragment, this.categoriesFragment)
                .commitNow();
    }

    public void setNightMode(
            final int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);
    }

    @Override
    public void onBackPressed() {
        if (isCardsFragmentVisible()) {
            if (this.cardsFragment.getSearchBar().getVisibility() == View.VISIBLE) {
                this.cardsFragment.getSearchCloseButton().callOnClick();
            } else {
                super.onBackPressed();
            }
        } else if (isCardRepositoryFragmentVisible()) {
            setCardsFragment();
        } else if (isCategoriesFragmentVisible()) {
            setCardsFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (biometricAuthenticationEnabled
                && BiometricManager.from(this).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
            NavigationViewUtil.setNavigationBarColor(this, true);

            this.mainActivity.setVisibility(View.INVISIBLE);

            this.biometricPrompt.authenticate(BiometricAuthenticationUtil.createBiometricAuthenticationPromptDialog());
        } else {
            this.mainActivity.setVisibility(View.VISIBLE);

            getDataLoader().saveBiometricAuthentication(false);
            this.biometricAuthenticationEnabled = false;
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        setupElements();

        setupBiometricPrompt();
    }

    private void setupElements() {
        if (Objects.isNull(this.dataLoader)) {
            this.dataLoader = new DataLoader(this);
        }

        this.mainActivity = findViewById(R.id.main_activity);
        this.mainActivity.setVisibility(View.INVISIBLE);

        this.bottomAppBarAction = findViewById(R.id.bottom_app_bar_action);
        this.bottomAppBarTitle = findViewById(R.id.bottom_app_bar_title);
        this.bottomAppBar = findViewById(R.id.bottom_app_bar);

        setBiometricAuthenticationEnabled(this.dataLoader.isBiometricAuthentication());

        setNightMode(this.dataLoader.getNightMode());

        this.cardsFragment.setEnterTransition(new Fade(Fade.IN).addTarget(R.id.cards_fragment));
        this.cardsFragment.setExitTransition(new Fade(Fade.OUT).addTarget(R.id.cards_fragment));

        this.cardRepositoryFragment.setEnterTransition(new Fade(Fade.IN).addTarget(R.id.card_repository_fragment));
        this.cardRepositoryFragment.setExitTransition(new Fade(Fade.OUT).addTarget(R.id.card_repository_fragment));

        this.categoriesFragment.setEnterTransition(new Fade(Fade.IN).addTarget(R.id.categories_fragment));
        this.categoriesFragment.setExitTransition(new Fade(Fade.OUT).addTarget(R.id.categories_fragment));

        setCardsFragment();

        this.bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        this.bottomAppBar.setNavigationOnClickListener(this.onNavigationClickListener);
        this.bottomAppBar.setOnMenuItemClickListener(this.onBottomAppBarMenuItemClickListener);

        this.bottomAppBarAction.setOnClickListener(this.onAddClickListener);
        this.bottomAppBarAction.setBackgroundTintList(
                ContextCompat.getColorStateList(
                        getApplicationContext(),
                        NightModeUtil.getColorPrimary(
                                NightModeUtil.getIntendedThemeMode(
                                        getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK,
                                        AppCompatDelegate.getDefaultNightMode()))));
    }

    private void setupBiometricPrompt() {
        this.biometricPrompt =
                new BiometricPrompt(
                        this,
                        getMainExecutor(),
                        this.biometricPromptAuthenticationCallback);
    }
}
