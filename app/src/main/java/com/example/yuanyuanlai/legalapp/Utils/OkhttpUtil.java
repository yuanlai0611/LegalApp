package com.example.yuanyuanlai.legalapp.Utils;

import android.util.Log;

import com.example.yuanyuanlai.legalapp.Bean.AlarmSummaryInfo;
import com.example.yuanyuanlai.legalapp.Bean.PhoneVerification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpUtil {

    private static final String TAG = "OkhttpUtil";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkhttpUtil mOkhttpUtil;
    private OkHttpClient mOkhttpClient;

    private OkhttpUtil() {
        mOkhttpClient = new OkHttpClient.Builder()
        .cookieJar(new CookieJar() {

            private HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();
            //上一个请求url
            private HttpUrl url;

            @Override
            public void saveFromResponse(HttpUrl httpUrl, List<Cookie> cookies) {

                Log.d(TAG, cookies.toString());
                cookieStore.put(url, cookies);
                url = httpUrl;

            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {

                List<Cookie> cookies = cookieStore.get(url);
                return cookies != null ? cookies : new ArrayList<Cookie>();

            }
        }).build();

    }

    public synchronized static OkhttpUtil getInstance(){
        if (mOkhttpUtil == null){
            mOkhttpUtil = new OkhttpUtil();
        }
        return mOkhttpUtil;
    }

    public void getVerificationCode(String phoneNumber, Callback callback){

        Request request = new Request.Builder()
                .url("http://47.94.100.108:8080/iot_server/user/sms/"+phoneNumber)
                .get()
                .build();
        mOkhttpClient.newCall(request).enqueue(callback);

    }

    public void alarmSummaryInfo(AlarmSummaryInfo alarmSummaryInfo,Callback callback){
        String json = new Gson().toJson( alarmSummaryInfo );
        RequestBody requestBody = RequestBody.create( JSON,json );
        Request request = new Request.Builder()
                .url( "http://47.94.100.108:8080/iot_server/alarm" )
                .post( requestBody )
                .build();
        mOkhttpClient.newCall( request ).enqueue( callback );
    }

    public void login(String phone, int verificationCode, String cookie, Callback callback){

        PhoneVerification a = new PhoneVerification( phone,verificationCode );
        String json =new Gson().toJson( a );
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .addHeader("cookie", cookie)
                .url("http://47.94.100.108:8080/iot_server/user/sms")
                .post(requestBody)
                .build();

        mOkhttpClient
                .newCall(request)
                .enqueue(callback);

    }

    public void uploadFile(String phone, File file, Callback callback){

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"),file);
        String fileName = phone + System.currentTimeMillis() + ".jpg";
        MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("uploadFile", fileName, requestBody)
                .build();

        Request request = new Request.Builder()
                .url("http://47.94.100.108:8080/iot_server/face/faceCheck/" + phone)
                .post(multipartBody).build();

        mOkhttpClient.newBuilder().readTimeout(30000, TimeUnit.MILLISECONDS)
                .build().newCall(request).enqueue(callback);

    }

    public void getNotification(String phone, String start, String end, Callback callback){

        Request request = new Request.Builder()
                .url("http://47.94.100.108:8080/iot_server/push/date/" + phone + "/" + start + "/" +end)
                .get()
                .build();
        mOkhttpClient.newCall(request).enqueue(callback);


    }

}
