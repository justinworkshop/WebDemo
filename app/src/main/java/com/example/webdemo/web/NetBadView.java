package com.example.webdemo.web;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.webdemo.R;
import com.example.webdemo.utils.NetUtils;

/**
 * Copyright (C), 2016-2020
 * FileName: NetBadView
 * Author: wei.zheng
 * Date: 2019/12/20 15:04
 * Description: NetBadView
 */
public class NetBadView extends LinearLayout {
    private Button btn_frush;
    private LinearLayout ll_network_error;
    private OnRetryListener onRetryListener;

    public NetBadView(Context context) {
        super(context);
    }

    public NetBadView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.view_network_error, this, true);
        ll_network_error = view.findViewById(R.id.ll_network_error);
        btn_frush = view.findViewById(R.id.view_network_reload_button);
        btn_frush.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onRetryListener.doRetry();
            }
        });
    }

    public void setOnRetryListener(OnRetryListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    public void checkNet(Context context) {
        if (!NetUtils.isNetworkConnected(context)) {
            setShow();
        } else {
            setHide();
        }
    }

    public void setShow() {
        ll_network_error.setVisibility(View.VISIBLE);
    }

    public void setHide() {
        ll_network_error.setVisibility(View.GONE);
    }

    public interface OnRetryListener {
        public void doRetry();
    }
}
