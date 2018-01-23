package com.example.xyzreader.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.xyzreader.R;

/**
 * Created by Hrishikesh Kadam on 12/01/2018
 */

public class CustomBehavior extends CoordinatorLayout.Behavior<ImageView> {

    private static final String LOG_TAG = CustomBehavior.class.getSimpleName();
    private float collapsed_layout_height;
    private float collapsed_layout_width;
    private float expanded_layout_height;
    private float expanded_layout_width;
    private float collapsed_scroll_position;
    private float expanded_scroll_position;
    private float scroll_position_difference;
    private float collapsed_margin_start;
    private float layout_height_difference;
    private float layout_width_difference;
    private float margin_start_difference;
    private float expanded_margin_start;

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.v(LOG_TAG, "-> constructor");

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //Log.i(LOG_TAG, "-> " + displayMetrics.toString());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageViewLogo);

        collapsed_layout_height = typedArray.getDimension(
                R.styleable.ImageViewLogo_collapsed_layout_height, 0F);
        collapsed_layout_width = typedArray.getDimension(
                R.styleable.ImageViewLogo_collapsed_layout_width, 0F);

        expanded_layout_height = typedArray.getDimension(
                R.styleable.ImageViewLogo_android_layout_height, 0F);
        layout_height_difference = expanded_layout_height - collapsed_layout_height;

        expanded_layout_width = typedArray.getDimension(
                R.styleable.ImageViewLogo_android_layout_width, 0F);
        layout_width_difference = expanded_layout_width - collapsed_layout_width;

        collapsed_margin_start = typedArray.getDimension(
                R.styleable.ImageViewLogo_collapsed_margin_start, 0F);
        expanded_margin_start = (displayMetrics.widthPixels / 2) - (expanded_layout_width / 2);
        margin_start_difference = expanded_margin_start - collapsed_margin_start;

        collapsed_scroll_position = typedArray.getDimension(
                R.styleable.ImageViewLogo_collapsed_scroll_position, 0F);
        expanded_scroll_position = typedArray.getDimension(
                R.styleable.ImageViewLogo_expanded_scroll_position, 0F);
        scroll_position_difference = expanded_scroll_position - collapsed_scroll_position;

        typedArray.recycle();
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ImageView child, View dependency) {
        return dependency.getId() == R.id.swipe_refresh_layout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ImageView child, View dependency) {

        float current_scroll_position = dependency.getY();
        float current_scroll_position_percent =
                ((current_scroll_position - collapsed_scroll_position)
                        / scroll_position_difference);
        //Log.d(LOG_TAG, "-> onDependentViewChanged -> " + current_scroll_position_percent);

        float current_layout_height =
                collapsed_layout_height + (layout_height_difference * current_scroll_position_percent);

        float current_margin_top = (current_scroll_position / 2) - (current_layout_height / 2);

        float current_layout_width =
                collapsed_layout_width + (layout_width_difference * current_scroll_position_percent);

        float current_margin_start =
                collapsed_margin_start + (margin_start_difference * current_scroll_position_percent);

        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) child.getLayoutParams();

        layoutParams.topMargin = (int) current_margin_top;
        layoutParams.setMarginStart((int) current_margin_start);
        layoutParams.height = (int) current_layout_height;
        layoutParams.width = (int) current_layout_width;

        child.setLayoutParams(layoutParams);

        return true;
    }
}
