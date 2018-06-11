package com.example.yuanyuanlai.legalapp.Activity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.Application.GlobalApp;
import com.example.yuanyuanlai.legalapp.BackgroundService.PollingReceiver;

import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;
import com.example.yuanyuanlai.legalapp.Utils.DialogUtil;
import com.example.yuanyuanlai.legalapp.Utils.PollingUtils;
import com.sdk.bluetooth.bean.BluetoothScanDevice;
import com.sdk.bluetooth.config.BluetoothConfig;
import com.sdk.bluetooth.interfaces.BluetoothManagerDeviceConnectListener;
import com.sdk.bluetooth.interfaces.BluetoothManagerScanListener;
import com.sdk.bluetooth.manage.AppsBluetoothManager;

public class HomeActivity extends BaseActivity implements BaseActivity.GetLocation{

    private Button facialRecognitionButton;
    private Button checkButton;
    private Button notificationButton;
    private Button myButton;
    private Intent intent;
    private LocationManager mLocationManager;
    private Location location;
    private String mAddress;
    private String mDeviceName;
    private AlertDialog mloadingDialog;
    private AlertDialog alertDialog;
    private BluetoothAdapter mBluetoothAdapter;
    private static final String TAG = "HomeActivity";
    private static final int REQUEST_FINE_LOCATION = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGetLocation(this);
        PollingUtils.startPollingService( this, 10, PollingReceiver.class, PollingUtils.ACTION );

        alertDialog=new AlertDialog.Builder( HomeActivity.this )
                .setTitle(  "警告！！" )
                .setMessage( "请打开手机蓝牙功能！" )
                .setIcon( getResources().getDrawable( R.drawable.ic_priority_high_red_24dp ) )
                .setCancelable( false )
                .create();
        mloadingDialog = DialogUtil.showProgressDialog( HomeActivity.this,"扫描连接中...",false );

        askForLocation();

    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,HomeActivity.class);
        return intent;
    }

    @Override
    public void initListener() {
       facialRecognitionButton.setOnClickListener(this);
       checkButton.setOnClickListener(this);
       notificationButton.setOnClickListener(this);
       myButton.setOnClickListener(this);


    }

    @SuppressLint("MissingPermission")
    @Override
    public void getLocation() {
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        location = getLocation(mLocationManager);
        Log.d(TAG,"Latitude:" + location.getLatitude() + ", Longitude:" + location.getLongitude());
    }

    @Override
    public void findViewById() {
        facialRecognitionButton = (Button)findViewById(R.id.facialRecognitionButton);
        checkButton = (Button)findViewById(R.id.checkButton);
        notificationButton = (Button)findViewById(R.id.notificationButton);
        myButton = (Button)findViewById(R.id.myButton);
    }

    /*
     * 扫描蓝牙设备
     */
    private void scanDevice() {
        mloadingDialog.show();
        if (mBluetoothAdapter == null) {
            // showToast(getString(R.string.error_bluetooth_not_supported));
            return;
        }
        if (mBluetoothAdapter.isEnabled()) {
            mAddress = "";
            AppsBluetoothManager.getInstance( GlobalApp.getAppContext()).startDiscovery();
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
            scanDevice();
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
            Toast.makeText(GlobalApp.getAppContext(), "--->onConnectDeviceTimeOut", Toast.LENGTH_SHORT).show();
        }
    };

    private void openBindDevice() {
        mloadingDialog.dismiss();
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).clearBluetoothManagerDeviceConnectListeners();
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).removeBluetoothManagerScanListeners(scanListener);
        Log.i("DeviceAddress", mAddress);
        Log.i("DeviceName", mDeviceName);

        BluetoothConfig.setDefaultMac(GlobalApp.getAppContext(), mAddress);
//        startActivity( MyActivity.newIntent( this ));//此处超过4行就不能扫描，未知的bug
//        finish();
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
                    AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
                    AlertDialog mdialog=new AlertDialog.Builder( HomeActivity.this )
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
            if (!HomeActivity.this.isDestroyed()) {
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
    protected void onStart() {
        super.onStart( );
        //获取蓝牙的系统服务
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).addBluetoothManagerScanListeners(scanListener);
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).addBluetoothManagerDeviceConnectListener(connectListener);

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

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.facialRecognitionButton:
             SharedPreferences sharedPreferences = getSharedPreferences("facialRecognition", MODE_PRIVATE);
             boolean isSuccess = sharedPreferences.getBoolean("isFirstSuccess", false);
             if (isSuccess){
                 Toast.makeText(HomeActivity.this, "已经经过第一次登陆了", Toast.LENGTH_SHORT).show();
             }else {
                 intent = FacialRecognitionActivity.newIntent(this);
                 startActivity(intent);
             }
             break;
         case R.id.checkButton:
             intent = CheckActivity.newIntent(this);
             startActivity(intent);
             break;
         case R.id.notificationButton:
             intent = NotificationActivity.newIntent( this );
             startActivity( intent );
             break;
         case R.id.myButton:
             intent = MyActivity.newIntent( this );
             startActivity( intent );
             break;
         default:
             break;
     }
    }

    @SuppressLint("MissingPermission")
    private Location getLocation(LocationManager locationManager){
        Location result = null;
        if (locationManager != null){
            result = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (result != null){
            return result;
            }else {
            result = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            return result;
            }
        }
        return result;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onNetDisconnected() {

        Log.d(TAG,"调用了HomeActivity的onNetDisconnected");

    }

    @Override
    public void onNetConnected(NetworkType networkType) {

    }

    @Override
    public void onBlueToothDisconnected() {
        AppsBluetoothManager.getInstance(GlobalApp.getAppContext()).cancelDiscovery();
        alertDialog.show();
        AlertDialog alertDialog =new  AlertDialog.Builder(HomeActivity.this)
                .setTitle( "警告！" )
                .setIcon( getResources().getDrawable( R.drawable.ic_priority_high_red_24dp ) )
                .setMessage( "蓝牙断开连接，已向服务器发送警报" )
                .create();
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
