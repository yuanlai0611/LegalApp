package com.example.yuanyuanlai.legalapp.BlueTooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;


public class BlueToothChangeReceiver extends BroadcastReceiver {

    private static final String TAG = "BlueToothChangeReceiver";
    private BlueToothChange mBlueToothChange = null;

    private static class InstanceHolder{
        private static final BlueToothChangeReceiver INSTANCE = new BlueToothChangeReceiver();
    }

    public interface BlueToothChange{
        void BlueToothIsOpen();
        void BlueToothIsClose();
    }

    public void setBlueToothListener(BlueToothChange blueToothListener){
        mBlueToothChange = blueToothListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d(TAG,"调用了onReceive");
        switch (intent.getAction()){
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,0);
                switch (blueState) {
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG,"STATE_TURNING_ON");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        mBlueToothChange.BlueToothIsOpen();
                        Log.d(TAG,"STATE_ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        break;
                    case BluetoothAdapter.STATE_OFF:
                        mBlueToothChange.BlueToothIsClose();
                        Log.d(TAG,"STATE_OFF");
                        break;
                }
                break;
            default:
                break;
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

    private void notifyObservers(String STATE){

    }

}
