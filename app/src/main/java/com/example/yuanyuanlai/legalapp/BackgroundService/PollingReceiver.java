package com.example.yuanyuanlai.legalapp.BackgroundService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.yuanyuanlai.legalapp.Utils.PollingUtils;

public class PollingReceiver extends BroadcastReceiver {

    public static String TAG = "PollingReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG, "调用了onReceive");
        PollingUtils.startExactAgain(context, 60, PollingReceiver.class, PollingUtils.ACTION);
        Intent i = PollingService.newIntent(context);
        context.startService(i);

    }

}
