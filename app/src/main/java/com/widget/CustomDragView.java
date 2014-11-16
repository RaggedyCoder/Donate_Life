package com.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.widget.helper.DragHelperCallback;

/**
 * Created by tuman on 16/11/2014.
 */
public class CustomDragView extends LinearLayout {


    private final ViewDragHelper mDraghelper;
    private View mHeaderView;
    private View mDragView;

    private int mDragRange;
    private float mInitialMotionX;
    private float mInitialMotionY;
    private int mTop;

    public CustomDragView(Context context) {
        super(context);
        mDraghelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback(this));
    }


    public CustomDragView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mDraghelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback(this));
    }

    @SuppressLint("NewApi")
    public CustomDragView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDraghelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback(this));
    }

    @SuppressLint("NewApi")
    public CustomDragView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDraghelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback(this));
    }

    @Override
    public void computeScroll() {
        if (mDraghelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        if (action != MotionEvent.ACTION_DOWN) {
            mDraghelper.cancel();
            return super.onInterceptTouchEvent(ev);
        } else if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDraghelper.cancel();
            return false;
        }
        final float x = ev.getX();
        final float y = ev.getY();
        boolean interceptTap = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                interceptTap = mDraghelper.isViewUnder(mHeaderView, (int) x, (int) y);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                final float adX = Math.abs(x - mInitialMotionX);
                final float adY = Math.abs(y - mInitialMotionY);
                final int slop = mDraghelper.getTouchSlop();
                if (adY > slop && adX > adY) {
                    mDraghelper.cancel();
                    return false;
                }
            }
        }
        return super.onInterceptTouchEvent(ev) || interceptTap;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDraghelper.processTouchEvent(ev);
        final int action = ev.getAction();
        final float x = ev.getX();
        final float y = ev.getY();
        boolean isViewUnder = mDraghelper.isViewUnder(mHeaderView, (int) x, (int) y);
        switch (action & MotionEventCompat.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                mInitialMotionX = x;
                mInitialMotionY = y;
                break;
            }
            case MotionEvent.ACTION_UP: {
                final float dx = x - mInitialMotionX;
                final float dy = y - mInitialMotionY;
                final int slop = mDraghelper.getTouchSlop();
                if (dx * dx + dy * dy < slop * slop && isViewUnder) {

                }
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mDragRange = getHeight() - mHeaderView.getHeight();
        mHeaderView.layout(0, mTop, r, mTop + mHeaderView.getMeasuredHeight());
    }

    public int getmTop() {
        return mTop;
    }

    public void setmTop(int mTop) {
        this.mTop = mTop;
    }

    public View getmHeaderView() {
        return mHeaderView;
    }
}
