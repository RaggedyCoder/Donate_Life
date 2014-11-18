package com.project.bluepandora.blooddonation.fragments;

import android.support.v4.app.Fragment;
import android.widget.AbsListView;

import com.widget.ScrollTabHolder;

/**
 * Created by tuman on 17/11/2014.
 */
public abstract class ScrollTabHolderFragment extends Fragment implements ScrollTabHolder {
    protected ScrollTabHolder mScrollTabHolder;

    public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
        mScrollTabHolder = scrollTabHolder;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition) {
        // nothing
    }
}
