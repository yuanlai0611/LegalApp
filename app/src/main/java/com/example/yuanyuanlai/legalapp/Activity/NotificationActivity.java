package com.example.yuanyuanlai.legalapp.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.R;

import java.util.Calendar;

public class NotificationActivity extends BaseActivity implements View.OnClickListener{
    private RelativeLayout choose_date_re;
    private ImageButton btn_back;
    private PopupWindow popupWindow;
    private LinearLayout all_message,select_date;
    private int fromYear,fromMonth,fromDay;

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,NotificationActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        initWindow();
    }

    @Override
    public void setContentView() {
        setContentView( R.layout.activity_notification );
    }

    @Override
    public void initListener() {
        btn_back.setOnClickListener(this);
        popupWindow = new PopupWindow(this);
        choose_date_re.setOnClickListener(this);
    }

    @Override
    public void findViewById() {
        choose_date_re=findViewById( R.id.relat_choose_date );
        btn_back=findViewById( R.id.ib_back );
    }

    @Override
    public void askPermission() {

    }

    private void showPopupWindow() {

        // 设置布局文件
        popupWindow.setContentView( LayoutInflater.from(this).inflate(R.layout.popup_window, null));
        // 设置pop透明效果
        popupWindow.setBackgroundDrawable(new ColorDrawable( Color.WHITE));
        // 设置pop出入动画
        popupWindow.setAnimationStyle(R.style.pop_add);
        // 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        popupWindow.setFocusable(true);
        // 设置pop可点击，为false点击事件无效，默认为true
        popupWindow.setTouchable(true);
        // 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        popupWindow.setOutsideTouchable(true);
        // 相对于 + 号正下面，同时可以设置偏移量
        popupWindow.showAsDropDown(choose_date_re, -10, dp2px( 1 ));

        // 设置pop关闭监听，用于改变背景透明度
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });
        all_message = (LinearLayout)popupWindow.getContentView().findViewById(R.id.all_message);
        select_date = (LinearLayout)popupWindow.getContentView().findViewById(R.id.select_date);
        all_message.setOnClickListener(this);
        select_date.setOnClickListener(this);
    }

    public void initWindow(){
        //透明状态栏
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener( ) {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Toast.makeText( NotificationActivity.this, year + "-" + (month + 1) + "-" + dayOfMonth, Toast.LENGTH_SHORT ).show( );
            fromYear = year;
            fromMonth = month+1;
            fromDay = dayOfMonth;
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.relat_choose_date:
                showPopupWindow();
                break;
            case R.id.ib_back:
                finish();
                break;
            case R.id.all_message:
                popupWindow.dismiss();
                Toast.makeText( this,"全部通知",Toast.LENGTH_SHORT ).show();
                break;
            case R.id.select_date:
                popupWindow.dismiss();
                Calendar calendar = Calendar.getInstance();
                //获取系统的日期
                //年
                int year = calendar.get(Calendar.YEAR);
                //月
                int month = calendar.get(Calendar.MONTH)+1;
                //日
                int day = calendar.get(Calendar.DAY_OF_MONTH);
//                Toast.makeText( MainActivity.this,year+"  "+month+"  "+day,Toast.LENGTH_SHORT ).show();
//                //获取系统时间
//                //小时
//                int hour = calendar.get(Calendar.HOUR_OF_DAY);
//                //分钟
//                int minute = calendar.get(Calendar.MINUTE);
//                //秒
//                int second = calendar.get(Calendar.SECOND);
                DatePickerDialog dialog = new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, dateSetListener, year, month-1, day);
                dialog.show();
                break;
            default:
        }
    }

    private void setDateFrom(){

    }

}
