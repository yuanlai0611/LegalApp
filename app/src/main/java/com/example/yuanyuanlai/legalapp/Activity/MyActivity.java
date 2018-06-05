package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.R;

public class MyActivity extends BaseActivity {

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,MyActivity.class);
        return intent;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_my);
    }

    @Override
    public void findViewById() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public void onClick(View v) {

    }
}
