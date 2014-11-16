package com.widget.helper;

import android.support.v4.widget.ViewDragHelper;
import android.view.View;

import com.widget.CustomDragView;

/**
 * Created by tuman on 16/11/2014.
 */
public class DragHelperCallback extends ViewDragHelper.Callback {

    private CustomDragView parent;

    public DragHelperCallback(CustomDragView parent) {
        this.parent = parent;
    }

    @Override
    public boolean tryCaptureView(View child, int i) {
        return child == parent.getmHeaderView();
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        parent.setmTop(top);
        parent.requestLayout();
    }


    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
        final int topBound = parent.getPaddingTop();
        final int bottomBound = parent.getHeight() - parent.getmHeaderView().getHeight() -
                parent.getmHeaderView().getPaddingBottom();
        final int newTop = Math.min(Math.max(top, topBound), bottomBound);
        return newTop;
    }
}
