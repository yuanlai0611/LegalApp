package com.example.yuanyuanlai.legalapp.Base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.R;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener,EasyPermissions.PermissionCallbacks{

    private static final int RC_CAMERA = 1000;
    private OpenCamera mOpenCamera;

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
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
    }

    public abstract void setContentView();

    public abstract void initListener();

    public abstract void findViewById();

    public abstract void askPermission();

    private void setFullScreen(){
        View view = getWindow().getDecorView();
        int options = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        view.setSystemUiVisibility(options);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        if (requestCode == RC_CAMERA){
            Toast.makeText(this,"同意了相机权限",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (requestCode == RC_CAMERA){
            Toast.makeText(this,"拒绝了相机权限",Toast.LENGTH_SHORT).show();
        }

        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }

    }

    @AfterPermissionGranted(RC_CAMERA)
    public void askForCamera(){

        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)){
            mOpenCamera.openCamera();
        }else{
            PermissionRequest request = new PermissionRequest.Builder(this,RC_CAMERA,Manifest.permission.CAMERA).build();
            EasyPermissions.requestPermissions(request);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp,
                this.getResources().getDisplayMetrics());
    }

    public interface OpenCamera{
        abstract void openCamera();
    }
}
