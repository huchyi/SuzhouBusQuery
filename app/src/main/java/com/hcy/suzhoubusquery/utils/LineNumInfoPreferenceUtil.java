package com.hcy.suzhoubusquery.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.hcy.suzhoubusquery.MyApplication;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/1.
 *
 * LineNumInfoPreferenceUtil
 */
public class LineNumInfoPreferenceUtil {

    // LineNum
    public static final class LineNumKey {
        public static final String LINE_NUM_JSON = "line_num_json";
        public static final String LINE_STATION_JSON = "line_station_json";
        public static final String LINE_NUM = "line_num";
        public static final String LINE_DRICTION = "line_driction";
        public static final String LINE_GUID = "line_guid";
    }

    // station
    public static final class KidInfoKey {
    }

    private static SharedPreferences sp;
    static {
        sp = MyApplication.getInstances().getSharedPreferences("user_fav", Context.MODE_PRIVATE);
    }

    public static SharedPreferences getSp() {
        return sp;
    }

    public static boolean contains(String key) {
        return sp.contains(key);
    }

    public static String getValue(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public static void setValue(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static int getValue(String key, int defValue) {
        return sp.getInt(key, defValue);
    }

    public static void setValue(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static boolean getValue(String key, boolean defValue) {
        return sp.getBoolean(key, defValue);
    }

    public static void setValue(String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void setValues(HashMap<String, Object> values) {
        Iterator<Map.Entry<String, Object>> iterator = values.entrySet().iterator();
        Map.Entry<String, Object> entry = null;
        SharedPreferences.Editor editor = sp.edit();
        while (iterator.hasNext()) {
            entry = iterator.next();
            Object value = entry.getValue();
            if (value instanceof String) {
                editor.putString(entry.getKey(), (String) value);
            } else if (value instanceof Boolean) {
                editor.putBoolean(entry.getKey(), (Boolean) value);
            } else if (value instanceof Integer) {
                editor.putInt(entry.getKey(), (Integer) value);
            }
        }
        editor.commit();
    }

    public static void clear() {
        sp.edit().clear().commit();
    }



}
