package com.hcy.suzhoubusquery.net;

import android.content.Context;
import android.util.Log;

import com.hcy.suzhoubusquery.MainActivity;
import com.hcy.suzhoubusquery.MyApplication;
import com.hcy.suzhoubusquery.utils.MethodUtils;
import com.hcy.suzhoubusquery.utils.StringUtils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by hcy on 2015/11/30.
 *
 * HttpRequest
 *
 */
public class HttpRequest {


    private static HttpRequest mInstance = null;

    public static HttpRequest getInstances() {
        if (null == mInstance) {
            mInstance = new HttpRequest();
        }
        return mInstance;
    }


    private OkHttpClient mOkHttpClient;
    private Request request;

    public HttpRequest() {
        //创建okHttpClient对象
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
    }

    public interface ICallBack {


        void onSuccess(String json);

        void onFailure(String msg);

    }

    /**
     * line num
     **/
    public void getLineNumData(String input, final ICallBack callBack) {

        if (input != null && !input.equals("")) {
            String urlStr = MainActivity.SERVER_FILE
                    + "/Json?method=" + MethodUtils.slMethod()
                    + "&appVersion=" + MainActivity.APP_VERSION
                    + "&deviceID=" + MainActivity.DEVICE_ID
                    + "&lineName=" + input;

            request = new Request.Builder().url(urlStr).build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    callBack.onFailure(request.toString());
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String htmlStr = response.body().string();
                    callBack.onSuccess(htmlStr);
                }
            });
        } else {
            callBack.onFailure("请输入线路号");
        }
    }


    /**
     * line Direction
     **/
    public void getLineDirectionData(String input, final ICallBack callBack) {

        if (input != null && !input.equals("")) {
            String urlStr = MainActivity.SERVER_FILE
                    + "/Json?method=" + MethodUtils.ldMethod()
                    + "&appVersion=" + MainActivity.APP_VERSION
                    + "&deviceID=" + MainActivity.DEVICE_ID
                    + "&Guid=" + input;

            request = new Request.Builder().url(urlStr).build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    callBack.onFailure("");
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String htmlStr = response.body().string();
                    callBack.onSuccess(htmlStr);
                }
            });
        } else {
            callBack.onFailure("线路的编号为空");
        }
    }


    /**
     * Fuzzy name
     **/
    public void getFuzzyNameData(String input, final ICallBack callBack) {

        if (input != null && !input.equals("")) {
            String urlStr = MainActivity.SERVER_FILE
                    + "/Json?method=" + MethodUtils.ssMethod()
                    + "&appVersion=" + MainActivity.APP_VERSION
                    + "&deviceID=" + MainActivity.DEVICE_ID
                    + "&standName=" + input;

            request = new Request.Builder().url(urlStr).build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    callBack.onFailure("");
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String htmlStr = response.body().string();
                    callBack.onSuccess(htmlStr);
                }
            });
        } else {
            callBack.onFailure("请输入站台名");
        }
    }


    /**
     * Detailed name
     **/
    public void getDetailedNameData(String input, final ICallBack callBack) {

        if (input != null && !input.equals("")) {
            String urlStr = MainActivity.SERVER_FILE
                    + "/Json?method=" + MethodUtils.gsMethod()
                    + "&appVersion=" + MainActivity.APP_VERSION
                    + "&deviceID=" + MainActivity.DEVICE_ID
                    + "&NoteGuid=" + input;

            request = new Request.Builder().url(urlStr).build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {

                    callBack.onFailure("");
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String htmlStr = response.body().string();
                    callBack.onSuccess(htmlStr);
                }
            });
        } else {
            callBack.onFailure("站台名的编号为空");
        }
    }


    /**
     * 得到天气的信息
     **/
    public void getTempDate(final ICallBack callBack) {
            String urlStr = MainActivity.SERVER_FILE_TEMP + "101190401";
            request = new Request.Builder().url(urlStr).build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    callBack.onFailure("");
                }

                @Override
                public void onResponse(final Response response) throws IOException {
                    String htmlStr = response.body().string();
                    if(StringUtils.isNullOrNullStr(htmlStr)){
                        callBack.onFailure("");
                    }else{
                        callBack.onSuccess(htmlStr);
                    }
                }
            });
    }


}
