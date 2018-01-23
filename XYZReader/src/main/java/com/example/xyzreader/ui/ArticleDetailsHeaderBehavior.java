package com.example.xyzreader.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.xyzreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Hrishikesh Kadam on 22/01/2018
 */

public class ArticleDetailsHeaderBehavior extends AppBarLayout.ScrollingViewBehavior {

    private static final String LOG_TAG = ArticleDetailsHeaderBehavior.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.textViewToolbar)
    TextView textViewToolbar;

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;

    @BindView(R.id.textViewSubTitle)
    TextView textViewSubTitle;

    private float imageViewHeight;
    private float keyline1;

    private float expandedY;
    private float collapsedY;
    private float differenceScrollY;

    private float shrinkExpandedY;
    private float shrinkCollapsedY;
    private float differenceShrinkY;

    private float expandedTitleTextSize;
    private float collapsedTitleTextSize;
    private float differenceTitleTextSize;

    private float expandedTitleMarginStart;
    private float collapsedTitleMarginStart;
    private float differenceTitleMarginStart;

    private float expandedTitleMarginTop;
    private float collapsedTitleMarginTop;
    private float differenceTitleMarginTop;

    private boolean wasSubTitleVisible = true;

    public ArticleDetailsHeaderBehavior() {
        super();
    }

    public ArticleDetailsHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        imageViewHeight = context.getResources().
                getDimension(R.dimen.article_details_imageView_height);

        keyline1 = context.getResources().getDimension(R.dimen.keyline_1);

        expandedY = imageViewHeight;
        collapsedY = 0;
        differenceScrollY = expandedY - collapsedY;

        int[] actionBarAttrs = {android.R.attr.actionBarSize};
        TypedArray typedArray = context.obtainStyledAttributes(actionBarAttrs);

        shrinkExpandedY = typedArray.getDimension(0, 0F);
        shrinkCollapsedY = 0;
        differenceShrinkY = shrinkExpandedY - shrinkCollapsedY;

        int[] textViewToolbarAttrs = {android.R.attr.textSize};
        typedArray = context.obtainStyledAttributes(
                R.style.TextAppearance_AppCompat_Widget_ActionBar_Title, textViewToolbarAttrs);

        expandedTitleTextSize = context.getResources()
                .getDimension(R.dimen.article_details_textViewTitle_textSize);
        collapsedTitleTextSize = typedArray.getDimension(0, 0F);
        differenceTitleTextSize = expandedTitleTextSize - collapsedTitleTextSize;

        typedArray.recycle();
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, View child, int parentWidthMeasureSpec,
                                  int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        //Log.d(LOG_TAG, "-> onMeasureChild");

        ButterKnife.bind(this, parent);

        expandedTitleMarginTop = imageViewHeight + keyline1;
        // TODO optimise if possible
        collapsedTitleMarginTop = textViewToolbar.getY();
        //Log.d(LOG_TAG, "-> onMeasureChild -> collapsedTitleMarginTop = " + collapsedTitleMarginTop);
        differenceTitleMarginTop = expandedTitleMarginTop - collapsedTitleMarginTop;

        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed,
                parentHeightMeasureSpec, heightUsed);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        //Log.d(LOG_TAG, "-> layoutDependsOn");

        if (dependency.getId() == R.id.appBarLayout) {

            textViewSubTitle = parent.findViewById(R.id.textViewSubTitle);
            textViewToolbar = parent.findViewById(R.id.textViewToolbar);

            expandedTitleMarginStart = textViewSubTitle.getX();
            // TODO optimise if possible
            collapsedTitleMarginStart = textViewToolbar.getX();
            //Log.d(LOG_TAG, "-> onMeasureChild -> collapsedTitleMarginStart = " + collapsedTitleMarginStart);
            differenceTitleMarginStart = collapsedTitleMarginStart - expandedTitleMarginStart;
        }

        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {

        float scrollPosition = expandedY + dependency.getY();
        float scrollPercent = ((collapsedY + scrollPosition) / differenceScrollY);
        //Log.v(LOG_TAG, "-> onDependentViewChanged -> scrollPercent = " + scrollPercent);
        float shrinkPercent = ((shrinkCollapsedY + scrollPosition) / differenceShrinkY);
        //Log.v(LOG_TAG, "-> onDependentViewChanged -> shrinkPercent = " + shrinkPercent);

        boolean shouldSubTitleBeVisible = true;
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) textViewTitle.getLayoutParams();
        float textSizePx = expandedTitleTextSize;

        if (scrollPercent < 0) {

            textSizePx = collapsedTitleTextSize;

            textViewTitle.setVisibility(View.INVISIBLE);
            textViewToolbar.setVisibility(View.VISIBLE);
            shouldSubTitleBeVisible = false;
        }

        if (scrollPercent >= 0 && scrollPercent <= 1) {
            //Log.v(LOG_TAG, "-> onDependentViewChanged -> scrollPercent = " + scrollPercent);

            textSizePx = expandedTitleTextSize;

            float titleMarginTop = expandedTitleMarginTop + dependency.getY();
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginTop = " + titleMarginTop);

            layoutParams.setMargins(
                    (int) expandedTitleMarginStart, (int) titleMarginTop, (int) keyline1, 0);
            layoutParams.setMarginStart((int) expandedTitleMarginStart);

            textViewTitle.setVisibility(View.VISIBLE);
            textViewToolbar.setVisibility(View.INVISIBLE);
            shouldSubTitleBeVisible = true;
        }

        if (shrinkPercent >= 0 && shrinkPercent <= 1) {
            //Log.v(LOG_TAG, "-> onDependentViewChanged -> shrinkPercent = " + shrinkPercent);

            textSizePx =
                    collapsedTitleTextSize + (differenceTitleTextSize * shrinkPercent);
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> textSizePx = " + textSizePx);

            float titleMarginStart =
                    collapsedTitleMarginStart - (differenceTitleMarginStart * shrinkPercent);
            //Log.d(LOG_TAG, "-> onDependentViewChanged -> titleMarginStart = " + titleMarginStart);

            layoutParams.leftMargin = (int) titleMarginStart;
            layoutParams.setMarginStart((int) titleMarginStart);
            shouldSubTitleBeVisible = false;
        }

        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);
        textViewTitle.setLayoutParams(layoutParams);

        if (shouldSubTitleBeVisible && !wasSubTitleVisible) {

            textViewSubTitle.animate().cancel();
            textViewSubTitle.animate().alpha(1.0F).setDuration(300).start();
            wasSubTitleVisible = true;

        } else if (!shouldSubTitleBeVisible && wasSubTitleVisible) {

            textViewSubTitle.animate().cancel();
            textViewSubTitle.animate().alpha(0.0F).setDuration(300).start();
            wasSubTitleVisible = false;
        }

        return super.onDependentViewChanged(parent, child, dependency);
    }
}
