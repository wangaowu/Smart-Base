package com.bytemiracle.base.framework.view.recyclerTools;

import com.bytemiracle.base.framework.listener.QuickListListener;
import com.scwang.smartrefresh.layout.adapter.SmartRecyclerAdapter;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/1/15 8:17
 */
public class QuickAdapter<T> extends SmartRecyclerAdapter<T> {
    private QuickListListener<T> quickListListener;

    public QuickAdapter(int layoutId, QuickListListener<T> quickListListener) {
        super(layoutId);
        this.quickListListener = quickListListener;
    }

    @Override
    protected void onBindViewHolder(SmartViewHolder holder, T item, int position) {
        if (quickListListener != null) {
            holder.itemView.setOnClickListener(v -> quickListListener.onClickItem(this, item));
            holder.itemView.setOnLongClickListener(v -> quickListListener.onLongClickItem(this, v, item));
            quickListListener.onBindItem(this, holder, item);
        }
    }
}
