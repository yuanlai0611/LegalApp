package com.example.yuanyuanlai.legalapp.BackgroundService;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class PollingService extends IntentService {

    private static final String TAG = "PollingService";

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

       // Log.d(TAG, "启动了服务");

    }

    public static Intent newIntent(Context context){

        Intent intent = new Intent(context, PollingService.class);
        return intent;

    }

    public PollingService() {
        super(TAG);
    }

}
