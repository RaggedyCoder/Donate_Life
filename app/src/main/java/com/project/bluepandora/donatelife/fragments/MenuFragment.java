package com.project.bluepandora.donatelife.fragments;

import android.app.Activity;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.project.bluepandora.donatelife.R;
import com.project.bluepandora.donatelife.adapter.SlideMenuAdapter;
import com.project.bluepandora.donatelife.data.Item;
import com.project.bluepandora.donatelife.data.SlideItem;

import java.util.ArrayList;
import java.util.List;

public class MenuFragment extends Fragment {

    private static final String TAG = MenuFragment.class.getSimpleName();
    private View rootView;
    private SlideMenuAdapter listAdapter;
    private FragmentSwitch mFragmentSwitch;
    private List<Item> slideItems;
    private int prevPos = 0;
    private MainViewHolder mainViewHolder;

    public MenuFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createSlidingMenu();
        listAdapter = new SlideMenuAdapter(getActivity(), slideItems);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_menu, container, false);
            mainViewHolder = new MainViewHolder();
            mainViewHolder.menuListView = (ListView) rootView.findViewById(R.id.menu_list_view);
            rootView.setTag(mainViewHolder);
        } else {
            mainViewHolder = (MainViewHolder) rootView.getTag();
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainViewHolder.menuListView.setAdapter(listAdapter);
        mainViewHolder.menuListView.setOnItemClickListener(new ListItemListener());
    }

    private void createSlidingMenu() {
        slideItems = new ArrayList<Item>();
        String[] names = getResources().getStringArray(R.array.nav_drawer_items);
        TypedArray icons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        TypedArray backgrounds = getResources().obtainTypedArray(R.array.nav_drawer_backgrounds);
        SlideItem item;
        for (int i = 0; i < names.length; i++) {
            item = new SlideItem();
            item.setSlideItem(names[i]);
            item.setIcons(icons.getResourceId(i, -1));
            item.setBackground(backgrounds.getResourceId(i, -1));
            slideItems.add(item);
        }
        icons.recycle();
        backgrounds.recycle();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mFragmentSwitch = (FragmentSwitch) getActivity();
    }

    public interface FragmentSwitch {
        public void switchContent(int position);

        public void closeDownMenu();
    }

    private static class MainViewHolder {
        private ListView menuListView;
    }

    private class ListItemListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            listAdapter.setSelected(position);
            if (prevPos != position) {
                mFragmentSwitch.closeDownMenu();
                if (position < 3) {
                    prevPos = position;
                }
                mFragmentSwitch.switchContent(position);
                mainViewHolder.menuListView.setItemChecked(position, true);
                mainViewHolder.menuListView.setSelection(position);
            } else {
                mFragmentSwitch.closeDownMenu();
                Log.i(TAG, "Returned to previous fragment without creating a new one");
            }
        }
    }
}
