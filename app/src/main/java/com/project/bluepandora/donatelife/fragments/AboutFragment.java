package com.project.bluepandora.donatelife.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.AboutActivity;
import com.project.bluepandora.donatelife.adapter.AboutAdapter;
import com.project.bluepandora.donatelife.data.AboutItem;

import java.util.ArrayList;
import java.util.List;

import nineoldandroids.animation.Animator;

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
public class AboutFragment extends ListFragment implements AboutActivity.BackPressImp {

    private static boolean licenseView = false;
    private List<AboutItem> aboutItems;
    private View rootView;
    private AboutAdapter mAdapter;
    private WebView wv;
    private Animation animOpen;
    private Animation animClose;
    private Dialog versionDialog;
    private Dialog developerDialog;
    private Animator.AnimatorListener mAnimatorListener;

    public AboutFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.about_list, container, false);
        aboutItems = new ArrayList<AboutItem>();
        String[] header = getResources().getStringArray(R.array.about_list_header);
        String[] body = getResources().getStringArray(R.array.about_list_body);
        for (int i = 0; i < header.length; i++) {
            AboutItem aboutItem = new AboutItem();
            aboutItem.setHeader(header[i]);
            aboutItem.setBody(body[i]);
            aboutItems.add(aboutItem);
        }
        mAdapter = new AboutAdapter(getActivity(), aboutItems);
        animOpen = AnimationUtils.loadAnimation(getActivity(), R.anim.dim_grow);
        animClose = AnimationUtils.loadAnimation(getActivity(), R.anim.dim);
        versionDialog = new Dialog(getActivity());
        versionDialog.setTitle(R.string.app_info);
        versionDialog.setContentView(R.layout.version_info);
        developerDialog = new Dialog(getActivity());
        developerDialog.setTitle(R.string.about_developer);
        developerDialog.setContentView(R.layout.developer_info);
        wv = (WebView) rootView.findViewById(R.id.license_view);
        if (getViewVisibility(wv)) {
            licenseView = true;
        } else {
            licenseView = false;
        }
        wv.loadUrl("file:///android_asset/licences.html");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getListView().setAdapter(mAdapter);
        animClose.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                wv.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    versionDialog.show();
                } else if (position == 4) {
                    wv.setVisibility(View.VISIBLE);
                    wv.startAnimation(animOpen);
                    licenseView = true;
                } else if (position == 5) {
                    developerDialog.show();
                }
            }
        });
    }

    @Override
    public boolean onBackPressed() {
        if (wv.getVisibility() == View.VISIBLE) {
            licenseView = false;
            wv.startAnimation(animClose);
        } else {
            licenseView = true;
        }
        return licenseView;
    }

    private boolean getViewVisibility(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }
}
