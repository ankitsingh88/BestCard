package com.ankitsingh.bestcard.util;

import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ankitsingh.bestcard.BestCardActivity;
import com.ankitsingh.bestcard.R;

public class AnimationUtil {

    private static long fadeInDuration = 500;
    private static long fadeOutDuration = 125;
    private static long expandCollapseDuration = 5000/2;
    private static long elevateDuration = 500;

//    public static void expand(
//            final BestCardActivity bestCardActivity,
//            final RelativeLayout row,
//            final LinearLayout expanded,
//            final LinearLayout actionContainer,
//            final ImageButton expandCollapseButton,
//            final int targetHeight) {
//        final Animation expandAnimation = new Animation() {
//            @Override
//            protected void applyTransformation(
//                    final float interpolatedTime,
//                    final Transformation t) {
//                expanded.getLayoutParams().height = interpolatedTime == 1
//                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
//                        : (int)(targetHeight * interpolatedTime);
//                expanded.requestLayout();
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//        expandAnimation.setDuration(expandCollapseDuration);
//
//        final Animation fadeInExpanded = new AlphaAnimation(0, 1);
//        fadeInExpanded.setInterpolator(new DecelerateInterpolator());
//        fadeInExpanded.setDuration(fadeInDuration);
//
//        final AnimationSet expandedAnimationSet = new AnimationSet(false);
//        expandedAnimationSet.addAnimation(expandAnimation);
//        expandedAnimationSet.addAnimation(fadeInExpanded);
//        expandedAnimationSet.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                expanded.setVisibility(View.VISIBLE);
//                actionContainer.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                expanded.getLayoutParams().height = targetHeight;
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//
////        row.animate()
////                .translationZ(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics()))
////                .setDuration(elevateDuration)
////                .start();
//
//        expanded.startAnimation(expandedAnimationSet);
//
//        expandCollapseButton.setImageResource(R.drawable.collapse);
//    }

    public static void collapse(
            final BestCardActivity bestCardActivity,
            final RelativeLayout row,
            final LinearLayout expanded,
            final LinearLayout actionContainer,
            final ImageButton expandCollapseButton,
            final int initialHeight) {
        final Animation collapse = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                expanded.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                expanded.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        collapse.setDuration(expandCollapseDuration);

        final Animation fadeOutExpanded = new AlphaAnimation(1, 0);
        fadeOutExpanded.setInterpolator(new AccelerateInterpolator());
        fadeOutExpanded.setDuration(fadeOutDuration);

        final AnimationSet expandedAnimationSet = new AnimationSet(false);
        expandedAnimationSet.addAnimation(collapse);
        expandedAnimationSet.addAnimation(fadeOutExpanded);
        expandedAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                actionContainer.setVisibility(View.GONE);
                expanded.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

//        row.animate()
//                .translationZ(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, bestCardActivity.getResources().getDisplayMetrics()))
//                .setDuration(elevateDuration)
//                .start();

        expanded.startAnimation(expandedAnimationSet);

        expandCollapseButton.setImageResource(R.drawable.expand);
    }

//    public static void delete(
//            final RelativeLayout row,
//            final String cardId,
//            final CardAdapter cardAdapter,
//            final int initialHeight) {
//        final Animation collapse = new Animation() {
//            @Override
//            protected void applyTransformation(
//                    final float interpolatedTime, final Transformation t) {
//                row.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
//                row.requestLayout();
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//        collapse.setDuration(expandCollapseDuration);
//
//        final Animation fadeOutRow = new AlphaAnimation(1, 0);
//        fadeOutRow.setInterpolator(new AccelerateInterpolator());
//        fadeOutRow.setDuration(fadeOutDuration);
//
//        final AnimationSet rowAnimationSet = new AnimationSet(false);
//        rowAnimationSet.addAnimation(collapse);
//        rowAnimationSet.addAnimation(fadeOutRow);
//        rowAnimationSet.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(final Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(final Animation animation) {
//                cardAdapter.deleteCardById(cardId);
//            }
//
//            @Override
//            public void onAnimationRepeat(final Animation animation) {
//            }
//        });
//
//        row.startAnimation(rowAnimationSet);
//    }

    public static void expand(
            final BestCardActivity bestCardActivity,
            final RelativeLayout row,
            final LinearLayout expanded,
            final LinearLayout actionContainer,
            final ImageButton expandCollapseButton,
            final int targetHeight) {
        final Animation expandAnimation = new Animation() {
            @Override
            protected void applyTransformation(
                    final float interpolatedTime,
                    final Transformation t) {
                expanded.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                expanded.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        expandAnimation.setDuration(expandCollapseDuration);

        final Animation fadeInExpanded = new AlphaAnimation(0, 1);
        fadeInExpanded.setInterpolator(new DecelerateInterpolator());
        fadeInExpanded.setDuration(fadeInDuration);

        final AnimationSet expandedAnimationSet = new AnimationSet(false);
        expandedAnimationSet.addAnimation(expandAnimation);
        expandedAnimationSet.addAnimation(fadeInExpanded);
        expandedAnimationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
//                expanded.setVisibility(View.VISIBLE);
//                actionContainer.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                expanded.getLayoutParams().height = targetHeight;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        row.animate()
                .translationZ(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, bestCardActivity.getResources().getDisplayMetrics()))
                .setDuration(elevateDuration)
                .start();

        expanded.startAnimation(expandedAnimationSet);

        expandCollapseButton.setImageResource(R.drawable.collapse);
    }
}
