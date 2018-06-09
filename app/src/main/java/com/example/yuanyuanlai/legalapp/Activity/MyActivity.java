package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;
import com.example.yuanyuanlai.legalapp.utils.BlueToothUtil;

public class MyActivity extends BaseActivity{
    private TextView phoneNumber;
    private String number,name,watchId,userId;
    private int mtag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        SharedPreferences sharedPreferences=getSharedPreferences( "loginStatus", MODE_PRIVATE );
        number=sharedPreferences.getString( "userPhoneNumber" ,"");
        BlueToothUtil blueToothUtil = new BlueToothUtil(MyActivity.this);
        blueToothUtil.getWatchId();
        blueToothUtil.setDeviceId( new BlueToothUtil.DeviceId( ) {
            @Override
            public void getDeviceId(String id) {
                Toast.makeText( MyActivity.this,id,Toast.LENGTH_SHORT ).show();
                mtag++;
                if (mtag>=4) setUserData();
            }
        } );


    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,MyActivity.class);
        return intent;
    }

    private void setUserData(){
        phoneNumber.setText( number );
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_my);
    }

    @Override
    public void findViewById() {
        phoneNumber=findViewById( R.id.phoneNumber );
    }

    @Override
    public void initListener() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onNetDisconnected() {

    }

    @Override
    public void onNetConnected(NetworkType networkType) {

    }


    @Override
    public void onBlueToothDisconnected() {

    }

    @Override
    public void onBlueToothConnected() {

    }
}
