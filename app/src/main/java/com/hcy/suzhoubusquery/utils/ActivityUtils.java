package com.hcy.suzhoubusquery.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/12/3.
 *
 *
 * ActivityUtils
 */
public class ActivityUtils {

    public static boolean checkApkExist(Context context, String className) {
        Intent intent = new Intent();
        intent.setClassName("com.hcy.suzhoubusquery", className);
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent, 0);
        return list.size() > 0;
    }
}
