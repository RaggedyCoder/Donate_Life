package com.project.bluepandora.donatelife.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ViewSwitcher;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.adapter.DonationRecordAdapter;
import com.project.bluepandora.donatelife.adapter.ProfileDetailsAdapter;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.widget.CustomButton;
import com.widget.CustomTextView;
import com.widget.ScrollTabHolder;

import java.util.ArrayList;
import java.util.List;

import nineoldandroids.view.ViewHelper;


public class ProfileDetailsFragment extends Fragment implements ScrollTabHolder, AbsListView.OnScrollListener {

    private static final AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();
    int[] origin = new int[2];
    boolean firstTime = false;
    private ImageView mHeaderPicture;
    private View mHeader;
    private LinearLayout tab;
    private int mActionBarHeight;
    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private LinearLayout mHeaderLogo;
    private CustomTextView mHeaderTitle;
    private CustomButton profileButton;
    private ViewSwitcher switcher;
    private CustomButton donationRecordButton;
    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();
    private TypedValue mTypedValue = new TypedValue();
    private SpannableString mSpannableString;
    private View rootView;
    private UserDataSource userDatabase;
    private ArrayList<UserInfoItem> userInfo;
    private ListView mListView;
    private LinearLayout actionbar;
    private ArrayList<String> mListItems;
    private GridView mGridView;
    private Drawable actionbarDrawble;
    private int change;
    private boolean profile = true;
    private boolean record = false;


    public static float clamp(float value, float max, float min) {
        return Math.max(Math.min(value, min), max);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMinHeaderHeight = getResources().getDimensionPixelSize(R.dimen.min_header_height);
        mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
        mMinHeaderTranslation = -mMinHeaderHeight + getActionBarHeight();
        firstTime = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mHeaderPicture = (ImageView) rootView.findViewById(R.id.header_picture);
        mHeader = rootView.findViewById(R.id.header);
        tab = (LinearLayout) rootView.findViewById(R.id.tabs);
        profileButton = (CustomButton) rootView.findViewById(R.id.profile_button);
        donationRecordButton = (CustomButton) rootView.findViewById(R.id.donation_record_button);
        mHeaderLogo = (LinearLayout) rootView.findViewById(R.id.header_logo);
        mHeaderTitle = (CustomTextView) rootView.findViewById(R.id.username);
        switcher = (ViewSwitcher) rootView.findViewById(R.id.view_switcher);
        actionbarDrawble = getResources().getDrawable(
                R.drawable.actionbar_background);
        actionbarDrawble.setAlpha(0);
        actionbar = (LinearLayout) rootView.findViewById(R.id.actionbar);
        actionbar.setBackgroundDrawable(actionbarDrawble);
        mGridView = (GridView) rootView.findViewById(R.id.grid_view);
        mSpannableString = new SpannableString(getString(R.string.app_name));
        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        userDatabase = new UserDataSource(getActivity());
        userDatabase.open();
        userInfo = userDatabase.getAllUserItem();
        userDatabase.close();
        mHeaderTitle.setText(userInfo.get(0).getFirstName());
        ((CustomTextView) rootView.findViewById(R.id.username_title)).setText(userInfo.get(0).getFirstName());
        origin[1] = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 310 - 48 - 4, getActivity().getResources().getDisplayMetrics());
        change = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 310 - 48 - 4, getActivity().getResources().getDisplayMetrics()) - getResources().getDimensionPixelOffset(R.dimen.abc_action_bar_default_height_material));
        mListView = (ListView) rootView.findViewById(R.id.listView);
        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
        View footerView = inflater.inflate(R.layout.view_footer_placeholder, mListView, false);
        mListView.addFooterView(footerView);
        Log.e("origin", origin[1] + "");
        mListView.addHeaderView(placeHolderView);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!profile) {

                    switcher.showPrevious();
                    profile = true;
                    record = false;
                    profileButton.setSelected(true);
                    donationRecordButton.setSelected(false);
                    switcher.setInAnimation(getActivity(), R.anim.to_left);
                    switcher.setOutAnimation(getActivity(), R.anim.out_right);

                }
            }
        });
        donationRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!record) {

                    switcher.showNext();
                    profile = false;
                    record = true;
                    profileButton.setSelected(false);
                    donationRecordButton.setSelected(true);
                    switcher.setInAnimation(getActivity(), R.anim.to_right);
                    switcher.setOutAnimation(getActivity(), R.anim.out_left);
                }
            }
        });
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowTitleEnabled(false);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayHomeAsUpEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setHomeButtonEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowCustomEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setDisplayShowHomeEnabled(true);
        ((ActionBarActivity) getActivity()).getSupportActionBar()
                .setCustomView(inflater.inflate(R.layout.profile_actionbar, null, false));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setOnScrollListener(this);
        mGridView.setOnScrollListener(this);
        List<String> list = new ArrayList<String>();
        list.add(getActivity().getResources().getString(R.string.full_name));
        list.add(getActivity().getResources().getString(R.string.mobile_number));
        list.add(getActivity().getResources().getString(R.string.area_name));
        list.add(getActivity().getResources().getString(R.string.blood_group));
        list.add(getActivity().getResources().getString(R.string.total_donation));
        profileButton.setSelected(true);
        firstTime = false;

        List<String> list2 = new ArrayList<String>();
        list2.add("");
        list2.add("");
        list2.add("");
        list2.add("");
        list2.add("");
        list2.add("");
        list2.add("");

        mGridView.setAdapter(new DonationRecordAdapter(getActivity(), list2));
        mListView.setAdapter(new ProfileDetailsAdapter(getActivity(), userInfo.get(0), list));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
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
        return (LinearLayout) rootView.findViewById(R.id.header_logos);
    }

    private CustomTextView getActionBarTitleView() {
        return (CustomTextView) rootView.findViewById(R.id.username_title);
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }
        mListView.setSelectionFromTop(1, scrollHeight);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        int scrollY = getScrollY(view);

        ViewHelper.setTranslationY(mHeader, Math.max(-scrollY, mMinHeaderTranslation));
        ViewHelper.setTranslationY(tab, Math.max(-scrollY, mMinHeaderTranslation));
        float ratio1 = ((float) Math.min(Math.max(-Math.max(-scrollY, mMinHeaderTranslation), 0),
                change) / change);
        Log.e("ratio", ((float) Math.min(Math.max(-Math.max(-scrollY, mMinHeaderTranslation), 0),
                change) / change) + "");
        actionbarDrawble.setAlpha((int) (ratio1 * 255));
        donationRecordButton.layout((int) donationRecordButton.getX(), origin[1] + Math.max(-scrollY, mMinHeaderTranslation), (int) donationRecordButton.getX() + donationRecordButton.getMeasuredWidth(), (int) origin[1] + donationRecordButton.getMeasuredHeight() + Math.max(-scrollY, mMinHeaderTranslation));
        profileButton.layout((int) profileButton.getX(), origin[1] + Math.max(-scrollY, mMinHeaderTranslation), (int) profileButton.getX() + profileButton.getMeasuredWidth(), (int) origin[1] + profileButton.getMeasuredHeight() + Math.max(-scrollY, mMinHeaderTranslation));
        Log.e("Trans", Math.max(-scrollY, mMinHeaderTranslation) + " " + origin[1]);
        float ratio = clamp(ViewHelper.getTranslationY(mHeader) / mMinHeaderTranslation, 0.0f, 1.0f);
        interpolate(mHeaderLogo, getActionBarIconView(), sSmoothInterpolator.getInterpolation(ratio), -scrollY);
        mHeaderTitle.setTextColor(Color.argb((int)
                ((clamp(5.0F * (1.0f - ratio) - 4.0F, 0.0F, 1.0F)) * 255), 0x2e, 0x2e, 0x2e));
        setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void setTitleAlpha(float alpha) {
        Log.e("alpha", alpha + "");
        getActionBarTitleView().setTextColor(Color.argb((int) (alpha * 255), 0xf1, 0xf1, 0xf1));
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            headerHeight = mHeaderHeight;
        }

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    private RectF getOnScreenRect(RectF rect, View view) {
        rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        return rect;
    }

    private void interpolate(View view1, View view2, float interpolation, int Y) {
        getOnScreenRect(mRect1, view1);
        getOnScreenRect(mRect2, view2);
        float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
        float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
        float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
        float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));
        ViewHelper.setTranslationX(view1, translationX);
        ViewHelper.setTranslationY(view1, translationY);
        ViewHelper.setScaleX(view1, scaleX);
        ViewHelper.setScaleY(view1, scaleY);
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, 1);
    }
}