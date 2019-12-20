package com.example.webdemo.jsinterface;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

import com.example.webdemo.R;

/**
 * Copyright (C), 2016-2020
 * FileName: JsInterfaceActivity
 * Author: wei.zheng
 * Date: 2019/12/20 17:43
 * Description: JsInterfaceActivity
 */
public class JsInterfaceActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "JsInterfaceActivity";
    private Button mBtnCallJs;
    private WebView mWebView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsinterface);

        mBtnCallJs = findViewById(R.id.btn_call_js);
        mWebView = findViewById(R.id.web_view);
        mBtnCallJs.setOnClickListener(this);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");

        mWebView.addJavascriptInterface(new WebViewUtils(mWebView), "androidInterface");
        mWebView.loadUrl("file:///android_asset/test.html");


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_js:
                handleGoBack();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleGoBack() {
        mWebView.evaluateJavascript("goBack()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.d(TAG, "onReceiveValue: " + value);
            }
        });
    }
}
