package com.widget.helper;

import android.widget.AbsListView;

/**
 * Created by tuman on 24/11/2014.
 */
public interface ScrollTabHolder {

    public void adjustScroll(int scrollHeight);

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
