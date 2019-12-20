package com.example.webdemo.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Copyright (C), 2016-2020
 * FileName: NetUtils
 * Author: wei.zheng
 * Date: 2019/12/20 15:07
 * Description: NetUtils
 */
public class NetUtils {

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (mConnectivityManager == null) {
                return false;
            }
            NetworkInfo mNetworkInfo = mConnectivityManager
                    .getActiveNetworkInfo();
            return mNetworkInfo != null && mNetworkInfo.isConnected();
        }
        return false;
    }
}
