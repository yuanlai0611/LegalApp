package com.example.yuanyuanlai.legalapp.Utils;

import android.content.Context;
import android.util.TypedValue;

public class ScreenUtil {

    public static int dp2px(int dp, Context context) {
        return (int) TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());

    }

}
