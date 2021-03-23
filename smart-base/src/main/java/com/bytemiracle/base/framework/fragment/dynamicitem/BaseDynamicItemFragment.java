package com.bytemiracle.base.framework.fragment.dynamicitem;

import android.view.View;
import android.widget.LinearLayout;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能：仅有滑动listview的基类fragment
 * 泛型H：ViewHolder
 * 泛型V：数据类型
 *
 * @author gwwang
 * @date 2021/3/22 17:07
 */
public abstract class BaseDynamicItemFragment<V> extends BaseFragment {
    private static final String TAG = "BaseDynamicItemFragment";

    protected final List<ItemController> itemControllers = new ArrayList();
    private LinearLayout llContainer;
    private DynamicItemPresenter dynamicItemPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_dynamic_item;
    }

    @Override
    protected void initViews() {
        llContainer = mRootView.findViewById(R.id.ll_container);
        dynamicItemPresenter = new DynamicItemPresenter(llContainer);
    }

    /**
     * 删除所有条目
     */
    protected void clearItems() {
        llContainer.removeAllViews();
    }

    /**
     * 添加条目
     *
     * @param itemData 条目数据
     */
    protected void addItem(ItemData itemData) {
        ItemController itemController = dynamicItemPresenter.addItem(itemData);
        itemControllers.add(itemController);
    }

    /**
     * 锁定条目
     */
    protected void lockItems() {
        for (ItemController itemController : itemControllers) {
            itemController.lockItem();
        }
    }

    /**
     * 解锁条目
     */
    protected void unlockItems() {
        for (ItemController itemController : itemControllers) {
            itemController.unlockItem();
        }
    }

    /**
     * 添加条目
     *
     * @param itemView 条目view（当自己完全可以控制时，调用此方法）
     */
    protected void addItem(View itemView) {
        addItem(itemView, new LinearLayout.LayoutParams(-1, -2));
    }

    /**
     * 添加条目
     *
     * @param itemView     条目view（当自己完全可以控制时，调用此方法）
     * @param layoutParams 布局参数
     */
    protected void addItem(View itemView, LinearLayout.LayoutParams layoutParams) {
        dynamicItemPresenter.addItem(itemView, layoutParams);
    }
}
