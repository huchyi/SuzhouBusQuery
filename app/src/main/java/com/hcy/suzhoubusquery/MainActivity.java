package com.hcy.suzhoubusquery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ScrollView;


public class MainActivity extends Activity{
    protected WebView webView;
    private ProgressBar progressBar;
    private String mUrl = "http://www.szjt.gov.cn/apts/APTSLine.aspx";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        initPage();
    }


    private void initPage() {
        webView = (WebView) findViewById(R.id.web_about_us);
        progressBar = (ProgressBar) findViewById(R.id.web_base_progress);
        webView.setScrollBarStyle(ScrollView.SCROLLBARS_OUTSIDE_OVERLAY);
        //设置webView支持文件上传
        webView.setWebChromeClient(new MyWebChromeClient());
        // 防止被放大
        webView.setInitialScale(100);
        //避免上传文件出现乱码
        webView.getSettings().setDefaultTextEncodingName("utf-8");

        WebSettings webSettings = webView.getSettings();

//        // 支持放大缩小
//        webSettings.setSupportZoom(true);
//        // 设置出现缩放工具
//        webSettings.setBuiltInZoomControls(true);

        // 不支持放大缩小
        webSettings.setSupportZoom(false);
        // 设置不出现缩放工具
        webSettings.setBuiltInZoomControls(false);

        // 设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webSettings.setUseWideViewPort(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //控制字体
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //webSettings.setTextZoom(32);

        // Access-Control-Allow-Origin Error At Android 4.1
        if (Build.VERSION.SDK_INT >= 16) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
        // 某些网站需要加载整个Dom
        webSettings.setDomStorageEnabled(true);


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
            }

        });
        webView.loadUrl(mUrl);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= 11) {
            webView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT >= 11) {
            webView.onPause();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        webView.loadUrl("about:blank");
        super.onDestroy();
    }



    private class MyWebChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}