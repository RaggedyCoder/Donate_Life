package com.project.bluepandora.donatelife.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.project.bluepandora.util.WakeLocker;

import static com.project.bluepandora.util.CommonUtilities.EXTRA_MESSAGE;

/**
 * Created by tuman on 25/12/2014.
 */
public class GCMBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
        WakeLocker.acquire(context);
        Toast.makeText(context, "New Message: " + newMessage, Toast.LENGTH_LONG).show();
        WakeLocker.release();
    }
}
