package com.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Switch;

import com.widget.helper.CustomFontHelper;

/**
 * Created by tuman on 9/12/2014.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CustomSwitch extends Switch {
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public CustomSwitch(Context context) {
        super(context);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public CustomSwitch(Context context, AttributeSet attrs) {
        super(context, attrs);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomSwitch(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        CustomFontHelper.setCustomFont(this, context, attrs);
    }
}
