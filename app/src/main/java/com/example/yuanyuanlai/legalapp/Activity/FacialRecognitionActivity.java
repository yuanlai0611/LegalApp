package com.example.yuanyuanlai.legalapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.yuanyuanlai.legalapp.Base.BaseActivity;
import com.example.yuanyuanlai.legalapp.Internet.NetworkType;
import com.example.yuanyuanlai.legalapp.R;

public class FacialRecognitionActivity extends BaseActivity implements BaseActivity.OpenCamera {

    private final int TAKE_PHOTO_REQUEST = 1;
    private Button takePhotoAgainButton;
    private Button uploadButton;
    private ImageView userImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setOpenCamera(this);
        askForCamera();
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
    public void findViewById() {
        takePhotoAgainButton = (Button)findViewById(R.id.takePhotoAgainButton);
        uploadButton = (Button)findViewById(R.id.uploadButton);
        userImageView = (ImageView)findViewById(R.id.user_picture);
    }

    @Override
    public void initListener() {

        takePhotoAgainButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        setOpenCamera(this);

    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.takePhotoAgainButton:
                askForCamera();
                break;
            case R.id.uploadButton:
                break;
            default:
                break;
        }

    }

    @Override
    public void openCamera(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,TAKE_PHOTO_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode){

            case TAKE_PHOTO_REQUEST:
                if (requestCode == RESULT_CANCELED){
                    Toast.makeText(this,"取消了拍照",Toast.LENGTH_SHORT).show();
                    return;
                }
                Bitmap photo = data.getParcelableExtra("data");
                userImageView.setImageBitmap(photo);
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
}
