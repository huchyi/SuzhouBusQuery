package com.hcy.suzhoubusquery;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.util.SimpleNodeIterator;

import java.io.IOException;


public class MainActivity extends Activity {
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
        //initPage();
        //extractKeyWordText(mUrl, "");

        Log.i("hcy", "=========================================================================");
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
//创建一个Request
        final Request request = new Request.Builder()
                .url("http://www.szjt.gov.cn/apts/APTSLine.aspx")
                .build();
//new call
        Call call = mOkHttpClient.newCall(request);
//请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("hcy", "ParserException:"+e.getMessage() + "request"+request.body().toString());
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                String htmlStr =  response.body().string();
                Log.i("hcy", "htmlStr:" + htmlStr);
            }
        });
    }


    // 循环访问所有节点，输出包含关键字的值节点
    public static void extractKeyWordText(String url, String keyword) {
        try {
            //生成一个解析器对象，用网页的 url 作为参数
            Parser parser = new Parser(url);
            //设置网页的编码,这里只是请求了一个 gb2312 编码网页
            parser.setEncoding("utf-8");
            //迭代所有节点, null 表示不使用 NodeFilter
            NodeList list = parser.parse(null);
            //从初始的节点列表跌倒所有的节点
            processNodeList(list, keyword);
        } catch (ParserException e) {
            Log.i("hcy", "ParserException:", e);
            e.printStackTrace();
        }
    }

    private static void processNodeList(NodeList list, String keyword) {
        //迭代开始
        SimpleNodeIterator iterator = list.elements();
        while (iterator.hasMoreNodes()) {
            Node node = iterator.nextNode();
            //得到该节点的子节点列表
            NodeList childList = node.getChildren();
            //孩子节点为空，说明是值节点
            if (null == childList) {
                //得到值节点的值
                String result = node.toPlainTextString();
                Log.i("hcy", "result:" + result);
                if (result.indexOf(keyword) != -1)
                    //若包含关键字，则简单打印出来文本
                    System.out.println(result);
            } //end if
            //孩子节点不为空，继续迭代该孩子节点
            else {
                processNodeList(childList, keyword);
            }//end else
        }//end wile
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

        // 不支持放大缩小
        webSettings.setSupportZoom(false);
        // 设置不出现缩放工具
        webSettings.setBuiltInZoomControls(false);

        // 设置可在大视野范围内上下左右拖动，并且可以任意比例缩放
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        //控制字体
        webSettings.setTextSize(WebSettings.TextSize.LARGEST);

        // Access-Control-Allow-Origin Error At Android 4.1
        if (Build.VERSION.SDK_INT >= 16) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }


        webView.getSettings().setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
//                view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
//                        "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
            }
        });
        webView.loadUrl(mUrl);
    }

    final class InJavaScriptLocalObj {

        @JavascriptInterface
        public void showSource(String html) {
            Log.i("hcy", html);
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