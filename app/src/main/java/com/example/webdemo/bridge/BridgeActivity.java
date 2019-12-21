package com.example.webdemo.bridge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;

import com.example.webdemo.R;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.github.lzyzsd.jsbridge.DefaultHandler;

/**
 * Copyright (C), 2016-2020
 * FileName: BridgeActivity
 * Author: wei.zheng
 * Date: 2019/12/20 15:23
 * Description: BridgeActivity
 */
public class BridgeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "BridgeActivityTag";

    private Button mBtnCallJS;
    private BridgeWebView mBridgeWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        mBtnCallJS = findViewById(R.id.btn_call_js);
        mBtnCallJS.setOnClickListener(this);
        mBridgeWebView = findViewById(R.id.bridge_web_view);

        mBridgeWebView.setWebChromeClient(new WebChromeClient());
        mBridgeWebView.loadUrl("file:///android_asset/demo.html");

        mBridgeWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.d(TAG, "handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data from Java");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_call_js:
                callJsMethod();
                break;
            default:
                break;
        }
    }

    private void callJsMethod() {
        mBridgeWebView.callHandler("functionInJs", "我是Java数据", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Log.d(TAG, "onCallBack:" + data);
            }
        });
    }
}
