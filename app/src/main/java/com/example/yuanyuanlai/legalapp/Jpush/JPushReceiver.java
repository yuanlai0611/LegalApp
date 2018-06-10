package com.example.yuanyuanlai.legalapp.Jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.example.yuanyuanlai.legalapp.Activity.CheckActivity;
import com.example.yuanyuanlai.legalapp.Bean.TypeBean;
import com.google.gson.Gson;

import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver {

    private static final String TAG = "PushReceiver";
    enum ACTION{
        SIGN_IN,ALARM
    }
    ACTION action;

    @Override
    public void onReceive(Context context, Intent intent) {

      if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())){

          Log.d(TAG, "JPush用户注册成功");

      }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())){

          Bundle bundle = intent.getExtras();
          String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
          if (!TextUtils.isEmpty(message)){

              Log.d(TAG, "Message: "+ message);

          }

          Log.d(TAG, "接受到推送下来的自定义消息");

      }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){

          Log.d(TAG, "接受到推送下来的通知");

      }else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){

          Bundle bundle = intent.getExtras();
          String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);

          Gson gson = new Gson();
          TypeBean typeBean = gson.fromJson(extra, TypeBean.class);
          String typeId = typeBean.getTYPE_ID();
          if (typeId.equals("1")){
              Intent i = CheckActivity.newIntent(context);
              i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
              context.startActivity(i);
          }

          Log.d(TAG, "用户点击打开了通知");
      }
}

}
