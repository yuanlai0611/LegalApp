package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;
import com.example.yuanyuanlai.legalapp.utils.BlueToothUtil;

public class MyActivity extends BaseActivity{
    private TextView phoneNumber;
    private String number,name,watchId,userId,bluetoothcode;//身份证号,蓝牙配码
    //获取个人信息标识位，当标识位到达5时才去setData显示数据
    private int mtag = 0;
    private final String TAG="MyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        SharedPreferences sharedPreferences=getSharedPreferences( "loginStatus", MODE_PRIVATE );
        number=sharedPreferences.getString( "userPhoneNumber" ,"");
        if (!TextUtils.isEmpty( number )){
            mtag++;
        }
//        Log.d( TAG,"-----------"+number );
        BlueToothUtil blueToothUtil = new BlueToothUtil(MyActivity.this);
        blueToothUtil.getHeartRateData();
//        blueToothUtil.getWatchId();
        blueToothUtil.setDeviceId( new BlueToothUtil.DeviceId( ) {
            @Override
            public void getDeviceId(String id) {
                watchId = id;
                mtag++;
                if (mtag>=5) setUserData();
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
