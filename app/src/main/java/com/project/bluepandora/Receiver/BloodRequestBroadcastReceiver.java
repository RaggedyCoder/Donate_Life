package com.project.bluepandora.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.project.bluepandora.util.CommonUtilities;
import com.project.bluepandora.util.WakeLocker;

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
 */public final class BloodRequestBroadcastReceiver extends BroadcastReceiver {
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
