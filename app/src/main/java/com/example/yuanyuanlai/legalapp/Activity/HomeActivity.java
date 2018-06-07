package com.example.yuanyuanlai.legalapp.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;

public class HomeActivity extends BaseActivity implements BaseActivity.GetLocation{

    private Button facialRecognitionButton;
    private Button checkButton;
    private Button notificationButton;
    private Button myButton;
    private Intent intent;
    private LocationManager mLocationManager;
    private Location location;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setGetLocation(this);
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

    @SuppressLint("MissingPermission")
    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.facialRecognitionButton:
             intent = FacialRecognitionActivity.newIntent(this);
             startActivity(intent);
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
//             intent = MyActivity.newIntent(this);
//             startActivity(intent);
             askForLocation();
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

    }

    @Override
    public void onBlueToothConnected() {

    }
}
