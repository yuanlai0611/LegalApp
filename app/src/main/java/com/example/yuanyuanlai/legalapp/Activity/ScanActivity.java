package com.example.yuanyuanlai.legalapp.Activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.Application.GlobalApp;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.BlueTooth.BlueToothChangeReceiver;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;
import com.sdk.bluetooth.bean.BluetoothScanDevice;
import com.sdk.bluetooth.config.BluetoothConfig;
import com.sdk.bluetooth.interfaces.BluetoothManagerDeviceConnectListener;
import com.sdk.bluetooth.interfaces.BluetoothManagerScanListener;
import com.sdk.bluetooth.manage.AppsBluetoothManager;

public class ScanActivity extends BaseActivity{
    private String mAddress;
    private String mDeviceName;
    private BluetoothAdapter mBluetoothAdapter;
    private AlertDialog alertDialog;

    public static Intent newIntent(Context context){
        Intent intent=new Intent( context,ScanActivity.class );
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        alertDialog=new AlertDialog.Builder( ScanActivity.this )
                .setTitle(  "警告！！" )
                .setMessage( "请打开手机蓝牙功能！" )
                .setIcon( getResources().getDrawable( R.drawable.ic_priority_high_red_24dp ) )
                .setCancelable( false )
                .create();
        //获取蓝牙的系统服务
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        if (mBluetoothAdapter==null){
            //手机不支持蓝牙模块
        }else {
            if (mBluetoothAdapter.isEnabled()){
                //蓝牙开启了
                scanDevice();
            }else {
                alertDialog.show();
            }
        }

        BlueToothChangeReceiver blueToothChangeReceiver=new BlueToothChangeReceiver();
        blueToothChangeReceiver.setBlueToothListener( new BlueToothChangeReceiver.BlueToothChange( ) {
            @Override
            public void BlueToothIsOpen() {
                if (alertDialog!=null){
                    if (alertDialog.isShowing()){
                        alertDialog.dismiss();
                    }
                }
                scanDevice();
            }

            @Override
            public void BlueToothIsClose() {
                AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
                alertDialog.show();
            }
        } );

    }

    /*
     * 扫描蓝牙设备
     */
    private void scanDevice() {
        if (mBluetoothAdapter == null) {
            // showToast(getString(R.string.error_bluetooth_not_supported));
            return;
        }
        if (mBluetoothAdapter.isEnabled()) {
            mAddress = "";
            // AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
//            devAdapter.clearList();
            AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).startDiscovery();
            Toast.makeText( GlobalApp.getAppContext(),"在扫描",Toast.LENGTH_SHORT ).show();
        } else {
            // showToast(getString(R.string.turn_on_bluetooth_tips));
        }
    }



    /**
     * 蓝牙连接状态
     */
    private BluetoothManagerDeviceConnectListener connectListener = new BluetoothManagerDeviceConnectListener() {
        @Override
        public void onConnected(BluetoothDevice device) {
            // Toast.makeText(DeviceScanActivity.this, "--->onConnected", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onConnectFailed() {
            Toast.makeText(GlobalApp.getAppContext(), "--->onConnectFailed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEnableToSendComand(final BluetoothDevice device) {
            Toast.makeText(GlobalApp.getAppContext(), "--->onEnableToSendComand", Toast.LENGTH_SHORT).show();
            mAddress = device.getAddress();
            mDeviceName = device.getName();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).createBound(device);
                    openBindDevice();
                }
            });
        }

        @Override
        public void onConnectDeviceTimeOut() {
            // connect_time_out
            Toast.makeText(GlobalApp.getAppContext(), "--->onConnectDeviceTimeOut", Toast.LENGTH_SHORT).show();
        }
    };

    private void openBindDevice() {
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).clearBluetoothManagerDeviceConnectListeners();
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).removeBluetoothManagerScanListeners(scanListener);
        Log.i("DeviceAddress", mAddress);
        Log.i("DeviceName", mDeviceName);

        BluetoothConfig.setDefaultMac(GlobalApp.getAppContext(), mAddress);
//        startActivity(new Intent(this, nextactivity.class));
        finish();
    }

    /**
     * 扫描蓝牙回调
     */
    private BluetoothManagerScanListener scanListener = new BluetoothManagerScanListener() {
        @Override
        public void onDeviceFound(BluetoothScanDevice scanDevice) {
            if (scanDevice != null && scanDevice.getBluetoothDevice() != null) {
                BluetoothDevice bluetoothDevice = scanDevice.getBluetoothDevice();
                if (null != bluetoothDevice.getName()) {
                    //此处获得扫描i到的蓝牙设备
//                    devAdapter.addDevice(devAdapter.new DevData(bluetoothDevice, scanDevice.getRssi()));
                }
            }
        }

        @Override
        public void onDeviceScanEnd() {
            if (!ScanActivity.this.isDestroyed()) {
//                if (devAdapter.getCount() <= 0) {
//                    if (!DeviceScanActivity.this.isDestroyed()) {
//                        // no_found_device
//                    }
//                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).removeBluetoothManagerScanListeners(scanListener);
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).removeBluetoothManagerDeviceConnectListeners(connectListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).addBluetoothManagerScanListeners(scanListener);
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).addBluetoothManagerDeviceConnectListener(connectListener);
    }

    @Override
    public void setContentView() {
        setContentView( R.layout.activity_scan );
    }

    @Override
    public void initListener() {

    }

    @Override
    public void findViewById() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onNetDisconnected() {

    }

    @Override
    public void onNetConnected(NetworkType networkType) {

    }
}
