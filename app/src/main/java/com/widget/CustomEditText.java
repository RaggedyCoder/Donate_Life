package com.widget;

import com.widget.helper.CustomFontHelper;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

public class CustomEditText extends EditText {

	public CustomEditText(Context context) {
		super(context);
		init(context, null);
	}

	public CustomEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	private void init(Context context, AttributeSet attrs) {
		CustomFontHelper.setCustomFont(this, context, attrs);
	}

}
