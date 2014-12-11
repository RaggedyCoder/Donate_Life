package com.project.bluepandora.donatelife.preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.util.Log;

import java.util.Locale;

/**
 * Created by tuman on 10/12/2014.
 */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public class CustomSwitchPreference extends SwitchPreference {
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomSwitchPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSwitchPreference(Context context) {
        super(context);
    }

    @Override
    protected void notifyChanged() {
        super.notifyChanged();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Log.e("TAG", prefs.getBoolean("pref_app_language", false) + "");
        if (prefs.getBoolean("pref_app_language", false)) {
            String languageToLoad = "bn";
            Locale locale = new Locale(languageToLoad);
            Configuration config = new Configuration();
            config.locale = locale;
            getContext().getResources().updateConfiguration(config, null);


        } else {
            Locale locale = Locale.ROOT;
            Configuration config = new Configuration();
            config.locale = locale;
            getContext().getResources().updateConfiguration(config, null);
        }

    }

}
