package com.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
	private OnScrollChangedListener mOnScrollChangedListener;
	private boolean mIsOverScrollEnabled = true;

	public CustomScrollView(Context context) {
		super(context);

	}

	public CustomScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public CustomScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {

		super.onScrollChanged(l, t, oldl, oldt);
		if (mOnScrollChangedListener != null) {
			mOnScrollChangedListener.onScrollChanged(this, l, t, oldl, oldt);
		}
	}

	public void setOverScrollEnabled(boolean enabled) {
		mIsOverScrollEnabled = enabled;
	}

	public boolean isOverScrollEnabled() {
		return mIsOverScrollEnabled;
	}

	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX,
			int scrollY, int scrollRangeX, int scrollRangeY,
			int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY,
				scrollRangeX, scrollRangeY,
				mIsOverScrollEnabled ? maxOverScrollX : 0,
				mIsOverScrollEnabled ? maxOverScrollY : 0, isTouchEvent);
	}

	public interface OnScrollChangedListener {
		void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt);
	}

	public void setOnScrollChangedListener(OnScrollChangedListener listener) {
		mOnScrollChangedListener = listener;
	}

}
