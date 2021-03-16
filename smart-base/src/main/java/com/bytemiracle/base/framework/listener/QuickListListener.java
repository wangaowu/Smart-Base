package com.bytemiracle.base.framework.listener;

import android.view.View;

import com.bytemiracle.base.framework.view.recyclerTools.QuickAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/1/15 8:20
 */
public abstract class QuickListListener<T> {

    public abstract void onBindItem(QuickAdapter<T> adapter, SmartViewHolder holder, T model);

    public void onClickItem(QuickAdapter<T> adapter, T model) {
    }

    public boolean onLongClickItem(QuickAdapter<T> adapter, View itemView, T model) {
        return false;
    }
}
