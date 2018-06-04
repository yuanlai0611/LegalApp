package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yuanyuanlai.legalapp.R;

public class CheckResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_result);
    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,CheckResultActivity.class);
        return  intent;
    }

}
