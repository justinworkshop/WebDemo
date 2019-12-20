package com.example.webdemo.keyboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.webdemo.R;
import com.example.webdemo.utils.SpUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Copyright (C), 2016-2020
 * FileName: KeyboardActivity
 * Author: wei.zheng
 * Date: 2019/11/20 16:40
 * Description: 键盘
 */
public class KeyboardActivity extends AppCompatActivity implements TextWatcher {

    private static final int TYPE_NUM = 1;
    private static final int TYPE_BIG = 2;
    private static final int TYPE_SMALL = 3;
    private static final int TYPE_MARK = 4;

    private ImageView ivBack;
    private BaseEditText etPassword;
    private TextView btnConfirm;
    private TextView btnNum, btnBig, btnSmall, btnMark;
    private RecyclerView rcvKeyboard;
    private KeyboardItemAdapter keyboardItemAdapter;
    private ImageView ivDelete;
    private ImageView ivSlideBg, ivSlideBtn;

    private List<String> numList = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0");
    private List<String> smallList = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z");
    private List<String> bigList = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
    private List<String> markList = Arrays.asList("!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "_", "+", "-", "=", "`", "~", "{", "}", "[", "]", ";", ":", "\'", "\""
            , "<", ">", ",", ".", "?", "/", "|", "\\", " ");

    private StringBuilder password = new StringBuilder();

    private int scrollY;
    private int slideRealHeight; //滚动条所需滑动真实高度
    private int rcvRealHeight;  //RecyclerView所需滚动真实高度
    private boolean isTop;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        initView();
        initList();
        initListener();
        showKeyboard();
    }

    private void initView() {
        ivBack = findViewById(R.id.iv_back);
        etPassword = findViewById(R.id.et_password);
        btnConfirm = findViewById(R.id.btn_connect);
        btnNum = findViewById(R.id.btn_num);
        btnBig = findViewById(R.id.btn_big);
        btnSmall = findViewById(R.id.btn_small);
        btnMark = findViewById(R.id.btn_mark);
        rcvKeyboard = findViewById(R.id.rcv_keyboard);
        ivDelete = findViewById(R.id.iv_delete);
        ivSlideBg = findViewById(R.id.iv_slide_bg);
        ivSlideBtn = findViewById(R.id.iv_slide_btn);

        setSelect(TYPE_NUM);
    }

    private void initList() {
        keyboardItemAdapter = new KeyboardItemAdapter();
        keyboardItemAdapter.setOnItemClickListener(new KeyboardItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                password.append(keyboardItemAdapter.getDataList().get(position));
                etPassword.setText(password);
            }
        });

        rcvKeyboard.setLayoutManager(new GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false));
        keyboardItemAdapter.setDataList(numList);
        rcvKeyboard.setAdapter(keyboardItemAdapter);
        getPercent();
    }

    private void showKeyboard() {
        etPassword.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.VISIBLE);
        ivDelete.setVisibility(View.VISIBLE);

        etPassword.setFocusable(true);
        etPassword.setFocusableInTouchMode(true);
        etPassword.requestFocus();
    }

    private void initListener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(TYPE_NUM);
                setDateList(TYPE_NUM);
            }
        });

        btnBig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(TYPE_BIG);
                setDateList(TYPE_BIG);
            }
        });

        btnSmall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(TYPE_SMALL);
                setDateList(TYPE_SMALL);
            }
        });

        btnMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSelect(TYPE_MARK);
                setDateList(TYPE_MARK);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.putString("ip", etPassword.getText().toString().trim());
                finish();
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        //长点删除所有内容
        ivDelete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteAll();
                return true;
            }
        });

        etPassword.addTextChangedListener(this);

        rcvKeyboard.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                scrollY = scrollY + dy;

                //为了防止滚动条滑出上方
                if (scrollY < 0) {
                    scrollY = 0;
                }
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivSlideBtn.getLayoutParams();
                if (isTop) {
                    //进度条背景上面阴影边距为9px
                    layoutParams.topMargin = 9;
                    scrollY = 0;
                    ivSlideBtn.setLayoutParams(layoutParams);
                    isTop = false;
                } else {
                    layoutParams.topMargin = (int) (scrollY * getPercent() + 9);
                    ivSlideBtn.setLayoutParams(layoutParams);
                }

            }
        });
    }

    private void delete() {
        if (password == null || password.length() == 0) {
            return;
        }
        password.deleteCharAt(password.length() - 1);
        etPassword.setText(password);
    }

    private void deleteAll() {
        password = new StringBuilder();
        etPassword.setText(password);
    }

    private void setSelect(int type) {
        btnNum.setSelected(type == TYPE_NUM);
        btnBig.setSelected(type == TYPE_BIG);
        btnSmall.setSelected(type == TYPE_SMALL);
        btnMark.setSelected(type == TYPE_MARK);
    }

    private void setDateList(int type) {
        isTop = true;
        switch (type) {
            case TYPE_NUM:
                keyboardItemAdapter.setDataList(numList);
                break;
            case TYPE_BIG:
                keyboardItemAdapter.setDataList(bigList);
                break;
            case TYPE_SMALL:
                keyboardItemAdapter.setDataList(smallList);
                break;
            case TYPE_MARK:
                keyboardItemAdapter.setDataList(markList);
                break;
        }
        //切换数据后滚动到最上面
        keyboardItemAdapter.notifyDataSetChanged();
        rcvKeyboard.scrollToPosition(0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) ivSlideBtn.getLayoutParams();
        layoutParams.topMargin = 9;
        scrollY = 0;
        ivSlideBtn.setLayoutParams(layoutParams);
    }

    /**
     * 获得滚动条相对于recyclerView所需滚动比例
     *
     * @return
     */
    private float getPercent() {
        slideRealHeight = ivSlideBg.getHeight() - ivSlideBtn.getHeight();
        int residue = keyboardItemAdapter.getItemCount() % 3;
        int lines = residue == 0 ? keyboardItemAdapter.getItemCount() / 3 : keyboardItemAdapter.getItemCount() / 3 + 1;
        int rcvHeight = lines * 50;  //  50就是当前item的高度
        rcvRealHeight = rcvHeight - rcvKeyboard.getHeight();
        return (float) slideRealHeight / (float) rcvRealHeight;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //光标移动到内容最后面
        etPassword.setSelection(s.length());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (etPassword != null) {
            etPassword.removeTextChangedListener(this);
            etPassword.clearFocus();
        }
    }
}
