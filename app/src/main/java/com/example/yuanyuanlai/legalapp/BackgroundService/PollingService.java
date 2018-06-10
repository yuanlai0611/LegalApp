package com.example.yuanyuanlai.legalapp.BackgroundService;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.yuanyuanlai.legalapp.BlueTooth.BlueToothChangeObserver;
import com.example.yuanyuanlai.legalapp.BlueTooth.BlueToothChangeReceiver;
import com.example.yuanyuanlai.legalapp.Internet.NetStateChangeObserver;
import com.example.yuanyuanlai.legalapp.Internet.NetStateChangeReceiver;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;

import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public class PollingService extends IntentService implements NetStateChangeObserver, BlueToothChangeObserver {
    private LocationManager mLocationManager;
    private Location location;
    private double latitude,longitude;

    private static final String TAG = "PollingService";

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        Log.d( TAG, "启动了服务" );


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
        Log.d( TAG, "Latitude:" + latitude + ", Longitude:" + longitude );
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
