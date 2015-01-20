package com.project.bluepandora.donatelife.preferences;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;

import com.project.bluepandora.donatelife.activities.SettingsActivity;

import java.util.Locale;

public class CustomCheckBoxPreference extends CheckBoxPreference {

    private static final String TAG = CustomCheckBoxPreference.class.getSimpleName();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @SuppressWarnings("UnusedDeclaration")
    public CustomCheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressWarnings("UnusedDeclaration")
    public CustomCheckBoxPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("UnusedDeclaration")
    public CustomCheckBoxPreference(Context context) {
        super(context);
    }

    @Override
    protected void notifyChanged() {
        super.notifyChanged();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        final boolean languageFlag = prefs.getBoolean(SettingsActivity.LANGUAGE_TAG, false);
        if (languageFlag) {
            Locale locale = new Locale("bn");
            Configuration config = new Configuration();
            config.locale = locale;
            getContext().getResources().updateConfiguration(config, null);
            Log.e(TAG, "Bangla mode.");
        } else {
            Locale locale = Locale.ENGLISH;
            Configuration config = new Configuration();
            config.locale = locale;
            getContext().getResources().updateConfiguration(config, null);
            Log.e(TAG, "English mode.");
        }
    }
}
