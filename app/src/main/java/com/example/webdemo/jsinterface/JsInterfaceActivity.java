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
import android.widget.EditText;

import com.example.webdemo.R;

/**
 * Copyright (C), 2016-2020
 * FileName: JsInterfaceActivity
 * Author: wei.zheng
 * Date: 2019/12/20 17:43
 * Description: JsInterfaceActivity
 */
public class JsInterfaceActivity extends AppCompatActivity {
    private EditText mEtInput;
    private Button mBtnCallJs;
    private WebView mWebView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsinterface);

        mEtInput = findViewById(R.id.et_input);
        mBtnCallJs = findViewById(R.id.btn_call_js);
        mWebView = findViewById(R.id.web_view);

        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");

        mWebView.addJavascriptInterface(new WebViewUtils(mWebView), "androidInterface");
        mWebView.loadUrl("file:///android_asset/test.html");

        mBtnCallJs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.d("PET", "click");
                mWebView.evaluateJavascript("goBack()", new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        Log.d("PET", "onReceiveValue " + value);
                    }
                });
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBackPressed() {
        mWebView.evaluateJavascript("goBack()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                Log.d("PET", "goBack() return " + value);
                if (value.equals("0")) {
                    finish();
                }
            }
        });
    }
}
