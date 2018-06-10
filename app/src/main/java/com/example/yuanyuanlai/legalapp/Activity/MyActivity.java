package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.AlarmType;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Bean.AlarmSummaryInfo;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;
import com.example.yuanyuanlai.legalapp.utils.BlueToothUtil;
import com.example.yuanyuanlai.legalapp.utils.OkhttpUtil;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Response;

public class MyActivity extends BaseActivity{
    private TextView phoneNumber;
    private Button getheartrate,openheartrate;
    BlueToothUtil blueToothUtil;
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
        blueToothUtil = new BlueToothUtil(MyActivity.this);
//        blueToothUtil.getWatchId();
        blueToothUtil.setDeviceId( new BlueToothUtil.DeviceId( ) {
            @Override
            public void getDeviceId(String id) {
                watchId = id;
                mtag++;
                if (mtag>=5) setUserData();
            }
        } );

        blueToothUtil.setHeartRate( new BlueToothUtil.HeartRate( ) {
            @Override
            public void getHeartRate(int heartrate) {
                Log.d( TAG, ""+heartrate );
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
        getheartrate=findViewById( R.id.getheartretedate );
        openheartrate=findViewById( R.id.openheartrate );
    }

    @Override
    public void initListener() {
        phoneNumber.setOnClickListener( this );
        getheartrate.setOnClickListener( this );
        openheartrate.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.phoneNumber:
                AlarmSummaryInfo alarmSummaryInfo=new AlarmSummaryInfo( AlarmType.BLUETOOTH_DISCONNECT.getAlarmId(),"ahoowl102375408a","250","250","2018-06-10T04:51:14.673Z" );
                OkhttpUtil.getInstance().alarmSummaryInfo( alarmSummaryInfo, new okhttp3.Callback( ) {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d( TAG,"上传失败！！！！！！" );
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Log.d( TAG,result );
                    }
                } );
                break;
            case R.id.getheartretedate:
                blueToothUtil.getHeartRateData();
                break;
            case R.id.openheartrate:
                blueToothUtil.openHeartRateSwitch();
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
