package com.example.yuanyuanlai.legalapp.Activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.Application.GlobalApp;
import com.example.yuanyuanlai.legalapp.R;
import com.sdk.bluetooth.bean.BluetoothScanDevice;
import com.sdk.bluetooth.config.BluetoothConfig;
import com.sdk.bluetooth.interfaces.BluetoothManagerDeviceConnectListener;
import com.sdk.bluetooth.interfaces.BluetoothManagerScanListener;
import com.sdk.bluetooth.manage.AppsBluetoothManager;

public class ScanActivity extends AppCompatActivity {
    private String mAddress;
    private String mDeviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_scan );
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

}
