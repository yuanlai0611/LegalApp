package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.R;

public class HomeActivity extends BaseActivity {

    private Button facialRecognitionButton;
    private Button checkButton;
    private Button notificationButton;
    private Button myButton;
    private Intent intent;

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

    @Override
    public void findViewById() {
        facialRecognitionButton = (Button)findViewById(R.id.facialRecognitionButton);
        checkButton = (Button)findViewById(R.id.checkButton);
        notificationButton = (Button)findViewById(R.id.notificationButton);
        myButton = (Button)findViewById(R.id.myButton);
    }

    @Override
    public void askPermission() {

    }


    @Override
    public void onClick(View v) {
     switch (v.getId()){
         case R.id.facialRecognitionButton:
             intent = FacialRecognitionActivity.newIntent(this);
             startActivity(intent);
             break;
         case R.id.checkButton:
             break;
         case R.id.notificationButton:

             break;
         case R.id.myButton:
             intent = MyActivity.newIntent(this);
             startActivity(intent);
             break;
         default:
             break;
     }
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_home);
    }

}
