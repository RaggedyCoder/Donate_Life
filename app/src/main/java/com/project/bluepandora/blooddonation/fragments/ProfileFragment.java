package com.project.bluepandora.blooddonation.fragments;
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

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.project.bluepandora.blooddonation.adapter.ProfileDetailsAdapter;
import com.project.bluepandora.blooddonation.data.UserInfoItem;
import com.project.bluepandora.blooddonation.datasource.UserDataSource;
import com.project.bluepandora.donatelife.R;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends ScrollTabHolderFragment implements AbsListView.OnScrollListener {

    private UserDataSource userDatabase;
    private ArrayList<UserInfoItem> userInfo;
    private ListView mListView;
    private ArrayList<String> mListItems;
    private int mPosition;

    public static Fragment newInstance() {
        return new ProfileFragment();
    }
    public ProfileFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list, container,
                false);
        mPosition = 0;
        userDatabase = new UserDataSource(getActivity());
        userDatabase.open();
        userInfo = userDatabase.getAllUserItem();
        userDatabase.close();
        mListView = (ListView) rootView.findViewById(R.id.listView);
        View placeHolderView = inflater.inflate(R.layout.view_header_placeholder, mListView, false);
        mListView.addHeaderView(placeHolderView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setOnScrollListener(this);
        List<String> list = new ArrayList<String>();
        list.add(getActivity().getResources().getString(R.string.full_name));
        list.add(getActivity().getResources().getString(R.string.mobile_number));
        list.add(getActivity().getResources().getString(R.string.area_name));
        list.add(getActivity().getResources().getString(R.string.blood_group));
        list.add(getActivity().getResources().getString(R.string.total_donation));
        mListView.setAdapter(new ProfileDetailsAdapter(getActivity(), userInfo.get(0), list));
    }

    @Override
    public void adjustScroll(int scrollHeight) {
        if (scrollHeight == 0 && mListView.getFirstVisiblePosition() >= 1) {
            return;
        }

        mListView.setSelectionFromTop(1, scrollHeight);

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mScrollTabHolder != null)
            mScrollTabHolder.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount, mPosition);
    }
}
