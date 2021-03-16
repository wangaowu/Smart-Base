package com.bytemiracle.base.framework.view.selectWindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.listener.CommonAsyncListener;
import com.bytemiracle.base.framework.listener.QuickListListener;
import com.bytemiracle.base.framework.view.recyclerTools.QuickAdapter;
import com.bytemiracle.base.framework.view.recyclerTools.QuickList;
import com.scwang.smartrefresh.layout.adapter.SmartViewHolder;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import java.util.List;

/**
 * 类功能：功能选择器
 *
 * @author gwwang
 * @date 2021/2/1 15:37
 */
public class AppPopupSelector<T> {
    private static final String TAG = "AppPopupSelector";
    private static final int MAX_SHOW_COUNT = 6;

    private final PopupWindow popupWindow;
    private final Context context;
    private int itemLayoutID;

    private View view;
    private int itemHeight;

    public static AppPopupSelector newInstance(View view) {
        int itemLayoutId = R.layout.item_app_popup_selector_height_30_layout;
        return newInstance(view, true, view.getResources().getDimensionPixelSize(R.dimen.dpx_30), itemLayoutId);
    }

    public static AppPopupSelector newInstance(View view, boolean outsideTouchable, int itemHeight, int itemLayoutID) {
        return new AppPopupSelector(view, outsideTouchable, itemHeight, itemLayoutID);
    }

    public AppPopupSelector(View view, boolean outsideTouchable, int itemHeight, int itemLayoutID) {
        this.view = view;
        this.itemHeight = itemHeight;
        this.context = view.getContext();
        this.itemLayoutID = itemLayoutID;
        popupWindow = new PopupWindow(context);
        popupWindow.setOutsideTouchable(outsideTouchable);
        popupWindow.setFocusable(outsideTouchable);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
    }

    public AppPopupSelector bindSelector(List<String> items, CommonAsyncListener<Integer> onSelectListener) {
        popupWindow.setWidth(view.getWidth());
        int showCount = items.size() > MAX_SHOW_COUNT ? MAX_SHOW_COUNT : items.size();
        popupWindow.setHeight(itemHeight * showCount);

        SwipeRecyclerView swipeRecyclerView = new SwipeRecyclerView(context);
        swipeRecyclerView.setBackgroundColor(context.getColor(R.color.app_common_focus_gray));
        swipeRecyclerView.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        QuickList.instance().adapter(swipeRecyclerView, itemLayoutID, items, new QuickListListener<String>() {
            @Override
            public void onBindItem(QuickAdapter<String> adapter, SmartViewHolder holder, String model) {
                holder.text(R.id.tv_content, model);
            }

            @Override
            public void onClickItem(QuickAdapter<String> adapter, String model) {
                popupWindow.dismiss();
                onSelectListener.doSomething(items.indexOf(model));
            }
        });
        popupWindow.setContentView(swipeRecyclerView);
        return this;
    }

    public void showAsTop() {
        popupWindow.showAsDropDown(view, 0, 0);
    }

    public void dismissWindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }
}
