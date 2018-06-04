package com.example.yuanyuanlai.legalapp.Guide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.Activity.HomeActivity;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.R;

public class GuideActivity extends BaseActivity {

    private final int GO_MAIN = 1000;
    private final int TIME = 3000;
    private Intent mIntent;

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case GO_MAIN:
                    mIntent = HomeActivity.newIntent(GuideActivity.this);
                    startActivity(mIntent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void onClick(View v) {

    }
}
