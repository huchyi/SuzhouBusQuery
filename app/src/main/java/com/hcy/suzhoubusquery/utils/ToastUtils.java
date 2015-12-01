package com.hcy.suzhoubusquery.utils;


import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.hcy.suzhoubusquery.R;

/**
 * Toast消息工具类,可通过设置TOAST变量控制普通消息开关
 */
public class ToastUtils {

    public static boolean TOAST_CENTER = false;// 是否居中显示，默认是false；

    private static Toast mToast;

    public static void show(final Context context, final int resId) {
        show(context, resId, Toast.LENGTH_SHORT);
    }

    public static void show(final Context context, final CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(final Context context, final int resId, int duration) {
        show(context, resId, duration);
    }

    public static void show(final Context context, final CharSequence text, int duration) {
        if (context != null && !TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(context, text, duration);
            } else {
                mToast.setText(text);
                mToast.setDuration(duration);
            }
            if (TOAST_CENTER) {
                mToast.setGravity(Gravity.CENTER, 0, 0);
            } else {
                mToast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, context.getResources()
                        .getDimensionPixelOffset(R.dimen.toast_margin_buttom));
            }
            mToast.show();
        }
    }

    public static void showCenter(final Context context, final CharSequence text, int duration) {
        if (context != null && !TextUtils.isEmpty(text)) {
            if (mToast == null) {
                mToast = Toast.makeText(context, text, duration);
            } else {
                mToast.setText(text);
                mToast.setDuration(duration);
            }
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
        }
    }
}
