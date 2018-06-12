package com.example.yuanyuanlai.legalapp.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.yuanyuanlai.legalapp.Adapter.NotificationAdapter;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Bean.ItemNotification;
import com.example.yuanyuanlai.legalapp.Bean.NotificationArrayBean;
import com.example.yuanyuanlai.legalapp.Bean.NotificationBean;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;
import com.example.yuanyuanlai.legalapp.Utils.DialogUtil;
import com.example.yuanyuanlai.legalapp.Utils.OkhttpUtil;
import com.google.gson.Gson;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class NotificationActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout chooseDateRelativeLayout;
    private ImageButton backButton;
    private RecyclerView recyclerViewNotification;
    private int fromYear, fromMonth, fromDay;
    private TextView dateTextView;
    private static final String TAG = "NotificationActivity";
    private String phone, start, end;
    private NotificationAdapter notificationAdapter;
    private List<ItemNotification> notificationList = new ArrayList<>();
    private GetNotification getNotification;
    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getSharedPreferences("loginStatus", MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", "");
        start = getCurrentDate();
        end = getNextDate();
        Log.d(TAG, "END-->"+end);
        super.onCreate(savedInstanceState);
        loadingDialog = DialogUtil.showProgressDialog(this, "加载通知", false);
        initRecyclerView();
        loadingDialog.show();
        getNotification = new GetNotification();
        getNotification.execute();
        dateTextView.setText("今天");

    }

    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,NotificationActivity.class);
        return intent;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_notification);
    }

    @Override
    public void initListener() {
        backButton.setOnClickListener(this);
        chooseDateRelativeLayout.setOnClickListener(this);
    }

    @Override
    public void findViewById() {
        chooseDateRelativeLayout = (RelativeLayout)findViewById(R.id.relat_choose_date);
        backButton = (ImageButton)findViewById(R.id.ib_back);
        recyclerViewNotification = (RecyclerView)findViewById(R.id.recyclerView_notification);
        dateTextView = (TextView)findViewById(R.id.choose_date_textView);
    }


    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener( ) {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Toast.makeText( NotificationActivity.this, year + "-" + (month + 1) + "-" + dayOfMonth, Toast.LENGTH_SHORT ).show( );
            fromYear = year;
            fromMonth = month+1;
            fromDay = dayOfMonth;
            start = getYYYYMMDD(fromYear, fromMonth, fromDay);
            end = getNextDate(start);
            Log.d(TAG, "fromYear："+fromYear+" fromMonth："+fromMonth+" fromDay："+fromDay);
            if (Integer.parseInt(start) > Integer.parseInt(end)){
                Toast.makeText(NotificationActivity.this, "日期选择错误", Toast.LENGTH_SHORT).show();
            }{
                dateTextView.setText(fromYear + "-" + fromMonth + "-" + fromDay);
                loadingDialog.show();
                getNotification = new GetNotification();
                getNotification.execute();
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.relat_choose_date:
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(this, DatePickerDialog.THEME_HOLO_LIGHT, dateSetListener, year, month, day);
                dialog.show();
                break;
            case R.id.ib_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onNetDisconnected() {

    }

    @Override
    public void onNetConnected(NetworkType networkType) {

    }

    @Override
    public void onBlueToothDisconnected() {

    }

    @Override
    public void onBlueToothConnected() {

    }

    private class GetNotification extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... mVoids) {

            OkhttpUtil.getInstance().getNotification(phone, start, end, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "请求失败");
                        loadingDialog.dismiss();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    Gson gson = new Gson();
                    NotificationArrayBean notificationArrayBean = gson
                            .fromJson(response.body().string(), NotificationArrayBean.class);
                   if (notificationArrayBean.getStatus().getCode() == 1){

                       notificationList.clear();
                       Log.d(TAG, "获取数据成功");
                       List<NotificationBean> notificationBeanList = notificationArrayBean.getObject();

                       if (notificationBeanList == null){


                       }else {
                           for (NotificationBean notificationBean : notificationBeanList){
                               ItemNotification itemNotification = new ItemNotification(notificationBean.getContent(), notificationBean.getCreateTime(), ItemNotification.SHOW_MESSAGES);
                               notificationList.add(itemNotification);
                           }
                           runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   notificationAdapter.notifyDataSetChanged();
                               }
                           });
                       }



                   }else {
                        Log.d(TAG, "获取数据失败");
                   }


                    loadingDialog.dismiss();

                }
            });
            return null;
        }
    }

    private void initRecyclerView(){
        notificationAdapter = new NotificationAdapter(this, notificationList);
        recyclerViewNotification.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewNotification.setAdapter(notificationAdapter);
    }

    private String getYYYYMMDD(int year, int month, int day){

        StringBuilder result = new StringBuilder();
        result.append(String.valueOf(year));
        if (month >= 10){
            result.append(String.valueOf(month));
        }else {
            result.append("0").append(String.valueOf(month));
        }
        if (day >= 10){
            result.append(String.valueOf(day));
        }else {
            result.append("0").append(String.valueOf(day));
        }
        return result.toString();

    }

    private String parseDate(String createTime){

        createTime = createTime.replaceAll("-", "");
        createTime = createTime.replaceAll(":", "");
        createTime = createTime.replaceAll(" ", "");
        return createTime;

    }

    private String getCurrentDate(){

        //获取系统当前的时间
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return getYYYYMMDD(year, month, day);

    }



    private String getNextDate(){

        //获取明天的时间
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);
        date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);

    }

    private String getNextDate(String dateString) {

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException mE) {
            mE.printStackTrace();
        }
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.DATE, 1);
        date = calendar.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(date);

    }

    private void initNotification(){

    }
}
