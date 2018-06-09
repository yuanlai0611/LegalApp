package com.example.yuanyuanlai.legalapp.Activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.Application.GlobalApp;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.BlueTooth.BlueToothChangeObserver;
import com.example.yuanyuanlai.legalapp.BlueTooth.BlueToothChangeReceiver;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;
import com.sdk.bluetooth.bean.BluetoothScanDevice;
import com.sdk.bluetooth.config.BluetoothConfig;
import com.sdk.bluetooth.interfaces.BluetoothManagerDeviceConnectListener;
import com.sdk.bluetooth.interfaces.BluetoothManagerScanListener;
import com.sdk.bluetooth.manage.AppsBluetoothManager;

public class ScanActivity extends BaseActivity{
    private final String TAG = "ScanActivity";
    private String mAddress;
    private String mDeviceName;
    private BluetoothAdapter mBluetoothAdapter;
    private AlertDialog alertDialog;
    private Button testbutton;

    public static Intent newIntent(Context context){
        Intent intent=new Intent( context,ScanActivity.class );
        return intent;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler=new Handler(  ){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
//                    AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
//                    AlertDialog mdialog=new AlertDialog.Builder( ScanActivity.this )
//                            .setTitle( "提示：" )
//                            .setMessage( "扫描到设备：" )
//                            .setCancelable( false )
//                            .setPositiveButton( "连接", new DialogInterface.OnClickListener( ) {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
// //                                    AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
//                                    AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).connectDevice(bluetoothDevice.getAddress());
//
//                                }
//                            } )
//                            .create();
//                    mdialog.show();
                    break;
            }
        }
    };

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

        testbutton=findViewById(R.id.testbutton);
        testbutton.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                scanDevice();
            }
        } );


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
        startActivity(MyActivity.newIntent( this ));//此处超过4行就不能扫描，未知的bug
        finish();
    }

    /**
     * 扫描蓝牙回调
     */
    private BluetoothManagerScanListener scanListener = new BluetoothManagerScanListener() {
        @Override
        public void onDeviceFound(BluetoothScanDevice scanDevice) {
            Log.d( TAG,"----------回调了" );
            if (scanDevice != null && scanDevice.getBluetoothDevice() != null) {
                final BluetoothDevice bluetoothDevice = scanDevice.getBluetoothDevice();
                if (null != bluetoothDevice.getName()) {
                    //判断是否是账号对应的设备名称
//                    Message message=new Message();
//                    message.what = 1;
//                    handler.sendMessage( message );
                    AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
                    AlertDialog mdialog=new AlertDialog.Builder( ScanActivity.this )
                            .setTitle( "提示：" )
                            .setMessage( "扫描到设备："+bluetoothDevice.getName() )
                            .setCancelable( false )
                            .setPositiveButton( "连接", new DialogInterface.OnClickListener( ) {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //                                    AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
                                    AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).connectDevice(bluetoothDevice.getAddress());

                                }
                            } )
                            .create();
                    mdialog.show();
                    Log.d( TAG,bluetoothDevice.getName()+scanDevice.getRssi() );
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

    @Override
    public void onBlueToothDisconnected() {
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
        alertDialog.show();
    }

    @Override
    public void onBlueToothConnected() {
        if (alertDialog!=null){
            if (alertDialog.isShowing()){
                alertDialog.dismiss();
            }
        }
        scanDevice();
    }
}
