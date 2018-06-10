package com.example.yuanyuanlai.legalapp.BlueTooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.yuanyuanlai.legalapp.Internet.NetStateChangeReceiver;

import java.util.ArrayList;
import java.util.List;


public class BlueToothChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "BlueToothChangeReceiver";

    private List<BlueToothChangeObserver> mObservers = new ArrayList<>();

    private static class InstanceHolder{
        private static final BlueToothChangeReceiver INSTANCE = new BlueToothChangeReceiver();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction() == BluetoothAdapter.ACTION_STATE_CHANGED){
            int blueToothState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
            notifyObservers(blueToothState);
        }

    }

    public static void registerReceiver(Context context){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        context.registerReceiver(InstanceHolder.INSTANCE, intentFilter);
    }

    public static void unRegisterReceiver(Context context){
        context.unregisterReceiver(InstanceHolder.INSTANCE);
    }

    public static void registerObserver(BlueToothChangeObserver observer){
        if (observer == null)
            return;
        if (!BlueToothChangeReceiver.InstanceHolder.INSTANCE.mObservers.contains(observer)){
            BlueToothChangeReceiver.InstanceHolder.INSTANCE.mObservers.add(observer);
        }
    }

    public static void unRegisterObserver(BlueToothChangeObserver observer){
        if (observer == null)
            return;
        if (BlueToothChangeReceiver.InstanceHolder.INSTANCE.mObservers == null)
            return;
        BlueToothChangeReceiver.InstanceHolder.INSTANCE.mObservers.remove(observer);
    }

    private void notifyObservers(int STATE){

        if (STATE == BluetoothAdapter.STATE_ON){

            for (BlueToothChangeObserver observer : mObservers){
                observer.onBlueToothConnected();
            }

        }else if (STATE == BluetoothAdapter.STATE_OFF){
            for (BlueToothChangeObserver observer : mObservers){
                observer.onBlueToothDisconnected();
            }
        }

    }

}
