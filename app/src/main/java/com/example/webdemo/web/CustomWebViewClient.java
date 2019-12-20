package com.example.webdemo.web;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2016-2020
 * FileName: CustomWebViewClient
 * Author: wei.zheng
 * Date: 2019/12/20 14:57
 * Description: CustomWebViewClient
 */
public class CustomWebViewClient extends WebViewClient {
    private OnWebViewListener onWebViewListener;

    public interface OnWebViewListener {
        void onPageStarted(WebView view, String url, Bitmap favicon);

        void onPageFinished(WebView view, String url);

        void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error);

        void shouldOverrideUrlLoading(WebView view, String url);
    }

    public void setOnWebViewListener(OnWebViewListener listener) {
        this.onWebViewListener = listener;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        if (onWebViewListener != null) {
            onWebViewListener.onPageStarted(view, url, favicon);
        }
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        if (onWebViewListener != null) {
            onWebViewListener.onPageFinished(view, url);
        }
        super.onPageFinished(view, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
        if (onWebViewListener != null) {
            onWebViewListener.shouldOverrideUrlLoading(view, url);
        }

        if (url.contains("tel:")) {
            if (Build.VERSION.SDK_INT >= 23) {
                List<String> permissions = null;
                if (view.getContext().checkSelfPermission(Manifest.permission.CALL_PHONE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    permissions = new ArrayList<>();
                    permissions.add(Manifest.permission.CALL_PHONE);
                }
                if (permissions == null) {
                    openDialog(view, url);
                } else {
                    String[] permissionArray = new String[permissions.size()];
                    permissions.toArray(permissionArray);
                    // Request the permission. The result will be received
                    // in onRequestPermissionResult()
                    ((Activity) view.getContext()).requestPermissions(permissionArray, 2);
                }
            } else {
                openDialog(view, url);
            }
            return true;
        }else if (url.contains("mailto:")){

            Intent data=new Intent(Intent.ACTION_SENDTO);
            data.setData(Uri.parse(url));
//            data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
//            data.putExtra(Intent.EXTRA_TEXT, "这是内容");
            view.getContext().startActivity(data);
            return true;
        }

        return super.shouldOverrideUrlLoading(view, url);
    }

    private void openDialog(final WebView view, final String url) {
        new ConfirmDialog((Activity) view.getContext(), "温馨提示", "是否询问客服？", "取消", "确定",true) {
            @Override
            public void cancel() {
            }

            @Override
            public void confirm() {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(url));
                    view.getContext().startActivity(intent);
                } catch (Exception e) {

                }
            }
        };
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        Log.d("MAPP_WEB", "onReceivedError");
        if (onWebViewListener != null) {
            onWebViewListener.onReceivedError(view, request, error);
        }
        super.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        Log.d("MAPP_WEB", "onReceivedSslError___: " + error.getPrimaryError());
        if (error.getPrimaryError() == SslError.SSL_DATE_INVALID
                || error.getPrimaryError() == SslError.SSL_EXPIRED
                || error.getPrimaryError() == SslError.SSL_INVALID
                || error.getPrimaryError() == SslError.SSL_UNTRUSTED) {
            handler.proceed();
        } else {
            handler.cancel();
        }
    }
}
