package com.example.yuanyuanlai.legalapp.Utils;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.yuanyuanlai.legalapp.R;
import com.wang.avi.AVLoadingIndicatorView;

public class DialogUtil {

    public static AlertDialog showProgressDialog(Context context, String text, boolean cancelable){
        View view= LayoutInflater.from(context).inflate
                ( R.layout.toast_loading,null);
        AVLoadingIndicatorView avl=(AVLoadingIndicatorView) view.findViewById(R.id.avl);
        avl.show();
        TextView tv=view.findViewById(R.id.tv);
        tv.setText(text);
        AlertDialog dialog=new AlertDialog.Builder(context,R.style.CustomDialog)
                .setView(view)
                .setCancelable(cancelable)
                .create();
        return dialog;
    }

}
