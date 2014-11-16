package com.widget.helper;


import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;

public class FontCache {

	private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();;

	public static Typeface get(String name, Context context) {
		Typeface tf = null;
		if (fontCache != null)
			tf = fontCache.get(name);
		if (tf != null) {
			return tf;
		} else {
			try {
				tf = Typeface.createFromAsset(context.getAssets(), name);
				put(name, context, tf);

			} catch (Exception e) {

			}
			return fontCache.get(name);
		}

	}

	private static void put(String name, Context context, Typeface data) {

		if (name == null || data == null) {
			return;
		}
		if (fontCache != null) {
			fontCache.put(name, data);
		}
	}
}