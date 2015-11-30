package com.hcy.suzhoubusquery.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by Administrator on 2015/11/30.
 */
public class NetworkUtils {


    /**
     *
     * 获取当前网络连接的类型信息
     *
     * @param context
     * @return 返回类型为1,ConnectivityManager.TYPE_MOBILE; 2,ConnectivityManager.TYPE_WIFI;
     */

    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
                return mNetworkInfo.getType();// 返回类型为1,ConnectivityManager.TYPE_MOBILE;
                // 2,ConnectivityManager.TYPE_WIFI;
            }
        }
        return -1;// -1表示网络不可用或者没有连接上
    }

    /**
     * 判断是否有网络连接
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isConnected();// isConnected表示连接上,isAvailable表示可用
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isConnected();// isConnected表示连接上,isAvailable表示可用
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isConnected();// isConnected表示连接上,isAvailable表示可用
            }
        }
        return false;
    }

    /**
     * 检测手机是否开启GPRS网络,需要调用ConnectivityManager,TelephonyManager 服务.
     *
     * @param context
     * @return boolean
     */
    public static boolean checkGprsNetwork(Context context) {
        boolean has = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        int netType = info.getType();
        int netSubtype = info.getSubtype();
        if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {
            has = info.isConnected();
        }
        return has;

    }

    /**
     * 检测手机是否开启WIFI网络,需要调用ConnectivityManager服务.
     *
     * @param context
     * @return boolean
     */
    public static boolean checkWifiNetwork(Context context) {
        boolean has = false;
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        int netType = info.getType();
        if (netType == ConnectivityManager.TYPE_WIFI) {
            has = info.isConnected();
        }
        return has;
    }

    /**
     * 检测当前手机是否联网
     *
     * @param context
     * @return boolean
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 手机是否处在漫游
     *
     * @param mCm
     * @return boolean
     */
    public static boolean isNetworkRoaming(Context mCm) {
        ConnectivityManager connectivity = (ConnectivityManager) mCm.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        }
        NetworkInfo info = connectivity.getActiveNetworkInfo();
        boolean isMobile = (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE);
        TelephonyManager mTm = (TelephonyManager) mCm.getSystemService(Context.TELEPHONY_SERVICE);
        boolean isRoaming = isMobile && mTm.isNetworkRoaming();
        return isRoaming;
    }


}
