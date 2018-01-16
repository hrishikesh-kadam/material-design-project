package com.example.xyzreader.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.example.xyzreader.R;

/**
 * Created by Hrishikesh Kadam on 15/01/2018
 */

public class ArticleDetailsHeaderBehavior extends CoordinatorLayout.Behavior<TextView> {

    private static final String LOG_TAG = ArticleDetailsHeaderBehavior.class.getSimpleName();

    private DisplayMetrics displayMetrics;
    private float collapsedTitleTextSize;
    private float expandedTitleTextSize;
    private float titleTextSizeDifference;
    private float collapsedY;
    private float expandedY;
    private float appBarScrollDifference;
    private float scrollPercent;
    private float expandedTitleMarginTop;
    private float collapsedTitleMarginTop;
    private float titleMarginTopDifference;
    private float collapsedTitleMarginStart;
    private float expandedTitleMarginStart;
    private float titleMarginStartDifference;
    private boolean isTitleTextViewCollapsed = false;
    private float fromTitleMarginTop;
    private float animationTitleMarginTopDifference;
    private float scrollPosition;
    private boolean titleTextViewAnimationInProgress;
    private float animateAt;

    public ArticleDetailsHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        displayMetrics = context.getResources().getDisplayMetrics();
        Log.i(LOG_TAG, "-> constructor -> " + displayMetrics.toString());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArticleDetailsHeaderTextView);

        expandedTitleTextSize = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_android_textSize, 0F);
        //Log.d(LOG_TAG, "-> constructor -> expandedTitleTextSize = " + expandedTitleTextSize);

        collapsedY = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_collapsed_y, 0F);
        //Log.d(LOG_TAG, "-> constructor -> collapsedY = " + collapsedY);

        expandedY = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_expanded_y, 0F);
        //Log.d(LOG_TAG, "-> constructor -> expandedY = " + expandedY);

        appBarScrollDifference = expandedY - collapsedY;

        expandedTitleMarginTop = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_android_layout_marginTop, 0F);
        //Log.d(LOG_TAG, "-> constructor -> expandedTitleMarginTop = " + expandedTitleMarginTop);

        expandedTitleMarginStart = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_android_layout_marginStart, 0F);
        //Log.d(LOG_TAG, "-> constructor -> expandedTitleMarginStart = " + expandedTitleMarginStart);

        collapsedTitleMarginStart = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_collapsed_title_marginStart, 0F);
        //Log.d(LOG_TAG, "-> constructor -> collapsedTitleMarginStart = " + collapsedTitleMarginStart);

        titleMarginStartDifference = collapsedTitleMarginStart - expandedTitleMarginStart;

        animateAt = typedArray.getFloat(
                R.styleable.ArticleDetailsHeaderTextView_animationAt, 0.3F);
        //Log.d(LOG_TAG, "-> constructor -> animateAt = " + animateAt);

        typedArray.recycle();

        int[] textViewToolbarAttrs = {android.R.attr.textSize};
        typedArray = context.obtainStyledAttributes(
                R.style.TextAppearance_AppCompat_Widget_ActionBar_Title, textViewToolbarAttrs);

        collapsedTitleTextSize = typedArray.getDimension(0, 0F);
        //Log.d(LOG_TAG, "-> constructor -> collapsedTitleTextSize = " + collapsedTitleTextSize);

        titleTextSizeDifference = expandedTitleTextSize - collapsedTitleTextSize;
        //Log.d(LOG_TAG, "-> constructor -> titleTextSizeDifference = " + titleTextSizeDifference);

        typedArray.recycle();

        int[] actionBarAttrs = {android.R.attr.actionBarSize};
        typedArray = context.obtainStyledAttributes(actionBarAttrs);

        float actionBarSize = typedArray.getDimension(0, 0F);

        typedArray.recycle();

        //TODO optimise if possible
        collapsedTitleMarginTop = (actionBarSize - collapsedTitleTextSize) / 2;
        //Log.d(LOG_TAG, "-> constructor -> collapsedTitleMarginTop = " + collapsedTitleMarginTop);

        titleMarginTopDifference = expandedTitleMarginTop - collapsedTitleMarginTop;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency.getId() == R.id.nestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(final CoordinatorLayout parent, final TextView child, View dependency) {

        scrollPosition = dependency.getY();
        //Log.v(LOG_TAG, "-> onDependentViewChanged -> scrollPosition = " + scrollPosition);

        scrollPercent = ((scrollPosition - collapsedY)
                / appBarScrollDifference);
        //Log.d(LOG_TAG, "-> onDependentViewChanged -> scrollPercent = " + scrollPercent);

        if (scrollPercent >= animateAt && isTitleTextViewCollapsed) {

            Animation animation = new Animation() {

                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {

                    //Log.d(LOG_TAG, "-> applyTransformation -> interpolatedTime = " + interpolatedTime);

                    float textSizePx =
                            collapsedTitleTextSize + (titleTextSizeDifference * interpolatedTime);
                    //Log.d(LOG_TAG, "-> applyTransformation -> textSizePx = " + textSizePx);
                    child.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);

                    float titleMarginTopAtScrollPosition = expandedTitleMarginTop - (expandedY - scrollPosition);
                    //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginTopAtScrollPosition = " + titleMarginTopAtScrollPosition);

                    animationTitleMarginTopDifference = titleMarginTopAtScrollPosition - collapsedTitleMarginTop;

                    float titleMarginTop =
                            collapsedTitleMarginTop + (animationTitleMarginTopDifference * interpolatedTime);
                    //Log.d(LOG_TAG, "-> applyTransformation -> titleMarginTop = " + titleMarginTop);

                    float titleMarginStart =
                            collapsedTitleMarginStart - (titleMarginStartDifference * interpolatedTime);
                    //Log.d(LOG_TAG, "-> applyTransformation -> titleMarginStart = " + titleMarginStart);

                    CoordinatorLayout.LayoutParams layoutParams =
                            (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                    //layoutParams.setMarginStart((int) titleMarginStart);
                    layoutParams.setMargins((int) titleMarginStart, (int) titleMarginTop, 0, 0);
                    child.setLayoutParams(layoutParams);

                    if (interpolatedTime == 0.0F) {

                        parent.findViewById(R.id.textViewToolbar).setVisibility(View.INVISIBLE);
                        child.setVisibility(View.VISIBLE);
                    }
                }
            };

            animation.setDuration(200);

            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    titleTextViewAnimationInProgress = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    titleTextViewAnimationInProgress = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            child.startAnimation(animation);

            isTitleTextViewCollapsed = false;

        } else if (scrollPercent >= animateAt && !isTitleTextViewCollapsed && !titleTextViewAnimationInProgress) {

            child.setTextSize(TypedValue.COMPLEX_UNIT_PX, expandedTitleTextSize);

            float titleMarginTop = expandedTitleMarginTop - (expandedY - scrollPosition);
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginTop = " + titleMarginTop);

            CoordinatorLayout.LayoutParams layoutParams =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            layoutParams.setMargins((int) expandedTitleMarginStart, (int) titleMarginTop, 0, 0);
            child.setLayoutParams(layoutParams);

            if (scrollPercent > 0.8)
                child.clearAnimation();

        } else if (scrollPercent < animateAt && !isTitleTextViewCollapsed) {

            fromTitleMarginTop = child.getY();
            animationTitleMarginTopDifference = fromTitleMarginTop - collapsedTitleMarginTop;

            Animation animation = new Animation() {

                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {

                    //Log.d(LOG_TAG, "-> applyTransformation -> interpolatedTime = " + interpolatedTime);

                    float textSizePx =
                            expandedTitleTextSize - (titleTextSizeDifference * interpolatedTime);
                    //Log.d(LOG_TAG, "-> applyTransformation -> textSizePx = " + textSizePx);
                    child.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);

                    float titleMarginTop =
                            fromTitleMarginTop - (animationTitleMarginTopDifference * interpolatedTime);
                    //Log.d(LOG_TAG, "-> applyTransformation -> titleMarginTop = " + titleMarginTop);

                    float titleMarginStart =
                            expandedTitleMarginStart + (titleMarginStartDifference * interpolatedTime);
                    //Log.d(LOG_TAG, "-> applyTransformation -> titleMarginStart = " + titleMarginStart);

                    CoordinatorLayout.LayoutParams layoutParams =
                            (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                    //layoutParams.setMarginStart((int) titleMarginStart);
                    layoutParams.setMargins((int) titleMarginStart, (int) titleMarginTop, 0, 0);
                    child.setLayoutParams(layoutParams);

                    if (interpolatedTime == 1.0F) {
                        parent.findViewById(R.id.textViewToolbar).setVisibility(View.VISIBLE);
                        child.setVisibility(View.INVISIBLE);
                    }
                }
            };

            animation.setDuration(200);

            animation.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                    titleTextViewAnimationInProgress = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    titleTextViewAnimationInProgress = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            child.startAnimation(animation);

            isTitleTextViewCollapsed = true;
        }

        return true;
    }

    /*@Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {

        float scrollPosition = dependency.getY();
        //Log.v(LOG_TAG, "-> onDependentViewChanged -> scrollPosition = " + scrollPosition);

        scrollPercent = ((scrollPosition - collapsedY)
                / appBarScrollDifference);

        if (scrollPercent >= 0.0 && scrollPercent <= 1.0) {

            //Log.d(LOG_TAG, "-> onDependentViewChanged -> scrollPercent = " + scrollPercent);

            float textSizePx =
                    collapsedTitleTextSize + (titleTextSizeDifference * scrollPercent);
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> textSizePx = " + textSizePx);
            child.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);

            float titleMarginTop = expandedTitleMarginTop - (expandedY - scrollPosition);
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginTop = " + titleMarginTop);

            float titleMarginStart =
                    collapsedTitleMarginStart + (titleMarginStartDifference * scrollPercent);
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginStart = " + titleMarginStart);

            CoordinatorLayout.LayoutParams layoutParams =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            layoutParams.setMargins((int) titleMarginStart, (int) titleMarginTop, 0, 0);
            child.setLayoutParams(layoutParams);

            child.setVisibility(View.VISIBLE);

        } else if (scrollPercent < 0) {

            child.setVisibility(View.INVISIBLE);
        }

        return true;
    }*/
}

