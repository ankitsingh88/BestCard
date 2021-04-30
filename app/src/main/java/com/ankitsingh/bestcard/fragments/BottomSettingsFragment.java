package com.ankitsingh.bestcard.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.util.BiometricAuthenticationUtil;
import com.ankitsingh.bestcard.util.NavigationViewUtil;
import com.ankitsingh.bestcard.util.SettingsViewUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.HashMap;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Getter;

@Getter
public class BottomSettingsFragment extends BottomSheetDialogFragment {

    private static final String LIGHT = "light";
    private static final String DARK = "dark";
    private static final String AUTO = "auto";

    @Getter(AccessLevel.NONE)
    private Dialog dialog;

    private NavigationView navigationView;

    private MenuItem biometricAuthenticationMenuItem;
    private SwitchMaterial setupBiometricAuthentication;
    private MaterialButton clearSearchHistory;
    private Map<String, MaterialButton> nightModeMap = new HashMap<>();

    private boolean canAuthenticateUsingBiometrics = false;
    private boolean biometricsEnabled = false;

    private BiometricPrompt biometricPrompt;

    private final View.OnClickListener setupBiometricAuthenticationOnClickListener =
            view -> {
                if (((SwitchMaterial) view).isChecked()) {
                    setBiometricAuthentication(true);
                } else {
                    setBiometricAuthentication(false);
                }
            };

    private final BiometricPrompt.AuthenticationCallback biometricPromptAuthenticationCallback =
            new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationSucceeded(
                        @NonNull final BiometricPrompt.AuthenticationResult result) {
                    NavigationViewUtil.setNavigationBarColor(BottomSettingsFragment.this.getActivity(), dialog.getWindow(), false);

                    Toast.makeText(getBestCardActivity(), "Authenticated", Toast.LENGTH_SHORT).show();

                    setBiometrics(true);
                }

                @Override
                public void onAuthenticationError(
                        final int errorCode,
                        @NonNull CharSequence errString) {
                    NavigationViewUtil.setNavigationBarColor(BottomSettingsFragment.this.getActivity(), dialog.getWindow(), false);

                    Toast.makeText(getBestCardActivity(), "Could not authenticate", Toast.LENGTH_SHORT).show();

                    setBiometrics(false);
                }

                @Override
                public void onAuthenticationFailed() {
                    NavigationViewUtil.setNavigationBarColor(BottomSettingsFragment.this.getActivity(), dialog.getWindow(), false);

                    Toast.makeText(getBestCardActivity(), "Could not authenticate", Toast.LENGTH_SHORT).show();

                    setBiometrics(false);
                }
            };

    private final View.OnClickListener clearSuggestionsOnClickListener =
            view -> clearSearchHistoryMap();

    private final View.OnClickListener nightModeOnClickListener =
            view -> checkNightMode(view.getId());

    @Override
    public View onCreateView(
            final LayoutInflater inflater,
            final ViewGroup container,
            final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_bottom_settings, container, false);

        setupNavigationView(view);

        setupBiometricAuthentication(this.navigationView.getMenu().findItem(R.id.biometric_authentication));

        setupSearchHistory(this.navigationView.getMenu().findItem(R.id.search_history).getActionView());

        setupNightMode(this.navigationView.getMenu().findItem(R.id.night_mode).getActionView());

        setCurrentNightMode(getBestCardActivity().getDataLoader().getNightMode());

        return view;
    }

    @Override
    public Dialog onCreateDialog(
            final Bundle savedInstanceState) {
        this.dialog = super.onCreateDialog(savedInstanceState);

        NavigationViewUtil.setNavigationBarColor(getActivity(), this.dialog.getWindow(), false);

        return this.dialog;
    }

    private void setupNavigationView(
            final View view) {
        this.navigationView = view.findViewById(R.id.settings_view);
    }

    private void setupBiometricAuthentication(
            final MenuItem biometricAuthenticationMenuItem) {
        this.biometricAuthenticationMenuItem = biometricAuthenticationMenuItem;
        this.setupBiometricAuthentication = biometricAuthenticationMenuItem.getActionView().findViewById(R.id.biometric_authentication_selector);
        this.setupBiometricAuthentication.setOnClickListener(this.setupBiometricAuthenticationOnClickListener);

        this.biometricPrompt =
                new BiometricPrompt(
                        this,
                        getBestCardActivity().getMainExecutor(),
                        this.biometricPromptAuthenticationCallback);

        this.canAuthenticateUsingBiometrics = BiometricManager.from(getBestCardActivity()).canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS;

        if (this.canAuthenticateUsingBiometrics) {
            this.setupBiometricAuthentication.setVisibility(View.VISIBLE);
            this.biometricAuthenticationMenuItem.setVisible(true);

            this.biometricsEnabled = getBestCardActivity().isBiometricAuthenticationEnabled();
            this.setupBiometricAuthentication.setChecked(this.biometricsEnabled);
        } else {
            this.setupBiometricAuthentication.setVisibility(View.GONE);
            this.biometricAuthenticationMenuItem.setVisible(false);

            getBestCardActivity().setBiometricAuthenticationEnabled(false);
            this.biometricsEnabled = false;
        }
    }

    private void setupSearchHistory(
            final View view) {
        this.clearSearchHistory = view.findViewById(R.id.clear_search_history_selector);
        this.clearSearchHistory.setOnClickListener(this.clearSuggestionsOnClickListener);
    }

    private void setupNightMode(final View view) {
        this.nightModeMap.put(LIGHT, view.findViewById(R.id.night_mode_light));
        this.nightModeMap.put(DARK, view.findViewById(R.id.night_mode_dark));
        this.nightModeMap.put(AUTO, view.findViewById(R.id.night_mode_auto));

        this.nightModeMap.forEach((key, nightMode) -> {
            nightMode.setCheckable(false);
            nightMode.setOnClickListener(this.nightModeOnClickListener);
        });
    }

    private void setBiometricAuthentication(final boolean enabled) {
        if (enabled) {
            NavigationViewUtil.setNavigationBarColor(this.getActivity(), this.dialog.getWindow(), true);

            this.biometricPrompt.authenticate(BiometricAuthenticationUtil.createSetupBiometricPromptInfoDialog());
        } else {
            SettingsViewUtil.createBiometricDisableConfirmationDialog(getBestCardActivity(), this);
        }

    }

    public void setBiometrics(
            final boolean enabled) {
        if (enabled) {
            saveBiometricAuthentication(true);

            this.setupBiometricAuthentication.setChecked(true);
        } else {
            saveBiometricAuthentication(false);

            this.setupBiometricAuthentication.setChecked(false);
        }
    }

    private void saveBiometricAuthentication(
            final boolean enabled) {
        getBestCardActivity().getDataLoader().saveBiometricAuthentication(enabled);
        getBestCardActivity().setBiometricAuthenticationEnabled(enabled);
    }

    private void clearSearchHistoryMap() {
        SettingsViewUtil.createClearSearchHistoryDialog(getBestCardActivity());
    }

    private void setCurrentNightMode(
            final int nightMode) {
        switch (nightMode) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                checkNightMode(R.id.night_mode_light);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                checkNightMode(R.id.night_mode_dark);
                break;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                checkNightMode(R.id.night_mode_auto);
                break;
        }
    }

    private void checkNightMode(
            final int id) {
        switch (id) {
            case R.id.night_mode_light:
                checkNightMode(nightModeMap.get(LIGHT));
                saveNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.night_mode_dark:
                checkNightMode(nightModeMap.get(DARK));
                saveNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.night_mode_auto:
                checkNightMode(nightModeMap.get(AUTO));
                saveNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    private void saveNightMode(
            final int nightMode) {
        getBestCardActivity().getDataLoader().saveNightMode(nightMode);
        getBestCardActivity().setNightMode(nightMode);
    }

    private void checkNightMode(
            final MaterialButton currentNightMode) {
        this.nightModeMap.forEach((key, nightMode) -> {
            if (nightMode.equals(currentNightMode)) {
                check(nightMode);
            } else {
                uncheck(nightMode);
            }
        });
    }

    private void check(
            final MaterialButton nightMode) {
        nightMode.setCheckable(true);
        nightMode.setChecked(true);
        nightMode.setCheckable(false);
    }

    private void uncheck(
            final MaterialButton nightMode) {
        nightMode.setCheckable(true);
        nightMode.setChecked(false);
        nightMode.setCheckable(false);
    }

    private BestCardActivity getBestCardActivity() {
        return (BestCardActivity) getActivity();
    }
}
