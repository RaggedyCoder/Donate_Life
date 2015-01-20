package com.project.bluepandora.donatelife.fragments;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.activities.MainActivity;
import com.project.bluepandora.donatelife.adapter.SmallMenuAdapter;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.SMItem;

import java.util.ArrayList;
import java.util.List;


public class SmallMenuFragment extends Fragment implements MainActivity.SlidePaneListeners {

    private SmallMenuAdapter smallMenuAdapter;
    private LinearLayout smallMenuLayout;
    private List<Item> feedItems;
    private ListView menuListView;
    private View rootView;

    public SmallMenuFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_small_menu, container, false);

        }
        feedItems = new ArrayList<Item>();
        TypedArray icons = getResources().obtainTypedArray(R.array.small_menu_button_images);
        for (int i = 0; i < icons.length(); i++) {
            SMItem item = new SMItem();
            Log.e("LIST res", icons.getResourceId(i, -1) + "");
            item.setButtonImage(icons.getResourceId(i, -1));
            feedItems.add(item);
        }
        smallMenuAdapter = new SmallMenuAdapter(getActivity(), feedItems);
        menuListView = (ListView) rootView.findViewById(R.id.small_menu_list_view);
        menuListView.setAdapter(smallMenuAdapter);
        Log.e("LIST", menuListView.getCount() + "");
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onPaneSlide(float offset) {
        final float ratio = 1 - offset;
        rootView.setAlpha(ratio);
        if (ratio == 0) {
            rootView.setVisibility(View.GONE);
        } else {
            rootView.setVisibility(View.VISIBLE);
        }
        Log.e("Alpha", offset + "");
    }
}
