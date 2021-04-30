package com.ankitsingh.bestcard.util;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.DisplayMetrics;
import android.view.Window;

import androidx.fragment.app.FragmentActivity;

import com.ankitsingh.bestcard.R;

import java.util.Objects;

public class NavigationViewUtil {

    public static void setNavigationBarColor(
            final FragmentActivity fragmentActivity,
            final Window window,
            final boolean isDim) {
        if (Objects.nonNull(window)) {
            final DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            final GradientDrawable dimDrawable = new GradientDrawable();

            final GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);

            if (isDim) {
                navigationBarDrawable.setColor(fragmentActivity.getResources().getColor(R.color.dimOnSurface, fragmentActivity.getTheme()));
            } else {
                navigationBarDrawable.setColor(fragmentActivity.getResources().getColor(R.color.elevatedColorBackground, fragmentActivity.getTheme()));
            }

            final Drawable[] layers = {dimDrawable, navigationBarDrawable};

            final LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    public static void setNavigationBarColor(
            final Activity activity,
            final boolean isDim) {
        if (isDim) {
            activity.getWindow().setNavigationBarColor(
                    activity.getResources().getColor(R.color.dimOnSurface, activity.getTheme()));
        } else {
            activity.getWindow().setNavigationBarColor(
                    activity.getResources().getColor(R.color.elevatedColorBackground, activity.getTheme()));
        }
    }
}
