package com.project.bluepandora.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.project.bluepandora.util.CommonUtilities;
import com.project.bluepandora.util.WakeLocker;

/**
 * Created by tuman on 1/11/2014.
 */
public final class BloodRequestBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String newMessage = intent.getExtras().getString(CommonUtilities.EXTRA_MESSAGE);
        // Waking up mobile if it is sleeping
        WakeLocker.acquire(context);

        /**
         * Take appropriate action on this message
         * depending upon your app requirement
         * For now i am just displaying it on the screen
         * */

        // Showing received message
        Toast.makeText(context, "New Message: " + newMessage, Toast.LENGTH_LONG).show();

        // Releasing wake lock
        WakeLocker.release();
    }
}
