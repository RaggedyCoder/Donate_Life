package com.project.bluepandora.donatelife;

/*
 * Copyright (C) 2014 The Blue Pandora Project Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.project.bluepandora.donatelife.activities.MainActivity;
import com.project.bluepandora.donatelife.activities.SettingsActivity;
import com.project.bluepandora.donatelife.data.UserInfoItem;
import com.project.bluepandora.donatelife.datasource.UserDataSource;
import com.project.bluepandora.donatelife.services.ServerUtilities;
import com.project.bluepandora.util.CommonUtilities;

public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(CommonUtilities.SENDER_ID);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.notif;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);

        String title = context.getString(R.string.app_name);


        Intent notificationIntent = new Intent(context, MainActivity.class);
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        // Play default notification sound
        //notification.defaults |= Notification.DEFAULT_SOUND;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String strRingtonePreference = prefs.getString("pref_filter_ringtone", "DEFAULT_RINGTONE_URI");
        notification.sound = Uri.parse(strRingtonePreference);
        // Vibrate if vibrate is enabled
        if (PreferenceManager.getDefaultSharedPreferences(
                context).getBoolean(SettingsActivity.VIBRATION_TAG, true)) {
            notification.defaults |= Notification.DEFAULT_VIBRATE;
        }
        notificationManager.notify(0, notification);
    }

    /**
     * Method called on device registered
     */
    @Override
    protected void onRegistered(Context context, String registrationId) {
        UserDataSource database = new UserDataSource(context);
        database.open();
        UserInfoItem userInfo = database.getAllUserItem().get(0);
        Log.i(TAG, "Device registered: regId = " + registrationId);
        CommonUtilities.displayMessage(context, "Your device registred with GCM");
        Log.d("NAME", userInfo.getMobileNumber());
        ServerUtilities.register(context, userInfo.getMobileNumber(), registrationId);
        database.close();
    }

    /**
     * Method called on device un registered
     */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        UserDataSource database = new UserDataSource(context);
        database.open();
        UserInfoItem userInfo = database.getAllUserItem().get(0);
        Log.i(TAG, "Device unregistered");
        CommonUtilities.displayMessage(context, "Your device registred with GCM");
        Log.d("NAME", userInfo.getMobileNumber());
        CommonUtilities.displayMessage(context, getString(R.string.gcm_unregistered));
        ServerUtilities.unregister(context, userInfo.getMobileNumber());
        database.close();
    }

    /**
     * Method called on Receiving a new message
     */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String groupId = intent.getExtras().getString("message");
        //String hospitaId = intent.getExtras().getString("hospitalId");
        String message = groupId;
        // notifies user
        //Toast.makeText(context, context.getPackageName(), Toast.LENGTH_SHORT).show();
        //CommonUtilities.displayMessage(context, message);

        if (PreferenceManager.getDefaultSharedPreferences(
                context).getBoolean(SettingsActivity.NOTIFICATION_TAG, true)) {
            generateNotification(context, message);
        }

    }

    /**
     * Method called on receiving a deleted message
     */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        CommonUtilities.displayMessage(context, message);
        // notifies user
        if (PreferenceManager.getDefaultSharedPreferences(
                context).getBoolean(SettingsActivity.NOTIFICATION_TAG, true)) {
            generateNotification(context, message);
        }
    }

    /**
     * Method called on Error
     */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        CommonUtilities.displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        CommonUtilities.displayMessage(context,
                getString(R.string.gcm_recoverable_error, errorId));
        return super.onRecoverableError(context, errorId);
    }
}