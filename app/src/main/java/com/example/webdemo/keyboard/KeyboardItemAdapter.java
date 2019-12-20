package com.example.webdemo.keyboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.webdemo.R;

import java.util.List;

/**
 * Copyright (C), 2016-2020
 * FileName: KeyboardItemAdapter
 * Author: wei.zheng
 * Date: 2019/11/20 16:40
 * Description: 键盘适配器
 */
public class KeyboardItemAdapter extends RecyclerView.Adapter<KeyboardItemAdapter.KeyboardViewHolder> {

    private List<String> dataList;
    private OnItemClickListener onItemClickListener;

    public void setDataList(List<String> dataList) {
        this.dataList = dataList;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public KeyboardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_keyboard_item, viewGroup, false);
        return new KeyboardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull KeyboardViewHolder keyboardViewHolder, final int i) {
        String string = dataList.get(i);
        keyboardViewHolder.tvItem.setText(string);
        if (string.equals(" ")) {
            keyboardViewHolder.tvItem.setText("空格");
        }

        keyboardViewHolder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class KeyboardViewHolder extends RecyclerView.ViewHolder {

        private TextView tvItem;

        public KeyboardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(R.id.tv_keyboard_item);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
