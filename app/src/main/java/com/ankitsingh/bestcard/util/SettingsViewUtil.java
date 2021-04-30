package com.ankitsingh.bestcard.util;

import android.text.Html;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.fragments.BottomSettingsFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.HashMap;

public class SettingsViewUtil {

    public static void createClearSearchHistoryDialog(
            final BestCardActivity bestCardActivity) {
        new MaterialAlertDialogBuilder(bestCardActivity, R.style.AlertDialog)
                .setIcon(R.drawable.clear)
                .setMessage(
                        Html.fromHtml(
                                "Search history is used to show <b>Top categories</b>.",
                                Html.FROM_HTML_MODE_COMPACT))
                .setTitle(
                        Html.fromHtml(
                                "Clear search history?",
                                Html.FROM_HTML_MODE_COMPACT))
                .setPositiveButton(
                        R.string.delete,
                        (dialog, id) -> {
                            bestCardActivity.getDataLoader().saveCategoryRankMap(new HashMap<>());
                        })
                .setNegativeButton(
                        R.string.cancel,
                        (dialog, id) -> {})
                .show();
    }

    public static void createBiometricDisableConfirmationDialog(
            final BestCardActivity bestCardActivity,
            final BottomSettingsFragment bottomSettingsFragment) {
        new MaterialAlertDialogBuilder(bestCardActivity, R.style.AlertDialog)
                .setIcon(R.drawable.disable)
                .setMessage(
                        Html.fromHtml(
                                "Are you sure you want to disable <b>biometric authentication</b> when accessing this app?",
                                Html.FROM_HTML_MODE_COMPACT))
                .setTitle(
                        Html.fromHtml(
                                "Disable <b>biometric authentication</b>?",
                                Html.FROM_HTML_MODE_COMPACT))
                .setPositiveButton(
                        R.string.disable,
                        (dialog, id) -> bottomSettingsFragment.setBiometrics(false))
                .setNegativeButton(
                        R.string.cancel,
                        (dialog, id) -> bottomSettingsFragment.setBiometrics(true))
                .setOnCancelListener(dialog -> bottomSettingsFragment.setBiometrics(true))
                .show();
    }
}
