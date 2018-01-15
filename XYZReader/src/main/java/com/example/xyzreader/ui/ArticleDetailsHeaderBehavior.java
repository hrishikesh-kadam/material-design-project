/*import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.xyzreader.R;

public class ArticleDetailsHeaderBehavior extends CoordinatorLayout.Behavior<TextView> {

    private static final String LOG_TAG = ArticleDetailsHeaderBehavior.class.getSimpleName();

    private DisplayMetrics displayMetrics;
    private float collapsedTitleTextSize;
    private float expandedTitleTextSize;
    private float titleTextSizeDifference;
    private float collapsedToolbarHeight;
    private float expandedToolbarHeight;
    private float appBarScrollDifference;
    private float scrollPercent;
    private float expandedTitleMarginTop;
    private float collapsedTitleMarginTop;
    private float titleMarginTopDifference;
    private float collapsedTitleMarginStart;
    private float expandedTitleMarginStart;
    private float titleMarginStartDifference;

    public ArticleDetailsHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        displayMetrics = context.getResources().getDisplayMetrics();
        Log.i(LOG_TAG, "-> constructor -> " + displayMetrics.toString());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ArticleDetailsHeaderTextView);

        expandedTitleTextSize = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_android_textSize, 0F);
        //Log.d(LOG_TAG, "-> constructor -> expandedTitleTextSize = " + expandedTitleTextSize);

        collapsedToolbarHeight = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_collapsed_toolbar_height, 0F);
        //Log.d(LOG_TAG, "-> constructor -> collapsedToolbarHeight = " + collapsedToolbarHeight);

        expandedToolbarHeight = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_expanded_toolbar_height, 0F);
        //Log.d(LOG_TAG, "-> constructor -> expandedToolbarHeight = " + expandedToolbarHeight);

        appBarScrollDifference = expandedToolbarHeight - collapsedToolbarHeight;

        expandedTitleMarginTop = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_android_layout_marginTop, 0F);
        //Log.d(LOG_TAG, "-> constructor -> expandedTitleMarginTop = " + expandedTitleMarginTop);

        expandedTitleMarginStart = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_android_layout_marginStart, 0F);
        //Log.d(LOG_TAG, "-> constructor -> expandedTitleMarginStart = " + expandedTitleMarginStart);

        collapsedTitleMarginStart = typedArray.getDimension(
                R.styleable.ArticleDetailsHeaderTextView_collapsed_title_marginStart, 0F);
        //Log.d(LOG_TAG, "-> constructor -> collapsedTitleMarginStart = " + collapsedTitleMarginStart);

        titleMarginStartDifference = expandedTitleMarginStart - collapsedTitleMarginStart;

        typedArray.recycle();

        int[] textViewToolbarAttrs = {android.R.attr.textSize};
        typedArray = context.obtainStyledAttributes(
                R.style.TextAppearance_AppCompat_Widget_ActionBar_Title, textViewToolbarAttrs);

        collapsedTitleTextSize = typedArray.getDimension(0, 0F);
        //Log.d(LOG_TAG, "-> constructor -> collapsedTitleTextSize = " + collapsedTitleTextSize);

        titleTextSizeDifference = expandedTitleTextSize - collapsedTitleTextSize;
        //Log.d(LOG_TAG, "-> constructor -> titleTextSizeDifference = " + titleTextSizeDifference);

        typedArray.recycle();

        collapsedTitleMarginTop = (collapsedToolbarHeight - collapsedTitleTextSize) / 2;
        //Log.d(LOG_TAG, "-> constructor -> collapsedTitleMarginTop = " + collapsedTitleMarginTop);

        titleMarginTopDifference = expandedTitleMarginTop - collapsedTitleMarginTop;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, TextView child, View dependency) {
        return dependency.getId() == R.id.nestedScrollView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {

        float scrollPosition = dependency.getY();
        //Log.v(LOG_TAG, "-> onDependentViewChanged -> scrollPosition = " + scrollPosition);

        scrollPercent = ((scrollPosition - collapsedToolbarHeight)
                / appBarScrollDifference);
        //Log.d(LOG_TAG, "-> onDependentViewChanged -> scrollPercent = " + scrollPercent);

        float textSizePx =
                collapsedTitleTextSize + (titleTextSizeDifference * scrollPercent);
        //Log.d(LOG_TAG, "-> onDependentViewChanged -> textSizePx = " + textSizePx);
        child.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);

        float titleMarginTop = collapsedTitleMarginTop + (titleMarginTopDifference * scrollPercent);
        //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginTop = " + titleMarginTop);

        float titleMarginStart =
                collapsedTitleMarginStart + (titleMarginStartDifference * scrollPercent);
        //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginStart = " + titleMarginStart);

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        layoutParams.setMargins((int) titleMarginStart, (int) titleMarginTop, 0, 0);
        child.setLayoutParams(layoutParams);

        return true;
    }
}*/

package com.example.xyzreader.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
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

        titleMarginStartDifference = expandedTitleMarginStart - collapsedTitleMarginStart;

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
    }
}

