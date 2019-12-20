package com.example.webdemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.webdemo.AppInit;


/**
 * Copyright (C), 2015-2018
 * FileName: SpUtil
 * Author: wei.zheng
 * Date: 2018/10/31 15:51
 * Description:
 * sharedPreference 帮助类
 */
public class SpUtil {

    private static final String FILE_NAME = "AppSp";

    public static final SharedPreferences sp = AppInit.getApplication().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

    public static void putString(String key, String value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(String key, String defaultValue) {
        return sp.getString(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return sp.getBoolean(key, defaultValue);
    }

    public static long getLong(String key, long defaultValue) {
        return sp.getLong(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static int getInt(String key, int defaut) {
        return sp.getInt(key, defaut);
    }

    public static void putInt(String key, int value) {
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static String getString(String key, SharedPreferences preferences) {
        if (preferences == null) {
            preferences = sp;
        }
        return preferences.getString(key, "");
    }

    public static void remove(String key, SharedPreferences preferences) {
        preferences.edit().remove(key).apply();
    }

    public static void remove(String key) {
        sp.edit().remove(key).apply();
    }
}
