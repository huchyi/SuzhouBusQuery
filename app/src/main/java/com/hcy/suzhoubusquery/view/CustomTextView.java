package com.hcy.suzhoubusquery.view;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.widget.TextView;

import com.hcy.suzhoubusquery.MyApplication;
import com.hcy.suzhoubusquery.R;

/**
 * Created by hcy on 2015/12/23.
 *
 * CustomTextView
 */
public class CustomTextView extends TextView{
    public CustomTextView(Context context) {
        super(context);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setTextStr(final float timeNum){
        if(timeNum > 0){
            new CountDownTimer((int)timeNum * 60 * 1000, 60 * 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    int num = (int)(millisUntilFinished / (60 * 1000));
                    String timeStr = num > 60 ? (num / 60 + "小时" + num % 60 + "分钟"):(num + "分钟");
                    setText("约"+ timeStr + "到站");
                }

                @Override
                public void onFinish() {
                    setText(MyApplication.getInstances().getString(R.string.station_detail_list_time_str));
                }
            }.start();
        }else{
            setText(MyApplication.getInstances().getString(R.string.station_detail_list_time_str));
        }
    }

}
