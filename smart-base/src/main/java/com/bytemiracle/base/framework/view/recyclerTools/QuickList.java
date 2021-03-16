package com.bytemiracle.base.framework.view.recyclerTools;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

import com.bytemiracle.base.framework.listener.QuickListListener;

import java.util.List;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/1/15 8:11
 */
public class QuickList<T> {
    private static QuickList instance;

    private QuickList() {
    }

    public static QuickList instance() {
        if (instance == null) {
            instance = new QuickList();
        }
        return instance;
    }

    /**
     * 适配recyclerView
     *
     * @param recyclerView      滑动容器布局
     * @param itemLayoutId      条目布局id
     * @param items             数据集
     * @param quickListListener 必须实现的监听器
     * @return
     */
    public QuickAdapter adapter(RecyclerView recyclerView, int itemLayoutId, List<T> items, QuickListListener<T> quickListListener) {
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        QuickAdapter quickAdapter = new QuickAdapter(itemLayoutId, quickListListener);
        quickAdapter.setOpenAnimationEnable(false);
        recyclerView.setAdapter(quickAdapter);
        quickAdapter.refresh(items);
        return quickAdapter;
    }
}


