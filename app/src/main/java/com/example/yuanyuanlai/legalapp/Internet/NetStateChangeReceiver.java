package com.example.yuanyuanlai.legalapp.Internet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;

import com.example.yuanyuanlai.legalapp.utils.NetworkUtil;

import java.util.ArrayList;
import java.util.List;


public class NetStateChangeReceiver extends BroadcastReceiver{

    private static class InstanceHolder{
        private static final NetStateChangeReceiver INSTANCE = new NetStateChangeReceiver();
    }

    private List<NetStateChangeObserver> mObservers = new ArrayList<>();

    /*
      一旦网络状态发生变化，通知观察者
     */
    @Override
    public void onReceive(Context context, Intent intent) {

        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            NetworkType networkType = NetworkUtil.getNetworkType(context);
            notifyObservers(networkType);
        }

    }

    /*
       在这里动态注册对网络监听的广播（为隐式广播 ConnectivityManager.CONNECTIVITY_ACTION）
     */
    public static void registerReceiver(Context context){
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(InstanceHolder.INSTANCE,intentFilter);
    }

    /*
       在这里取消对网络监听的广播
     */
    public static void unRegisterReceiver(Context context){
        context.unregisterReceiver(InstanceHolder.INSTANCE);
    }

    /*
       这里注册网络变化的Observer
       往数组里面添加Observer对象
     */
    public static void registerObserver(NetStateChangeObserver observer){
        if (observer == null)
            return;
        if (!InstanceHolder.INSTANCE.mObservers.contains(observer)){
            InstanceHolder.INSTANCE.mObservers.add(observer);
        }
    }

    /*
       这里取消网络变化的Observer
       从数组里面删除Observer对象
     */
    public static void unRegisterObserver(NetStateChangeObserver observer){
        if (observer == null)
            return;
        if (InstanceHolder.INSTANCE.mObservers == null)
            return;
        InstanceHolder.INSTANCE.mObservers.remove(observer);
    }

    /*
       这里是观察者模式实现的关键，接受到通知以后通过判断当前的网络的类型，调用不同的方法
     */
    private void notifyObservers(NetworkType networkType){
        if (networkType == NetworkType.NETWORK_NO){
            for (NetStateChangeObserver observer : mObservers){
                observer.onNetDisconnected();
            }
        }else {
            for (NetStateChangeObserver observer : mObservers){
                observer.onNetConnected(networkType);
            }
        }
    }

}
