package com.project.bluepandora.donatelife.preferences;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.util.AttributeSet;
import android.util.Log;

public class NotificationSoundPreference extends RingtonePreference {

    private static final String TAG = NotificationSoundPreference.class.getSimpleName();
    private static final String DEFAULT_NOTIFICATION_URI = "content://settings/system/notification_sound";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NotificationSoundPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String strRingtonePreference = prefs.getString("pref_filter_ringtone", DEFAULT_NOTIFICATION_URI);
        Uri ringtoneUri = Uri.parse(strRingtonePreference);
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        String name = ringtone.getTitle(context);
        setSummary(name);
    }

    public NotificationSoundPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String strRingtonePreference = prefs.getString("pref_filter_ringtone", DEFAULT_NOTIFICATION_URI);
        Uri ringtoneUri = Uri.parse(strRingtonePreference);
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        String name = ringtone.getTitle(context);
        setSummary(name);

    }

    public NotificationSoundPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String strRingtonePreference = prefs.getString("pref_filter_ringtone", DEFAULT_NOTIFICATION_URI);
        Uri ringtoneUri = Uri.parse(strRingtonePreference);
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        String name = ringtone.getTitle(context);
        setSummary(name);
    }

    public NotificationSoundPreference(Context context) {
        super(context);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String strRingtonePreference = prefs.getString("pref_filter_ringtone", DEFAULT_NOTIFICATION_URI);
        Uri ringtoneUri = Uri.parse(strRingtonePreference);
        Ringtone ringtone = RingtoneManager.getRingtone(context, ringtoneUri);
        String name = ringtone.getTitle(context);
        setSummary(name);
    }

    @Override
    protected boolean callChangeListener(Object newValue) {
        Uri ringtoneUri = Uri.parse(newValue.toString());
        Ringtone ringtone = RingtoneManager.getRingtone(getContext(), ringtoneUri);
        String name = ringtone.getTitle(getContext());
        setSummary(name);
        Log.i(TAG, newValue.toString());
        return super.callChangeListener(newValue);
    }
}
