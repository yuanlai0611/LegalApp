package com.example.yuanyuanlai.legalapp.BackgroundService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.yuanyuanlai.legalapp.Activity.HomeActivity;
import com.example.yuanyuanlai.legalapp.Bean.WatchSummaryInfo;
import com.example.yuanyuanlai.legalapp.BlueTooth.BlueToothChangeObserver;
import com.example.yuanyuanlai.legalapp.BlueTooth.BlueToothChangeReceiver;
import com.example.yuanyuanlai.legalapp.Guide.GuideActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetStateChangeObserver;
import com.example.yuanyuanlai.legalapp.Internet.NetStateChangeReceiver;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.Login.LoginActivity;
import com.example.yuanyuanlai.legalapp.Utils.BlueToothUtil;
import com.example.yuanyuanlai.legalapp.Utils.OkhttpUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PollingService extends IntentService implements NetStateChangeObserver, BlueToothChangeObserver {
    private LocationManager mLocationManager;
    private Location location;
    private double latitude=0,longitude=0;
    private String deviceId;
    private int heartRateData = 70+new Random(  ).nextInt(10);
    private int mstep = 0;
    private int mcalorie = 0;
    private int menergy = 0;
    private int gpsId = 0;
    private String timestamp;
    private BlueToothUtil blueToothUtil;
    private int mflag = 0;

    private static final String TAG = "PollingService";

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage( msg );
            switch (msg.what){
                case 1:
                    Log.d( TAG, "准备获取心率数据啊啊啊啊又疯了一个" );
                    blueToothUtil.getHeartRateData();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onHandleIntent(Intent intent) {
        mflag=0;
        Log.d( TAG, "启动了服务" );
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");// HH:mm:ss
        //获取当前时间
        Date date = new Date(System.currentTimeMillis());
        timestamp=simpleDateFormat.format(date).toString();
        mflag++;
        getLocation();
        Log.d( TAG, timestamp );
        blueToothUtil = new BlueToothUtil();
        blueToothUtil.getWatchId();
        blueToothUtil.setDeviceId( new BlueToothUtil.DeviceId( ) {
            @Override
            public void getDeviceId(String id) {
                deviceId=id;
                mflag++;
                Log.d( TAG, "deviceId：" +deviceId );
                blueToothUtil.getSportDataCounts();
            }
        } );
        blueToothUtil.setHeartRate( new BlueToothUtil.HeartRate( ) {
            @Override
                public void getHeartRate(int heartrate) {
                    heartRateData=heartrate;
                mflag++;
                Log.d( TAG,"14秒后得到的心率是："+heartRateData+"mflag为："+mflag );
                if(mflag>=5){
                    sendDataToServer();
                }
            }
        } );

        blueToothUtil.setSportDetailData( new BlueToothUtil.SportData( ) {
            @Override
            public void getSportDetailData(int step, int calorie, int energy) {
                mstep=step;
                mcalorie=calorie;
                menergy=energy;
                mflag++;
                Log.d( TAG, "运动数据："+ mstep +"步，"+mcalorie+"卡路里,"+menergy );
                blueToothUtil.openHeartRateSwitch();
            }
        } );

        blueToothUtil.setIsOpenHeart( new BlueToothUtil.openHeartSet( ) {
            @Override
            public void isHeartSetOpen() {
                //打开心率成功后过12秒获取数据
                Log.d( TAG, "打开心率了,等14秒出结果......" );
//                handler.sendEmptyMessageDelayed( 1, 14000 );
                sendDataToServer();
            }
        } );

        BlueToothChangeReceiver.registerObserver( this );
        BlueToothChangeReceiver.registerReceiver( this );

        NetStateChangeReceiver.registerObserver( this );
        NetStateChangeReceiver.registerReceiver( this );

    }

    public void getLocation() {
        mLocationManager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );
        location = getLocation( mLocationManager );
        latitude=location.getLatitude();
        longitude=location.getLongitude();
        mflag++;
        Log.d( TAG, "Latitude:" + latitude + ", Longitude:" + longitude );
    }

    private void sendDataToServer(){
//        String deviceId;
//        int gpsId;
//        int heartRate;
//        String latitude;//纬度
//        String longitude;//经度
//        int sportCal;
//        int sportEnergy;
//        int sportSteps;
//        String timestamp;
        WatchSummaryInfo watchSummaryInfo=new WatchSummaryInfo(deviceId,gpsId,heartRateData,String.valueOf( latitude ),String.valueOf( longitude ),mcalorie,menergy,mstep,timestamp);
        OkhttpUtil.getInstance().sendWatchSummary( watchSummaryInfo, new Callback( ) {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.d( TAG,res );
            }
        } );
    }

    private Location getLocation(LocationManager locationManager) {
        Location result = null;
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            result = locationManager.getLastKnownLocation( LocationManager.GPS_PROVIDER );
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
    public void onDestroy() {
        super.onDestroy( );
        BlueToothChangeReceiver.unRegisterObserver( this );
        BlueToothChangeReceiver.unRegisterReceiver( this );
        NetStateChangeReceiver.unRegisterObserver( this );
        NetStateChangeReceiver.unRegisterReceiver( this );
    }

    public static Intent newIntent(Context context){

        Intent intent = new Intent(context, PollingService.class);
        return intent;

    }

    public PollingService() {
        super(TAG);
    }

    @Override
    public void onBlueToothDisconnected() {
        Log.d( TAG, "onBlueToothDisconnected" );
    }

    @Override
    public void onBlueToothConnected() {
        Log.d( TAG, "onBlueToothConnected" );

    }

    @Override
    public void onNetDisconnected() {
        Log.d( TAG, "onNetDisconnected" );

    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        Log.d( TAG, "onNetConnected" );

    }
}
