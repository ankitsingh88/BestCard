package com.ankitsingh.bestcard.util;

import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import com.ankitsingh.bestcard.R;
import com.ankitsingh.bestcard.model.ThemeMode;

public class NightModeUtil {

    public static ThemeMode getIntendedThemeMode(
            final int uiMode,
            final int nightModeSetting) {
        switch (nightModeSetting) {
            case AppCompatDelegate.MODE_NIGHT_NO:
                return ThemeMode.DAY;
            case AppCompatDelegate.MODE_NIGHT_YES:
                return ThemeMode.NIGHT;
            case AppCompatDelegate.MODE_NIGHT_UNSPECIFIED:
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                if (uiMode == Configuration.UI_MODE_NIGHT_NO) {
                    return ThemeMode.DAY;
                } else if (uiMode == Configuration.UI_MODE_NIGHT_YES) {
                    return ThemeMode.NIGHT;
                }
            default:
                return ThemeMode.DAY;
        }
    }

    public static int getColorPrimary(
            final ThemeMode themeMode) {
        switch (themeMode) {
            case DAY:
                return R.color.colorPrimary;
            case NIGHT:
                return R.color.colorPrimaryDark;
            default:
                return R.color.colorPrimary;
        }
    }
}
