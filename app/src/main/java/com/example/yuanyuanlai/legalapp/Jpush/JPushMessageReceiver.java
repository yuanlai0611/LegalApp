package com.example.yuanyuanlai.legalapp.Jpush;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushMessage;

public class JPushMessageReceiver extends cn.jpush.android.service.JPushMessageReceiver{

    private static final String TAG = "JPushMessageReceiver";

    @Override
    public void onTagOperatorResult(Context mContext, JPushMessage mJPushMessage) {
        super.onTagOperatorResult(mContext, mJPushMessage);
    }

    @Override
    public void onCheckTagOperatorResult(Context mContext, JPushMessage mJPushMessage) {
        super.onCheckTagOperatorResult(mContext, mJPushMessage);
    }

    @Override
    public void onAliasOperatorResult(Context mContext, JPushMessage mJPushMessage) {
        super.onAliasOperatorResult(mContext, mJPushMessage);
        Log.d(TAG, mJPushMessage.getAlias());
        Log.d(TAG, "" + mJPushMessage.getErrorCode());
    }

    @Override
    public void onMobileNumberOperatorResult(Context mContext, JPushMessage mJPushMessage) {
        super.onMobileNumberOperatorResult(mContext, mJPushMessage);
    }
}
