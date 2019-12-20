package com.example.webdemo.bridge;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.Button;
import android.widget.EditText;

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
public class BridgeActivity extends AppCompatActivity {
    private static final String TAG = "BridgeActivityLog";
    private EditText mEtInput;
    private Button mBtnCallJS;
    private BridgeWebView mBridgeWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge);

        mEtInput = findViewById(R.id.et_input);
        mBtnCallJS = findViewById(R.id.btn_call_js);
        mBridgeWebView = findViewById(R.id.bridge_web_view);

        mBridgeWebView.setDefaultHandler(new DefaultHandler());
        mBridgeWebView.setWebChromeClient(new WebChromeClient());
        mBridgeWebView.loadUrl("file:///android_asset/demo.html");

        mBridgeWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Log.d(TAG, "js 调用 Native, 从js返回数据:"+data);

                function.onCallBack("JS调用Android的返回数据:" + mEtInput.getText().toString());
            }
        });

        mBtnCallJS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Native 调用 js");
                mBridgeWebView.callHandler("goBack", "Android调用js方法", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.d(TAG, "onCallBack:" + data);
                    }
                });
            }
        });
    }
}
