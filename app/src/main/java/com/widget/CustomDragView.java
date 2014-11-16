package com.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.widget.helper.DragHelperCallback;

/**
 * Created by tuman on 16/11/2014.
 */
public class CustomDragView extends LinearLayout {


    private final ViewDragHelper mDraghelper;
    private View mDragView;


    public CustomDragView(Context context) {
        super(context);
        mDraghelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }


    public CustomDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDraghelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    @SuppressLint("NewApi")
    public CustomDragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDraghelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }

    @SuppressLint("NewApi")
    public CustomDragView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDraghelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    }
}
