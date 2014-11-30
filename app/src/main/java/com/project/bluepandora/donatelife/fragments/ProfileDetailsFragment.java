package com.project.bluepandora.donatelife.fragments;
/*
 * Copyright (C) 2014 The Blue Pandora Project Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.adapter.DonationRecordAdapter;
import com.project.bluepandora.donatelife.adapter.ProfileDetailsAdapter;
import com.project.bluepandora.donatelife.application.AppController;
import com.project.bluepandora.donatelife.data.BloodItem;
import com.project.bluepandora.donatelife.data.DRItem;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.BloodDataSource;
import com.project.bluepandora.donatelife.datasource.DRDataSource;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.helpers.DonationRecordHelper;
import com.project.bluepandora.donatelife.helpers.URL;
import com.project.bluepandora.donatelife.volley.CustomRequest;
import com.project.bluepandora.util.Utils;
import com.widget.CustomButton;
import com.widget.CustomEditText;
import com.widget.CustomTextView;
import com.widget.helper.ScrollTabHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nineoldandroids.view.ViewHelper;


public class ProfileDetailsFragment extends Fragment implements ScrollTabHolder, AbsListView.OnScrollListener {

    private static final AccelerateDecelerateInterpolator sSmoothInterpolator = new AccelerateDecelerateInterpolator();
    int[] origin = new int[2];
    boolean firstTime = false;
    DonationRecordAdapter mDonationRecordAdapter;
    private View mHeader;
    private LinearLayout tab;
    private int mActionBarHeight;
    private int mMinHeaderHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    private LinearLayout mHeaderLogo;
    private CustomTextView mHeaderTitle;
    private CustomTextView avatar;
    private CustomButton profileButton;
    private ViewSwitcher switcher;
    private CustomButton donationRecordButton;
    private TypedValue mTypedValue = new TypedValue();
    private View rootView;
    private UserDataSource userDatabase;
    private DRDataSource donationDatabase;
    private ArrayList<UserInfoItem> userInfo;
    private ArrayList<DRItem> donationInfo;
    private ListView mListView;
    private LinearLayout actionbar;
    private GridView donationRecordView;
    private CustomButton addDonationRecord;
    private CustomEditText donationDetails;
    private Drawable actionbarDrawable;
    private int change;
    private Dialog donationAddDialog;
    private DatePicker donationDate;
    private View addDRView;
    private ProgressDialog pd;
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

    @SuppressLint("NewApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        mHeader = rootView.findViewById(R.id.header);
        tab = (LinearLayout) rootView.findViewById(R.id.tabs);
        profileButton = (CustomButton) rootView.findViewById(R.id.profile_button);
        donationRecordButton = (CustomButton) rootView.findViewById(R.id.donation_record_button);
        mHeaderLogo = (LinearLayout) rootView.findViewById(R.id.header_logo);
        avatar = (CustomTextView) rootView.findViewById(R.id.avatar);
        mHeaderTitle = (CustomTextView) rootView.findViewById(R.id.username);
        switcher = (ViewSwitcher) rootView.findViewById(R.id.view_switcher);
        actionbarDrawable = getResources().getDrawable(
                R.drawable.actionbar_background);
        actionbarDrawable.setAlpha(0);
        actionbar = (LinearLayout) rootView.findViewById(R.id.actionbar);
        if (Utils.hasJB()) {
            actionbar.setBackground(actionbarDrawable);

        } else {
            actionbar.setBackgroundDrawable(actionbarDrawable);
        }
        donationRecordView = (GridView) rootView.findViewById(R.id.donation_record_view);
        ((ActionBarActivity) getActivity()).getSupportActionBar().setBackgroundDrawable(null);
        userDatabase = new UserDataSource(getActivity());
        ProgressDialog.Builder donationAddDialogBuilder = new ProgressDialog.Builder(getActivity());
        addDRView = inflater.inflate(R.layout.donation_record, null);
        donationDate = (DatePicker) addDRView.findViewById(R.id.donation_date_picker);
        donationDetails = (CustomEditText) addDRView.findViewById(R.id.donation_details);
        addDonationRecord = (CustomButton) addDRView.findViewById(R.id.donation_add_button);
        donationDate.setMaxDate(System.currentTimeMillis());
        donationAddDialogBuilder.setView(addDRView);
        donationAddDialog = donationAddDialogBuilder.create();
        userDatabase.open();
        userInfo = userDatabase.getAllUserItem();
        userDatabase.close();
        donationDatabase = new DRDataSource(getActivity());
        donationDatabase.open();
        donationInfo = donationDatabase.getAllDRItem();
        donationDatabase.close();
        userInfo.get(0).setTotalDonation(donationInfo.size() + "");
        mHeaderTitle.setText(userInfo.get(0).getFirstName());
        ((CustomTextView) rootView.findViewById(R.id.username_title)).setText(userInfo.get(0).getFirstName());
        origin[1] = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                310 - 48 - 4, getActivity().getResources().getDisplayMetrics());
        change = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                310 - 48 - 4, getActivity().getResources().getDisplayMetrics()) - getResources().
                getDimensionPixelOffset(R.dimen.abc_action_bar_default_height_material));
        mListView = (ListView) rootView.findViewById(R.id.listView);
        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
        View footerView = inflater.inflate(R.layout.view_footer_placeholder, mListView, false);
        mListView.addFooterView(footerView);
        Log.e("origin", origin[1] + "");
        mListView.addHeaderView(placeHolderView);
        profile = true;
        record = false;
        switcher.setInAnimation(getActivity(), R.anim.to_left);
        switcher.setOutAnimation(getActivity(), R.anim.out_right);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!profile) {
                    switcher.showPrevious();
                    profile = true;
                    record = false;
                    profileButton.setSelected(true);
                    donationRecordButton.setSelected(false);
                    mListView.smoothScrollToPositionFromTop(0, -DonationRecordHelper.getScrollY(donationRecordView, mHeaderHeight), 0);
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
                    donationRecordView.smoothScrollToPositionFromTop(0, -
                            DonationRecordHelper.getScrollY(mListView, mHeaderHeight), 0);
                    Log.e("TAG", DonationRecordHelper.getScrollY(mListView, mHeaderHeight) + " " +
                            DonationRecordHelper.getScrollY(donationRecordView, mHeaderHeight));

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
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setOnScrollListener(this);
        donationRecordView.setOnScrollListener(this);
        List<String> list = new ArrayList<String>();
        list.add(getActivity().getResources().getString(R.string.full_name));
        list.add(getActivity().getResources().getString(R.string.mobile_number));
        list.add(getActivity().getResources().getString(R.string.area_name));
        list.add(getActivity().getResources().getString(R.string.blood_group));
        list.add(getActivity().getResources().getString(R.string.total_donation));
        BloodDataSource database = new BloodDataSource(getActivity());
        database.open();
        avatar.setText(database.getBloodItem(new BloodItem("", userInfo.get(0).getGroupId())).getBloodName());
        database.close();
        profileButton.setSelected(true);
        firstTime = false;
        DonationRecordHelper.sort(donationInfo);
        if (donationInfo.size() == 0) {
            donationInfo.add(new DRItem("0", "0"));
            donationInfo.add(new DRItem("0", "0"));
            donationInfo.add(new DRItem("z", "0"));
        } else {
            donationInfo.add(0, new DRItem("0", "0"));
            donationInfo.add(0, new DRItem("0", "0"));
            donationInfo.add(new DRItem("z", "0"));
        }
        addDonationRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //yyyy-MM-dd HH:mm:ss
                String reqTime = "" + donationDate.getYear() + "-" + donationDate.getMonth() + "-"
                        + donationDate.getDayOfMonth() + " " + "00:00:00";
                if (DonationRecordHelper.search(donationInfo, reqTime)) {
                    createAlertDialog("Impossible");
                    return;
                } else {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put(URL.REQUEST_NAME, URL.ADD_DONATIONRECORD_REQUEST);
                    params.put(URL.DONATION_DATE_PARAM, reqTime);
                    params.put(URL.DONATION_DETAILS_PARAM, donationDetails.getText().toString());
                    params.put(URL.MOBILE_TAG, userInfo.get(0).getMobileNumber());
                    createProgressDialog();
                    pd.show();
                    addDonationRecord(params);
                    return;
                }
            }
        });
        donationRecordView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (donationRecordView.getCount() == (position + 1)) {
                    donationAddDialog.show();
                    Log.e("TAG", donationDate.getDayOfMonth() + "");
                }

            }
        });
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < donationInfo.size(); i++) {
            try {
                Log.e("TIME", isoFormat.parse(donationInfo.get(i).getDonationTime().replace(".0", "")).getTime() + "");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mDonationRecordAdapter = new DonationRecordAdapter(getActivity(), donationInfo);
        donationRecordView.setAdapter(mDonationRecordAdapter);
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

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount, int pagePosition) {
        int scrollY = DonationRecordHelper.getScrollY(view, mHeaderHeight);
        change(scrollY);
    }

    private void change(int scrollY) {
        ViewHelper.setTranslationY(mHeader, Math.max(-scrollY, mMinHeaderTranslation));
        ViewHelper.setTranslationY(tab, Math.max(-scrollY, mMinHeaderTranslation));
        float ratio1 = ((float) Math.min(Math.max(-Math.max(-scrollY, mMinHeaderTranslation), 0),
                change) / change);
        Log.e("ratio", ((float) Math.min(Math.max(-Math.max(-scrollY, mMinHeaderTranslation), 0),
                change) / change) + "");
        actionbarDrawable.setAlpha((int) (ratio1 * 255));
        donationRecordButton.layout(donationRecordButton.getLeft(),
                origin[1] + Math.max(-scrollY,
                        mMinHeaderTranslation),
                donationRecordButton.getLeft() + donationRecordButton.getMeasuredWidth(),
                (origin[1] + donationRecordButton.getMeasuredHeight() + Math.max(-scrollY, mMinHeaderTranslation)));
        profileButton.layout((int) profileButton.getLeft(), origin[1] + Math.max(-scrollY, mMinHeaderTranslation), (int) profileButton.getX() + profileButton.getMeasuredWidth(), (int) origin[1] + profileButton.getMeasuredHeight() + Math.max(-scrollY, mMinHeaderTranslation));
        Log.e("Trans", Math.max(-scrollY, mMinHeaderTranslation) + " " + origin[1]);
        float ratio = clamp(ViewHelper.getTranslationY(mHeader) / mMinHeaderTranslation, 0.0f, 1.0f);
        DonationRecordHelper.interpolate(mHeaderLogo, getActionBarIconView(), sSmoothInterpolator.getInterpolation(ratio));
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


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        this.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, 1);
    }

    private void addDonationRecord(final HashMap<String, String> params) {

        CustomRequest jsonReq = new CustomRequest(Request.Method.POST, URL.URL, params,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getActivity(), response.toString(),
                                Toast.LENGTH_LONG).show();
                        try {
                            if (response.getInt("done") == 1) {
                                createAlertDialog(response.getString("message"));
                                DRItem item = new DRItem();
                                item.setDonationDetails(donationDetails.getText().toString());
                                //yyyy-MM-dd HH:mm:ss
                                String reqTime = "" + donationDate.getYear() + "-" + donationDate.getMonth() + "-"
                                        + donationDate.getDayOfMonth() + " " + "00:00:00";
                                item.setDonationTime(reqTime);
                                donationDatabase.open();
                                donationInfo.add(donationInfo.size() - 1, donationDatabase.createDRItem(reqTime, donationDetails.getText().toString()));
                                for (DRItem itemq : donationInfo) {
                                    Log.e("r", itemq.getDonationTime());
                                }
                                donationInfo.remove(0);
                                donationInfo.remove(0);
                                donationInfo.remove(donationInfo.size() - 1);
                                for (DRItem itemq : donationInfo) {
                                    Log.e("rem", itemq.getDonationTime());
                                }
                                DonationRecordHelper.sort(donationInfo);
                                for (DRItem itemq : donationInfo) {
                                    Log.e("s", itemq.getDonationTime());
                                }
                                donationInfo.add(0, new DRItem("0", "0"));
                                donationInfo.add(0, new DRItem("0", "0"));
                                donationInfo.add(new DRItem("z", "z"));
                                for (DRItem itemq : donationInfo) {
                                    Log.e("sor", itemq.getDonationTime());
                                }
                                mDonationRecordAdapter.notifyDataSetChanged();
                                donationDatabase.close();
                            } else {
                                createAlertDialog(response.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        pd.dismiss();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                if (error.toString().contains("TimeoutError")) {
                    createAlertDialog(getResources().getString(R.string.TimeoutError));
                } else if (error.toString().contains("UnknownHostException")) {
                    createAlertDialog(getResources().getString(R.string.NoInternet));
                }
            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private void createProgressDialog() {
        pd = new ProgressDialog(getActivity());
        pd.setMessage(getActivity().getResources().getString(R.string.processing));
        pd.setIndeterminate(false);
        pd.setCancelable(false);
        pd.show();
    }

    private void createAlertDialog(String message) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                donationAddDialog.dismiss();
            }
        });
        alertDialog.show();
    }
}