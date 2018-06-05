package com.example.yuanyuanlai.legalapp.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
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


    private void showPopupWindow() {

        popupWindow.setContentView( LayoutInflater.from(this).inflate(R.layout.popup_window, null));
        popupWindow.setBackgroundDrawable(new ColorDrawable( Color.WHITE));
        popupWindow.setAnimationStyle(R.style.pop_add);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(choose_date_re);
        all_message = (LinearLayout)popupWindow.getContentView().findViewById(R.id.all_message);
        select_date = (LinearLayout)popupWindow.getContentView().findViewById(R.id.select_date);
        all_message.setOnClickListener(this);
        select_date.setOnClickListener(this);
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

                DatePickerDialog dialog = new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, dateSetListener, year, month-1, day);
                dialog.show();
                break;
            default:
        }
    }

    private void setDateFrom(){

    }

}
