package com.example.webdemo.web;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;

/**
 * Copyright (C), 2016-2020
 * FileName: WebViewDownLoadListener
 * Author: wei.zheng
 * Date: 2019/12/20 15:10
 * Description: WebViewDownLoadListener
 */
public class WebViewDownLoadListener implements DownloadListener {
    private Context mContext;

    public WebViewDownLoadListener(Context context) {
        this.mContext = context;
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                long contentLength) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);

        if (mContext != null && mContext instanceof Activity) {
            mContext.startActivity(intent);
        }
    }
}
