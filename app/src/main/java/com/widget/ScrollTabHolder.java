package com.widget;


import android.view.View;

public interface ScrollTabHolder {

    void adjustScroll(int scrollHeight);

    void onScroll(View view, int firstVisibleItem, int visibleItemCount, int totalItemCount, int pagePosition);
}
