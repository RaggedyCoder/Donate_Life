package com.widget;

import com.widget.helper.CustomFontHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class CustomButton extends Button {

	public CustomButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(null, null);
	}

	public CustomButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);

	}

	private void init(Context context, AttributeSet attrs) {
		// TODO Auto-generated method stub
		CustomFontHelper.setCustomFont(this, context, attrs);
	}

	public CustomButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	@Override
	public boolean isInEditMode() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	protected void onAnimationEnd() {
		// TODO Auto-generated method stub
		super.onAnimationEnd();
		int[] origin = new int[2];
		getLocationOnScreen(origin);

	}

}
