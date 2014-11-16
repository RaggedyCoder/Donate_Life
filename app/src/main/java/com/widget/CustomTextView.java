package com.widget;

import com.widget.helper.CustomFontHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class CustomTextView extends TextView {

	public CustomTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(null);
	}

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(attrs);
	}

	public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(attrs);
	}

	private void init(AttributeSet attrs) {
		// TODO Auto-generated method stub
		CustomFontHelper.setCustomFont(this, this.getContext(), attrs);
	}

	@Override
	public boolean isInEditMode() {
		// TODO Auto-generated method stub
		return true;
	}

}
