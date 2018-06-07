package com.example.yuanyuanlai.legalapp.Utils;

import android.util.Log;

import com.example.yuanyuanlai.legalapp.Bean.PhoneVerification;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkhttpUtil {

    private static final String TAG = "OkhttpUtil";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static OkhttpUtil mOkhttpUtil;
    private OkHttpClient mOkhttpClient;

    private OkhttpUtil() {
        mOkhttpClient = new OkHttpClient();
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

    public void login(String phone, int verificationCode, Callback callback){
        PhoneVerification phoneVerification = new PhoneVerification(phone, verificationCode);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("phone",phone);
        jsonObject.addProperty("verificationCode",verificationCode);
        String json = jsonObject.toString();
        Log.d(TAG, json);
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("http://47.94.100.108:8080/iot_server/user/sms")
                .post(requestBody)
                .build();
        mOkhttpClient.newCall(request).enqueue(callback);
    }
}
