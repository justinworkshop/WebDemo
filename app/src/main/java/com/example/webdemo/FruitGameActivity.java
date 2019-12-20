package com.example.webdemo;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Copyright (C), 2016-2020
 * FileName: FruitGameActivity
 * Author: wei.zheng
 * Date: 2019/11/20 16:40
 * Description: 水果游戏
 */
public class FruitGameActivity extends AppCompatActivity {

    private WebView mWebView;
    private long mStart;
    private long mEnd;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStart = System.currentTimeMillis();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web_view);

        mWebView = findViewById(R.id.web_view);
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return false;
            }
        });

        String mode = getIntent().getStringExtra("mode");
        if ("net".equals(mode)) {
            mWebView.loadUrl("http://g.alicdn.com/tmapp/hilodemos/3.0.7/fruit-ninja/index.html");
        } else if ("assets".equals(mode)) {
            mWebView.loadUrl("file:///android_asset/fruit/index.html");
        } else if ("sdcard".equals(mode)) {
            String sdcardPath = Environment.getExternalStorageDirectory().getAbsolutePath();
            mWebView.loadUrl("file:///" + sdcardPath + "/fruit/index.html");
        } else if ("define".equals(mode)) {
            String ip = getIntent().getStringExtra("ip");
            String url = "http://" + ip + ":3200/index.html";
            mWebView.loadUrl(url);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEnd = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();

            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
    }
}
