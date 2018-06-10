package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;

public class CheckResultActivity extends BaseActivity {

    private TextView checkResultTextView;
    private TextView checkWordTextView;
    private ImageView backImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        boolean isFirst = intent.getBooleanExtra("isFirst", false);
        boolean isSuccess = intent.getBooleanExtra("isSuccess", false);
        if (isFirst&&isSuccess){
            checkResultTextView.setText("识别成功");
            checkWordTextView.setText("第一次人脸识别成功");
        }else if (!isFirst&&isSuccess){
            checkResultTextView.setText("识别成功");
            checkWordTextView.setText("你已成功认证");

        }else if (isFirst&&!isSuccess){
            checkResultTextView.setText("识别失败");
            checkWordTextView.setText("第一次人脸识别失败");

        }else {

            checkResultTextView.setText("识别失败");
            checkWordTextView.setText("认证失败");

        }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_check_result);
    }

    @Override
    public void initListener() {

        backImageView.setOnClickListener(this);

    }

    @Override
    public void findViewById() {

        checkResultTextView = (TextView)findViewById(R.id.check_result);
        checkWordTextView = (TextView)findViewById(R.id.check_word);
        backImageView = (ImageView)findViewById(R.id.back);

    }

    @Override
    public void onNetDisconnected() {

    }

    @Override
    public void onNetConnected(NetworkType networkType) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            default:
                break;
        }
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,CheckResultActivity.class);
        return  intent;
    }

    @Override
    public void onBlueToothDisconnected() {

    }

    @Override
    public void onBlueToothConnected() {

    }
}
