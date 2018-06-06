package com.example.yuanyuanlai.legalapp.Login;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;

import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;

public class LoginActivity extends BaseActivity {

    private Button loginButton,sendButton;
    private TextView countDown;
    private ValueAnimator animator;

    public static Intent newIntent(Context context){
        Intent intent=new Intent( context,LoginActivity.class );
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    public void initListener() {
        sendButton.setOnClickListener( this );
        loginButton.setOnClickListener( this );
    }

    @Override
    public void findViewById() {
        loginButton=findViewById( R.id.loginButton );
        sendButton=findViewById( R.id.sendButton );
        countDown=findViewById( R.id.countDown );
    }

    private void initButton(){
        sendButton.setEnabled( true );
        countDown.setText( "时间" );
    }

    @Override
    public void onNetDisconnected() {

    }

    @Override
    public void onNetConnected(NetworkType networkType) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sendButton:
                sendButton.setEnabled( false );
                //确认网络请求发送后
                animator = ValueAnimator.ofInt(59, 0);
                animator.setDuration(60000);
                animator.setInterpolator(new LinearInterpolator());
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator valueAnimator) {
                        int value = (int)valueAnimator.getAnimatedValue();
                        countDown.setText(String.valueOf(value));
                        if (value<1){
                            initButton();
                        }
                    }
                });
                animator.start();
                break;
            case R.id.loginButton:
                //登录

                animator.end();
                sendButton.setEnabled( true );
                break;
        }
    }
}
