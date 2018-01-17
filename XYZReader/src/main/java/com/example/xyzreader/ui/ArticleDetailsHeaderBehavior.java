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
    private float yScrollDifference;

    private float expandedTitleMarginTop;
    private float collapsedTitleMarginTop;
    private float titleMarginTopDifference;

    private float collapsedTitleMarginStart;
    private float expandedTitleMarginStart;
    private float titleMarginStartDifference;

    private float shrinkAtPercent;
    private boolean isTextViewSubTitleVisible = true;

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

        yScrollDifference = expandedY - collapsedY;

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

        shrinkAtPercent = typedArray.getFloat(
                R.styleable.ArticleDetailsHeaderTextView_shrinkAtPercent, 0.3F);
        //Log.d(LOG_TAG, "-> constructor -> shrinkAtPercent = " + shrinkAtPercent);

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

        //Log.d(LOG_TAG, "-> layoutDependsOn -> " + dependency.getClass().getSimpleName());

        if (dependency.getId() == R.id.nestedScrollView) {

            TextView textViewSubTitle = parent.findViewById(R.id.textViewSubTitle);
            expandedTitleMarginStart = textViewSubTitle.getX() + textViewSubTitle.getPaddingStart();
            titleMarginStartDifference = collapsedTitleMarginStart - expandedTitleMarginStart;

            //Log.d(LOG_TAG, "-> layoutDependsOn -> expandedTitleMarginStart = " + expandedTitleMarginStart);

            return true;
        }
        return false;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, TextView child, View dependency) {

        float scrollPosition = dependency.getY();
        //Log.d(LOG_TAG, "-> onDependentViewChanged -> scrollPosition = " + scrollPosition);

        float scrollPercent = ((scrollPosition - collapsedY)
                / yScrollDifference);
        //Log.d(LOG_TAG, "-> onDependentViewChanged -> scrollPercent = " + scrollPercent);

        float shrinkScrollPercent = scrollPercent / shrinkAtPercent;

        if (scrollPercent < 0) {

            child.setVisibility(View.INVISIBLE);
            parent.findViewById(R.id.textViewToolbar).setVisibility(View.VISIBLE);
            return true;

        } else if (shrinkScrollPercent >= 0.0 && shrinkScrollPercent <= 1.0) {
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> shrinkScrollPercent = " + shrinkScrollPercent);

            float textSizePx =
                    collapsedTitleTextSize + (titleTextSizeDifference * shrinkScrollPercent);
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> textSizePx = " + textSizePx);
            child.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);

            float titleMarginTop = expandedTitleMarginTop - (expandedY - scrollPosition);
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginTop = " + titleMarginTop);

            float titleMarginStart =
                    collapsedTitleMarginStart - (titleMarginStartDifference * shrinkScrollPercent);
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginStart = " + titleMarginStart);

            CoordinatorLayout.LayoutParams layoutParams =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            layoutParams.setMargins((int) titleMarginStart, (int) titleMarginTop, 0, 0);
            layoutParams.topMargin = (int) titleMarginTop;
            child.setLayoutParams(layoutParams);

            child.setVisibility(View.VISIBLE);
            parent.findViewById(R.id.textViewToolbar).setVisibility(View.INVISIBLE);

            if (isTextViewSubTitleVisible) {
                parent.findViewById(R.id.textViewSubTitle)
                        .animate().alpha(0.0F).setDuration(300).start();
                isTextViewSubTitleVisible = false;
            }

        } else if (scrollPercent >= 0.0 && scrollPercent <= 1.1) {

            //Log.d(LOG_TAG, "-> onDependentViewChanged -> scrollPercent = " + scrollPercent);

            float textSizePx = expandedTitleTextSize;
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> textSizePx = " + textSizePx);
            child.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);

            float titleMarginTop = expandedTitleMarginTop - (expandedY - scrollPosition);
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginTop = " + titleMarginTop);

            float titleMarginStart = expandedTitleMarginStart;

            CoordinatorLayout.LayoutParams layoutParams =
                    (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            layoutParams.setMargins((int) titleMarginStart, (int) titleMarginTop, 0, 0);
            layoutParams.topMargin = (int) titleMarginTop;
            child.setLayoutParams(layoutParams);

            child.setVisibility(View.VISIBLE);
            parent.findViewById(R.id.textViewToolbar).setVisibility(View.INVISIBLE);

            if (!isTextViewSubTitleVisible) {
                parent.findViewById(R.id.textViewSubTitle)
                        .animate().alpha(1.0F).setDuration(300).start();
                isTextViewSubTitleVisible = true;
            }
        }

        return true;
    }
}

