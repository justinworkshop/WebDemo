package com.example.webdemo.web;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.webdemo.R;

/**
 * Copyright (C), 2016-2020
 * FileName: ConfirmDialog
 * Author: wei.zheng
 * Date: 2019/12/20 14:59
 * Description: ConfirmDialog
 */
public abstract class ConfirmDialog {
    private TextView tv_confirm_title, tv_confirm_content, tv_confirm, tv_cancel;
    private TextView tv_midline;
    public AlertDialog alertDialog;
    private boolean cancelable;

    public ConfirmDialog(Activity activity, String title, String message, final String cancel, final String confirm, boolean cancelable) {
        this.cancelable = cancelable;
        if (activity.isFinishing()) {
            //防止出现空指针
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.dialog_custom_confirm, null);
            tv_confirm_title = dialogView.findViewById(R.id.tv_confirm_title);
            tv_confirm_content = dialogView.findViewById(R.id.tv_confirm_content);
            tv_cancel = dialogView.findViewById(R.id.tv_cancel);
            tv_confirm = dialogView.findViewById(R.id.tv_confirm);
            tv_midline = dialogView.findViewById(R.id.tv_midline);
            return;
        }
        final AlertDialog.Builder dialog = new AlertDialog.Builder(activity);
        dialog.setCancelable(this.cancelable);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_custom_confirm, null);
        tv_confirm_title = dialogView.findViewById(R.id.tv_confirm_title);
        tv_confirm_content = dialogView.findViewById(R.id.tv_confirm_content);
        tv_cancel = dialogView.findViewById(R.id.tv_cancel);
        tv_confirm = dialogView.findViewById(R.id.tv_confirm);
        tv_midline = dialogView.findViewById(R.id.tv_midline);
        dialog.setView(dialogView);
        alertDialog = dialog.create();
        alertDialog.show();
        Display display = activity.getWindowManager().getDefaultDisplay();
        Window w = alertDialog.getWindow();
        w.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.width = (int) (display.getWidth() * 0.8);
        lp.dimAmount = 0.2f;
        w.setAttributes(lp);

        tv_confirm_title.setText(title);
        tv_confirm_content.setText(message);
        tv_cancel.setText(cancel);
        tv_confirm.setText(confirm);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                alertDialog.dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
                alertDialog.dismiss();
            }
        });
    }

    public void setHiddenTitle(boolean flag) {
        if (flag) {
            tv_confirm_title.setVisibility(View.GONE);
        }
    }

    public void setHiddenCancel(boolean flag) {
        if (flag) {
            tv_midline.setVisibility(View.GONE);
            tv_cancel.setVisibility(View.GONE);
        }
    }

    public abstract void cancel();

    public abstract void confirm();
}
