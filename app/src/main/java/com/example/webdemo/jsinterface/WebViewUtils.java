package com.example.webdemo.jsinterface;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

/**
 * Copyright (C), 2016-2020
 * FileName: WebViewUtils
 * Author: wei.zheng
 * Date: 2019/12/20 17:50
 * Description: WebViewUtils
 */
public class WebViewUtils {
    private WebView webView;

    public WebViewUtils(WebView webView) {
        this.webView = webView;
    }

    @JavascriptInterface
    public int goBack() {
        return 1;
    }

    @JavascriptInterface
    public String getPet() {
        return "From  native pet";
    }

    @JavascriptInterface
    public void setPet(String pet) {
        Log.d("PET", "From js pet:" + pet);
    }

    @JavascriptInterface
    public int getUserScore() {
        return 200;
    }

    @JavascriptInterface
    public void setUserScore(int score) {
        Log.d("PET", "From js score:" + score);
    }
}
