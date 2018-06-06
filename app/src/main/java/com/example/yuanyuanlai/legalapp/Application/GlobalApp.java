package com.example.yuanyuanlai.legalapp.Application;

import android.app.Application;
import android.content.Context;

import com.example.yuanyuanlai.legalapp.BlueTooth.BlueToothChangeReceiver;
import com.example.yuanyuanlai.legalapp.Internet.NetStateChangeReceiver;
import com.sdk.bluetooth.app.BluetoothApplicationContext;

import org.litepal.LitePal;

/**
 * github.com/GongYunHaoyyy
 * Creste by GongYunHao on 2018/6/5
 */
public class GlobalApp extends Application{

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate( );
        context=this;
        LitePal.initialize( context );
        BluetoothApplicationContext.getInstance().init( context );
        //在这里注册监听网络状态变化的广播
        NetStateChangeReceiver.registerReceiver(context);
        BlueToothChangeReceiver.registerReceiver(context);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //在这里取消监听网络状态变化的广播
        NetStateChangeReceiver.unRegisterReceiver(context);
        BlueToothChangeReceiver.unRegisterReceiver(context);
    }

    public static Context getAppContext(){
        return context;
    }
}
