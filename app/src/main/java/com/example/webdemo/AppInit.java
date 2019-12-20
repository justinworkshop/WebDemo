package com.example.webdemo;

import android.app.Application;

/**
 * Copyright (C), 2016-2020
 * FileName: AppInit
 * Author: wei.zheng
 * Date: 2019/11/21 14:01
 * Description: AppInit
 */
public class AppInit extends Application {
    private static AppInit sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    public static AppInit getApplication() {
        return sInstance;
    }
}
