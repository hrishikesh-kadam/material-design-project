package com.example.xyzreader.ui;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.xyzreader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final String LOG_TAG = TestActivity.class.getSimpleName();

    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.textViewToolbar)
    TextView textViewToolbar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewHeader)
    View viewHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "-> onCreate");

        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        appBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

//        float currentScrollPosition = (appBarLayout.getHeight() + verticalOffset);
//        //Log.d(LOG_TAG, "-> onOffsetChanged -> " + currentScrollPosition);
//
//        if (currentScrollPosition <= viewHeader.getHeight())
//            textViewToolbar.setVisibility(View.VISIBLE);
//        else
//            textViewToolbar.setVisibility(View.INVISIBLE);
    }
}
