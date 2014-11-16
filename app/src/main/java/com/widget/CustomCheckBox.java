package com.widget;

import com.widget.helper.CustomFontHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class CustomCheckBox extends CheckBox {

	public CustomCheckBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CustomCheckBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		CustomFontHelper.setCustomFont(this, context, attrs);
	}

	public CustomCheckBox(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		CustomFontHelper.setCustomFont(this, context, attrs);
	}
	@Override
	public boolean isInEditMode() {
		// TODO Auto-generated method stub
		return true;
	}
}
