package com.example.yuanyuanlai.legalapp.Login;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yuanyuanlai.legalapp.Activity.HomeActivity;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Bean.LoginBean;
import com.example.yuanyuanlai.legalapp.Bean.StatusBean;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;
import com.example.yuanyuanlai.legalapp.Utils.DialogUtil;
import com.example.yuanyuanlai.legalapp.Utils.OkhttpUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.List;
import cn.jpush.android.api.JPushInterface;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;

public class LoginActivity extends BaseActivity{

    private Button loginButton, sendButton;
    private TextView countDown;
    private EditText phoneEditText, verificationCodeEditText;
    private ValueAnimator animator;
    private static final String TAG = "LoginActivity";
    private GetVerificationCodeTask getVerificationCodeTask;
    private LoginTask loginTask;
    private String cookie;
    private AlertDialog mloadingDialog;

    public static Intent newIntent(Context context){
        Intent intent=new Intent( context,LoginActivity.class );
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mloadingDialog = DialogUtil.showProgressDialog( LoginActivity.this,"登录中...",false );
        MayRequestLocation();

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
        loginButton = findViewById( R.id.loginButton );
        sendButton = findViewById( R.id.sendButton );
        countDown = findViewById( R.id.countDown );
        phoneEditText = findViewById(R.id.phone_number);
        verificationCodeEditText = findViewById(R.id.verification_code);
    }

    private void MayRequestLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE}, 12);
        }
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
                Log.d(TAG, "点击了sendButton");
                if (!phoneEditText.getText().toString().isEmpty()){
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
                    getVerificationCodeTask = new GetVerificationCodeTask();
                    getVerificationCodeTask.execute();
                }else{
                    Toast.makeText(this, "手机号为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.loginButton:
                Log.d(TAG, "点击了login按钮");
                //登录
                if (!verificationCodeEditText.getText().toString().isEmpty()&&!(cookie == null)){
                    loginTask = new LoginTask();
                    loginTask.execute();
                    mloadingDialog.show();
                    if (animator!=null){
                        animator.end();
                    }
                    sendButton.setEnabled( true );
                }else if (TextUtils.isEmpty( cookie )){
                    Toast.makeText(this, "请先获取验证码", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "验证码没有填齐", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @SuppressLint("StaticFieldLeak")
    class GetVerificationCodeTask extends AsyncTask<Void, Void, Void>  {

        @Override
        protected Void doInBackground(Void... mVoids) {

            OkhttpUtil.getInstance().getVerificationCode(phoneEditText.getText().toString(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "获取验证码失败~~");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    Headers headers = response.headers();
                    Log.d(TAG, "headers: "+headers.toString());
                    List<String> cookies = headers.values("Set-Cookie");
                    String session = cookies.get(0);
                    cookie = session.substring(0, session.indexOf(";"));
                    Gson gson = new Gson();
                    StatusBean statusBean = gson.fromJson(response.body().string(), StatusBean.class);
                    if (statusBean.getStatus().getCode() == 1){
                        Log.d(TAG, "获取验证码成功");
                    }else {
                        Log.d(TAG, "获取验证码失败");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mloadingDialog.dismiss();
                                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }
            });

            return null;

        }

    };

    @SuppressLint("StaticFieldLeak")
    class LoginTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... mVoids) {

            Log.d(TAG, "发起了网络请求");

          OkhttpUtil.getInstance().login(phoneEditText.getText().toString(), Integer.parseInt(verificationCodeEditText.getText().toString()), cookie, new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          mloadingDialog.dismiss();
                          Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                      }
                  });

                  Log.d(TAG, "登录失败");
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {

                  //Log.d(TAG, response.body().string());
                  Gson gson = new Gson();
                  LoginBean loginBean = gson.fromJson(response.body().string(), LoginBean.class);
                  if (loginBean.getStatus().getCode() == 1){
                      Log.d(TAG, loginBean.getStatus().getMessage());

                      //初始化极光推送
                      String phoneNumber = loginBean.getObject().getPhone();
                      JPushInterface.setAlias(LoginActivity.this, 0, phoneNumber);

                      SharedPreferences sharedPreferences = getSharedPreferences("loginStatus", MODE_PRIVATE);
                      SharedPreferences.Editor editor = sharedPreferences.edit();
                      editor.putBoolean("isLogin", true);

                      editor.putString("bluetoothId", loginBean.getObject().getBluetoothId());
                      editor.putString("createTime", loginBean.getObject().getCreateTime());
                      editor.putString("deviceId", loginBean.getObject().getDeviceId());
                      editor.putString("idCard", loginBean.getObject().getIdCard());
                      editor.putBoolean("faceCheck", loginBean.getObject().isFaceCheck());
                      editor.putString("phone", loginBean.getObject().getPhone());
                      editor.putString("userName", loginBean.getObject().getUserName());
                      editor.apply();

                      //登录成功
                      Intent intent = HomeActivity.newIntent(LoginActivity.this);
                      startActivity(intent);
                      finish();
                      Log.d(TAG, "登录成功");
                  }else if (loginBean.getStatus().getCode() == 0){
                      Log.d(TAG, loginBean.getStatus().getMessage());
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              mloadingDialog.dismiss();
                              Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                          }
                      });

                  }else {
                      runOnUiThread(new Runnable() {
                          @Override
                          public void run() {
                              mloadingDialog.dismiss();
                              Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_SHORT).show();
                          }
                      });

                      Log.d(TAG, "登录失败");
                  }
              }
          });

            return null;
        }
    };



    @Override
    public void onBlueToothDisconnected() {
    }

    @Override
    public void onBlueToothConnected() {
    }
}
