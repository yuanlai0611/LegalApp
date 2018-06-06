package com.example.yuanyuanlai.legalapp.Guide;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.Activity.HomeActivity;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.Login.LoginActivity;
import com.example.yuanyuanlai.legalapp.R;

public class GuideActivity extends BaseActivity {

    private final String isLogin = "isLogin";
    private final String loginStatus = "loginStatus";
    private final int GO_MAIN = 1000;
    private final int TIME = 3000;
    private Intent mIntent;
    private SharedPreferences sharedPreferences;

    @SuppressLint("HandlerLeak")
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GO_MAIN:
                    Boolean il=sharedPreferences.getBoolean( isLogin,false );
                    if(!il){
                        mIntent = LoginActivity.newIntent( GuideActivity.this );
                        startActivity( mIntent );
                        finish();
                    }else {
                        mIntent = HomeActivity.newIntent(GuideActivity.this);
                        startActivity(mIntent);
                        finish();
                    }

//                        mIntent = HomeActivity.newIntent(GuideActivity.this);
//                        startActivity(mIntent);
//                        finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences=getSharedPreferences( loginStatus,MODE_PRIVATE );
        mHandler.sendEmptyMessageDelayed(GO_MAIN, TIME);

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_guide);
    }

    @Override
    public void initListener() {

    }

    @Override
    public void findViewById() {

    }

    @Override
    public void onNetDisconnected() {

    }

    @Override
    public void onNetConnected(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {

    }
}
