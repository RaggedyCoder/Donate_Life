package com.widget.helper;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.project.bluepandora.donatelife.R;

public class CustomFontHelper {

    public CustomFontHelper() {

    }

    public static void setCustomFont(TextView textview, Context context,
                                     AttributeSet attrs) {

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Museo);
        String font = a.getString(R.styleable.Museo_font);
        setCustomFont(textview, font, context);
        a.recycle();
    }

    public static void setCustomFont(TextView textview, String font,
                                     Context context) {
        if (font == null) {
            return;
        }
        Typeface tf = FontCache.get(font, context);
        if (tf != null) {
            textview.setTypeface(tf);
        }
    }
}
