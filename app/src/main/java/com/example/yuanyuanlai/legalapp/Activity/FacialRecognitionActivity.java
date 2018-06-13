package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Bean.CheckBean;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;
import com.example.yuanyuanlai.legalapp.Utils.BitmapUtil;
import com.example.yuanyuanlai.legalapp.Utils.OkhttpUtil;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FacialRecognitionActivity extends BaseActivity implements BaseActivity.OpenCamera {

    private final int TAKE_PHOTO_REQUEST = 1;
    private Button takePhotoAgainButton;
    private Button uploadButton;
    private File file;
    private ImageView userImageView;
    private String imageFilePath = Environment.getExternalStorageDirectory()
            .getAbsolutePath()+"/test/"+System.currentTimeMillis()+".jpg";
    private static final String TAG = "FacialActivity";
    private UploadFile uploadFile;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setOpenCamera(this);
        askForCamera();
        SharedPreferences sharedPreferences = getSharedPreferences("loginStatus", MODE_PRIVATE);
        phone = sharedPreferences.getString("phone", "");
        super.onCreate(savedInstanceState);

    }


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,FacialRecognitionActivity.class);
        return intent;
    }

    @Override
    public void setContentView() {
        setContentView(R.layout.activity_facial_recogonition);
    }

    @Override
    public void initListener() {
        takePhotoAgainButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);

    }

    @Override
    public void findViewById() {
        takePhotoAgainButton = (Button)findViewById(R.id.takePhotoAgainButton);
        uploadButton = (Button)findViewById(R.id.uploadButton);
        userImageView = (ImageView)findViewById(R.id.user_picture);
    }

    @Override
    public void openCamera() {

        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(imageFilePath);
        file.getParentFile().mkdirs();
        Uri uri = FileProvider.getUriForFile(this, "com.example.yuanyuanlai.legalapp.provider", file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, TAKE_PHOTO_REQUEST);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.takePhotoAgainButton:
                askForCamera();
                break;
            case R.id.uploadButton:
                BitmapUtil.compressImage(imageFilePath);
                uploadFile = new UploadFile();
                uploadFile.execute();

                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){

            case TAKE_PHOTO_REQUEST:
                if (resultCode == RESULT_CANCELED){
                    Toast.makeText(this, "取消了拍照", Toast.LENGTH_SHORT).show();
                    return;
                } else if (resultCode == RESULT_OK){
                    Toast.makeText(this, "拍照成功", Toast.LENGTH_SHORT).show();
                    userImageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
                }
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

    class UploadFile extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... mVoids) {
            OkhttpUtil.getInstance().uploadFile(phone, file, new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                    Log.d(TAG, "上传失败");
                    Toast.makeText(FacialRecognitionActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    Log.d(TAG, "上传成功");
                    Gson gson = new Gson();
                    CheckBean checkBean = gson.fromJson(response.body().string(), CheckBean.class);
                    if (checkBean.getStatus().getCode() == 1){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FacialRecognitionActivity.this, "第一次人脸识别成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d(TAG, "识别成功");
                        SharedPreferences sharedPreferences = getSharedPreferences("facialRecognition", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("isFirstSuccess", true);
                        editor.apply();
                        Intent intent = CheckResultActivity.newIntent(FacialRecognitionActivity.this);
                        intent.putExtra("isSuccess", true);
                        intent.putExtra("isFirst", true);
                        startActivity(intent);
                        finish();

                    }else if (checkBean.getStatus().getCode() == 0){

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FacialRecognitionActivity.this, "第一次人脸识识别失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d(TAG, "识别失败");
                        Intent intent = CheckResultActivity.newIntent(FacialRecognitionActivity.this);
                        intent.putExtra("isSuccess", false);
                        intent.putExtra("isFirst", true);
                        startActivity(intent);
                        finish();

                    }else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(FacialRecognitionActivity.this, "第一次人脸识别失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                        Log.d(TAG, "识别异常");

                    }

                }
            });
            return null;
        }


    }

}
