package com.example.yuanyuanlai.legalapp.Application;

import android.app.Application;
import android.content.Context;

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
    }

    public static Context getAppContext(){
        return context;
    }
}
