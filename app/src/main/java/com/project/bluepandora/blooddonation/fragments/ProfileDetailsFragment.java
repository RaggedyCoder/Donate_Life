package com.project.bluepandora.blooddonation.fragments;

import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.util.SparseArrayCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.LinearLayout;

import com.flavienlaurent.notboringactionbar.AlphaForegroundColorSpan;
import com.flavienlaurent.notboringactionbar.KenBurnsSupportView;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.util.Utils;
import com.widget.PagerSlidingTabStrip;
import com.widget.ScrollTabHolder;

import nineoldandroids.view.ViewHelper;


public class ProfileDetailsFragment extends Fragment implements ScrollTabHolder, ViewPager.OnPageChangeListener {

    private static AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();

    private KenBurnsSupportView mHeaderPicture;
    private View mHeader;

    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private ViewPager mViewPager;
    private PagerAdapter mPagerAdapter;

    private int mActionBarHeight;
    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private LinearLayout mHeaderLogo;

    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();

    private TypedValue mTypedValue = new TypedValue();
    private SpannableString mSpannableString;
    private AlphaForegroundColorSpan mAlphaForegroundColorSpan;

    private View rootView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mHeaderPicture = (KenBurnsSupportView) rootView.findViewById(R.id.header_picture);
        mHeaderPicture.setResourceIds(R.drawable.gradient_background);
        mHeaderLogo = (LinearLayout) rootView.findViewById(R.id.header_logo);
        mHeader = rootView.findViewById(R.id.header);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) rootView.findViewById(R.id.tabs);
        mViewPager = (ViewPager) rootView.findViewById(R.id.pager);
        mViewPager.setOffscreenPageLimit(2);
        mPagerAdapter = new PagerAdapter(getActivity().getSupportFragmentManager());
        mPagerAdapter.setTabHolderScrollingContent(this);
        mViewPager.setAdapter(mPagerAdapter);
        mPagerSlidingTabStrip.setViewPager(mViewPager);
        mPagerSlidingTabStrip.setOnPageChangeListener(this);
        mSpannableString = new SpannableString(getString(R.string.app_name));
        mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(0xffffffff);
        ViewHelper.setAlpha(getActionBarIconView(), 0f);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        return rootView;
    }

    public int getActionBarHeight() {
        if (mActionBarHeight != 0) {
            return mActionBarHeight;
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            getActivity().getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        } else {
            getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, mTypedValue, true);
        }

        mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data,
                getResources().getDisplayMetrics());

        return mActionBarHeight;
    }

    private LinearLayout getActionBarIconView() {
        if (Utils.hasHoneycomb()) {
            return (LinearLayout) rootView.findViewById(android.R.id.home);
        }
        return (LinearLayout) rootView.findViewById(android.support.v7.appcompat.R.id.home);
    }

    @Override
    public void onPageScrolled(int i, float v, int i2) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void adjustScroll(int scrollHeight) {

    }

    @Override
    public void onScroll(View view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {

    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private final String[] TITLES = {"Profile", "Donation Record"};
        private SparseArrayCompat<ScrollTabHolder> mScrollTabHolders;
        private ScrollTabHolder mListener;

        public PagerAdapter(FragmentManager fm) {
            super(fm);
            mScrollTabHolders = new SparseArrayCompat<ScrollTabHolder>();
        }

        public void setTabHolderScrollingContent(ScrollTabHolder listener) {
            mListener = listener;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }

        @Override
        public Fragment getItem(int position) {
            ScrollTabHolderFragment fragment = (ScrollTabHolderFragment) null;
            mScrollTabHolders.put(position, fragment);
            if (mListener != null) {
                fragment.setScrollTabHolder(mListener);
            }
            return fragment;
        }

        public SparseArrayCompat<ScrollTabHolder> getScrollTabHolders() {
            return mScrollTabHolders;
        }
    }
}
