package com.hcy.suzhoubusquery.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.hcy.suzhoubusquery.MyApplication;

/**
 * Created by Administrator on 2015/11/30.
 *
 *
 */
public class MethodUtils {

    /**  line num  */
    public static String slMethod(){
        int i = 0;
        if(i == 1){
            int b = 1/0;
            return b+"";
        }
        return "SearchBusLine";
    }

    /**  line Direction  */
    public static String ldMethod(){
        int i = 0;
        if(i == 1){
            int b = 1/0;
            return b+"";
        }
        return "GetBusLineDetail";
    }

    /**  Fuzzy name  */
    public static String ssMethod(){
        int i = 0;
        if(i == 1){
            int b = 1/0;
            return b+"";
        }
        return "SearchBusStation";
    }

    /**  Detailed name  */
    public static String gsMethod(){
        int i = 0;
        if(i == 1){
            int b = 1/0;
            return b+"";
        }
        return "GetBusStationDetail";
    }


    /**  获取IMEI码  */
    public static String IMEIMath(Context context){
        SharedPreferences sp = context.getSharedPreferences("imeimath",Activity.MODE_PRIVATE);
        String getstr = sp.getString("imei","");

        StringBuilder str = new StringBuilder();
        if(getstr.equals("")){
            int a;
            for (int i = 0; i < 15; i++) {
                a = (int)(10 * Math.random());
                str.append(a);
            }

            SharedPreferences.Editor editor = sp.edit();
            editor.putString("imei", str.toString());
            editor.apply();
        }else{
            str.append(getstr);
        }

      return str.toString();
    }

}
