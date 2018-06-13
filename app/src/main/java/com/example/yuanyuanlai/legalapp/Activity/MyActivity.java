package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;
import com.example.yuanyuanlai.legalapp.Utils.BlueToothUtil;

public class MyActivity extends BaseActivity{

    private TextView phoneNumber,watchName,userName,userID,deviceID;
    private BlueToothUtil blueToothUtil;
    private String number,name,watchId,userId,bluetoothcode;//身份证号,蓝牙配码
    private final String TAG="MyActivity";
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("loginStatus", MODE_PRIVATE );
        number = sharedPreferences.getString("phone" ,"");
        name = sharedPreferences.getString("userName", "" );
        userId = sharedPreferences.getString("idCard", "");
        watchId = sharedPreferences.getString("bluetoothId", "");

        blueToothUtil = new BlueToothUtil();
        blueToothUtil.getWatchId();
        blueToothUtil.setDeviceId( new BlueToothUtil.DeviceId( ) {
            @Override
            public void getDeviceId(String id) {
                bluetoothcode = id;
                setUserData();
            }
        } );

    }

    public static Intent newIntent(Context context){

        Intent intent = new Intent(context,MyActivity.class);
        return intent;

    }

    private void setUserData(){

        phoneNumber.setText( "手机号:"+number );
        watchName.setText( "设配号:"+watchId );
        userName.setText( "姓名:"+name );
        userID.setText( "身份证:"+userId );
        deviceID.setText( "蓝牙配码:"+bluetoothcode );

    }

    @Override
    public void setContentView() {

        setContentView(R.layout.activity_my);

    }

    @Override
    public void findViewById() {

        phoneNumber = findViewById(R.id.phoneNumber);
        watchName = findViewById(R.id.watch_name);
        userName = findViewById(R.id.name_textview);
        userID = findViewById(R.id.userid);
        deviceID = findViewById(R.id.device_id);
        backButton = findViewById(R.id.back_button);

    }

    @Override
    public void initListener() {
        phoneNumber.setOnClickListener(this);
        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phoneNumber:
//                AlarmSummaryInfo alarmSummaryInfo=new AlarmSummaryInfo( AlarmType.BLUETOOTH_DISCONNECT.getAlarmId(),"ahoowl102375408a","250","250","2018-06-10T04:51:14.673Z" );
//                OkhttpUtil.getInstance().alarmSummaryInfo( alarmSummaryInfo, new okhttp3.Callback( ) {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        Log.d( TAG,"上传失败！！！！！！" );
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        String result = response.body().string();
//                        Log.d( TAG,result );
//                    }
//                } );
                break;
            case R.id.back_button:
                finish();
                break;
            default:
                break;
        }
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
