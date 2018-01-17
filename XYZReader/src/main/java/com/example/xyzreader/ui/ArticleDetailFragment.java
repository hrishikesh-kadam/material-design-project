package com.example.xyzreader.ui;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Article detail screen. This fragment is
 * either contained in a {@link ArticleListActivity} in two-pane mode (on
 * tablets) or a {@link ArticleDetailActivity} on handsets.
 */
public class ArticleDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>,
        NestedScrollView.OnScrollChangeListener {

    public static final String ARG_ITEM_ID = "item_id";
    private static final String LOG_TAG = ArticleDetailFragment.class.getSimpleName();

    @BindView(R.id.textViewToolbar)
    TextView textViewToolbar;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewSubTitle)
    TextView textViewSubTitle;
    @BindView(R.id.textViewBody)
    TextView textViewBody;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.nestedScrollView)
    NestedScrollView nestedScrollView;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @Nullable
    @BindView(R.id.viewHeaderBeneath)
    View viewHeaderBeneath;
    @BindView(R.id.viewHeader)
    View viewHeader;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsingToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    private Cursor mCursor;
    private long mItemId;
    private int position;
    private View mRootView;
    private int vibrantColor = 0;
    private int darkVibrantColor = 0;
    private IsThisFragmentSelectedListener isThisFragmentSelectedListener;
    //TODO is mIsCard required
    private boolean mIsCard = false;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss");
    // Use default locale format
    private SimpleDateFormat outputFormat = new SimpleDateFormat();
    // Most time functions can only handle 1902 - 2037
    private GregorianCalendar START_OF_EPOCH = new GregorianCalendar(2, 1, 1);

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ArticleDetailFragment() {
    }

    public static ArticleDetailFragment newInstance(long itemId, int position) {
        Log.v(LOG_TAG, "-> newInstance");

        Bundle arguments = new Bundle();
        arguments.putLong(ARG_ITEM_ID, itemId);
        arguments.putInt("position", position);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(LOG_TAG, "-> onCreate");

        if (getArguments() != null) {

            if (getArguments().containsKey(ARG_ITEM_ID))
                mItemId = getArguments().getLong(ARG_ITEM_ID);

            if (getArguments().containsKey("position"))
                position = getArguments().getInt("position");
        }

        mIsCard = getResources().getBoolean(R.bool.detail_is_card);

        //TODO check is setHasOptionsMenu required
        //setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(LOG_TAG, "-> onActivityCreated");

        // In support library r8, calling initLoader for a fragment in a FragmentPagerAdapter in
        // the fragment's onCreate may cause the same LoaderManager to be dealt to multiple
        // fragments because their mIndex is -1 (haven't been added to the activity yet). Thus,
        // we do this in onActivityCreated.
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "-> onCreateView");

        mRootView = inflater.inflate(R.layout.fragment_article_detail, container, false);
        ButterKnife.bind(this, mRootView);

        isThisFragmentSelectedListener = (IsThisFragmentSelectedListener) getActivity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && viewHeaderBeneath != null)
            nestedScrollView.setOnScrollChangeListener(this);

        webView.getSettings().setDefaultFontSize(getResources().getInteger(R.integer.webViewFontSize));

//        mRootView.findViewById(R.id.share_fab).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
//                        .setType("text/plain")
//                        .setText("Some sample text")
//                        .getIntent(), getString(R.string.action_share)));
//            }
//        });

        bindViews();
        return mRootView;
    }

    private Date parsePublishedDate() {

        try {
            String date = mCursor.getString(ArticleLoader.Query.PUBLISHED_DATE);
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Log.e(LOG_TAG, ex.getMessage());
            Log.i(LOG_TAG, "passing today's date");
            return new Date();
        }
    }

    private void bindViews() {
        Log.v(LOG_TAG, "-> bindViews");

        if (mRootView == null) {
            return;
        }

        if (mCursor != null) {

            mRootView.setAlpha(0);
            mRootView.setVisibility(View.VISIBLE);
            mRootView.animate().alpha(1);

            textViewToolbar.setText(mCursor.getString(ArticleLoader.Query.TITLE));
            textViewTitle.setText(mCursor.getString(ArticleLoader.Query.TITLE));

            Date publishedDate = parsePublishedDate();

            if (!publishedDate.before(START_OF_EPOCH.getTime())) {
                textViewSubTitle.setText(Html.fromHtml(
                        DateUtils.getRelativeTimeSpanString(
                                publishedDate.getTime(),
                                System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                                DateUtils.FORMAT_ABBREV_ALL).toString()
                                + " by <font color='#ffffff'>"
                                + mCursor.getString(ArticleLoader.Query.AUTHOR)
                                + "</font>"));

            } else {
                // If date is before 1902, just show the string
                textViewSubTitle.setText(Html.fromHtml(
                        outputFormat.format(publishedDate) + " by <font color='#ffffff'>"
                                + mCursor.getString(ArticleLoader.Query.AUTHOR)
                                + "</font>"));

            }

            //String bodyText = mCursor.getString(ArticleLoader.Query.BODY);
            //textViewBody.setText(mCursor.getString(ArticleLoader.Query.BODY));
            //textViewBody.setText(Html.fromHtml(mCursor.getString(ArticleLoader.Query.BODY).replaceAll("(\r\n|\n)", "<br />")));

            webView.loadData(mCursor.getString(ArticleLoader.Query.BODY).replaceAll("(\r\n|\n)", "<br />"),
                    "text/html", "UTF-8");
            webView.setBackgroundColor(0);
            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView view, int newProgress) {
                    super.onProgressChanged(view, newProgress);
                    //Log.d(LOG_TAG, "-> onProgressChanged -> newProgress = " + newProgress);

                    if (newProgress == 100)
                        textViewBody.setVisibility(View.INVISIBLE);
                }
            });

            //TODO check font of webView

            ImageLoaderHelper.getInstance(getActivity()).getImageLoader()
                    .get(mCursor.getString(ArticleLoader.Query.PHOTO_URL), new ImageLoader.ImageListener() {

                        @Override
                        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {

                            Bitmap bitmap = imageContainer.getBitmap();

                            if (bitmap != null) {

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                                    Palette palette = Palette.from(bitmap).generate();
                                    vibrantColor = palette.getVibrantColor(getResources().getColor(R.color.colorPrimary));
                                    darkVibrantColor = palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimaryDark));

                                    viewHeader.setBackgroundColor(vibrantColor);
                                    if (viewHeaderBeneath != null)
                                        viewHeaderBeneath.setBackgroundColor(vibrantColor);

                                    collapsingToolbarLayout.setContentScrimColor(vibrantColor);

                                    setStatusBarColor();
                                }

                                imageView.setImageBitmap(imageContainer.getBitmap());
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                        }
                    });

        } else {
            mRootView.setVisibility(View.GONE);
            textViewTitle.setText("N/A");
            textViewSubTitle.setText("N/A");
        }
    }

    public void setStatusBarColor() {
        //Log.d(LOG_TAG, "-> setStatusBarColor -> position = " + position);

        if (isThisFragmentSelectedListener == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                vibrantColor != 0 &&
                isThisFragmentSelectedListener.isThisFragmentSelected(position)) {

            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(darkVibrantColor);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "-> onCreateLoader");

        return ArticleLoader.newInstanceForItemId(getActivity(), mItemId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.v(LOG_TAG, "-> onLoadFinished");

        if (!isAdded()) {
            Log.e(LOG_TAG, "-> onLoadFinished -> fragment not yet added");
            if (cursor != null) {
                cursor.close();
            }
            return;
        }

        mCursor = cursor;
        if (mCursor != null && !mCursor.moveToFirst()) {
            Log.e(LOG_TAG, "-> Error reading item detail cursor");
            mCursor.close();
            mCursor = null;
        }

        bindViews();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.v(LOG_TAG, "-> onLoaderReset");

        mCursor = null;
        bindViews();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Log.d(LOG_TAG, "-> onScrollChange -> scrollY = " + scrollY);

        if (scrollY >= viewHeaderBeneath.getHeight())
            appBarLayout.setElevation(8.0F);
        else
            appBarLayout.setElevation(0.0F);
    }

    public interface IsThisFragmentSelectedListener {
        boolean isThisFragmentSelected(int position);
    }
}
