package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;

public class CheckResultActivity extends BaseActivity {

    @Override
    public void setContentView() {

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
        setContentView(R.layout.activity_check_result);
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,CheckResultActivity.class);
        return  intent;
    }



}
