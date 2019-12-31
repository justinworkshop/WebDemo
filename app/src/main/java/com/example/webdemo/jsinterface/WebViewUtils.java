package com.example.webdemo.jsinterface;

import android.app.Activity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Copyright (C), 2016-2020
 * FileName: WebViewUtils
 * Author: wei.zheng
 * Date: 2019/12/20 17:50
 * Description: WebViewUtils
 */
public class WebViewUtils {
    private static final String TAG = "WebViewUtils";
    private Activity activity;

    public WebViewUtils(Activity activity) {
        this.activity = activity;
    }

    @JavascriptInterface
    public void goBack() {
        activity.finish();
        Toast.makeText(activity.getApplicationContext(), "GoBack", Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public int getUserScore() {
        return 200;
    }

    @JavascriptInterface
    public void setUserScore(int score) {
        Log.d(TAG, "Score from web:" + score);
    }
}
