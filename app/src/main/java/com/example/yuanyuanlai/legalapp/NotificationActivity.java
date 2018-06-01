package com.example.yuanyuanlai.legalapp;

import android.app.DatePickerDialog;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Calendar;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout choose_date_re;
    private ImageButton btn_back;
    private PopupWindow popupWindow;
    private LinearLayout all_message,select_date;
    private int fromYear,fromMonth,fromDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_notification );
        initWindow();
        choose_date_re=findViewById( R.id.relat_choose_date );
        btn_back=findViewById( R.id.ib_back );
        btn_back.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                finish();
            }
        } );
        popupWindow = new PopupWindow(this);
        choose_date_re.setOnClickListener( new View.OnClickListener( ) {
            @Override
            public void onClick(View view) {
                showPopupWindow();
            }
        } );
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

    public int dp2px(int dp) {
        return (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp,
                this.getResources().getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        super.onResume();
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
//                Toast.makeText( this,"选择日期",Toast.LENGTH_SHORT ).show();
//                final DatePicker date = new DatePicker(MainActivity.this);
//                date.setCalendarViewShown(false);
//                date.setSpinnersShown( true );
////                date.init(0, 0, 0, new DatePicker.OnDateChangedListener() {
////                    @Override
////                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
////                    }
////                });
//                AlertDialog.Builder mDatePickerDialogBuilder = new AlertDialog.Builder(MainActivity.this);
//                mDatePickerDialogBuilder.setView(date);
//                mDatePickerDialogBuilder.setTitle("请选择日期");
//                mDatePickerDialogBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        //只有点击确定按钮时，才更改时间，并设置在文本中显示
//                        fromYear = date.getYear();
//                        fromMonth = date.getMonth()+1;
//                        fromDay = date.getDayOfMonth();
//                        setDateFrom();
//                    }
//                });
//                mDatePickerDialogBuilder.setNegativeButton("取消",null);
//                mDatePickerDialogBuilder.show();
                break;
            default:
        }
    }

    private void setDateFrom(){

    }

}
