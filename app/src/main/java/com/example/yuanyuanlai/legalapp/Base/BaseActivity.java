package com.example.yuanyuanlai.legalapp.Base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.BlueTooth.BlueToothChangeObserver;
import com.example.yuanyuanlai.legalapp.BlueTooth.BlueToothChangeReceiver;
import com.example.yuanyuanlai.legalapp.Internet.NetStateChangeObserver;
import com.example.yuanyuanlai.legalapp.Internet.NetStateChangeReceiver;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks, NetStateChangeObserver,BlueToothChangeObserver {

    private static final int RC_CAMERA = 1000;
    private static final int RC_LOCATION = 1001;
    private OpenCamera mOpenCamera = null;
    private GetLocation mGetLocation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView();
        findViewById();
        initListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        NetStateChangeReceiver.registerObserver(this);
        BlueToothChangeReceiver.registerObserver(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        NetStateChangeReceiver.unRegisterObserver(this);
        BlueToothChangeReceiver.unRegisterObserver(this);
    }

    public abstract void setContentView();

    public abstract void initListener();

    public abstract void findViewById();


    private void setFullScreen() {
        View view = getWindow().getDecorView();
        int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        view.setSystemUiVisibility(options);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        if (requestCode == RC_CAMERA) {
            Toast.makeText(this, "同意了相机权限", Toast.LENGTH_SHORT).show();
        }else if (requestCode == RC_LOCATION){
            Toast.makeText(this, "同意了位置权限", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (requestCode == RC_CAMERA) {
            Toast.makeText(this, "拒绝了相机权限", Toast.LENGTH_SHORT).show();
        }else if (requestCode == RC_LOCATION){
            Toast.makeText(this, "拒绝了定位权限", Toast.LENGTH_SHORT).show();
        }

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }

    }

    @AfterPermissionGranted(RC_CAMERA)
    public void askForCamera() {

        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mOpenCamera.openCamera();
        }  else {
            PermissionRequest request = new PermissionRequest.Builder(this, RC_CAMERA, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE).build();
            EasyPermissions.requestPermissions(request);
        }

    }


    @SuppressLint("MissingPermission")
    @AfterPermissionGranted(RC_LOCATION)
    public void askForLocation() {

        if (EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            mGetLocation.getLocation();
        }else {
            PermissionRequest request = new PermissionRequest.Builder(this,RC_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION).build();
            EasyPermissions.requestPermissions(request);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }


    public void setOpenCamera(OpenCamera openCamera) {
        mOpenCamera = openCamera;
    }

    public void setGetLocation(GetLocation getLocation){
        mGetLocation = getLocation;
    }

    public interface OpenCamera{
        void openCamera();
    }

    public interface GetLocation{
        void getLocation();
    }

}
