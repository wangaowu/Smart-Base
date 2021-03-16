package com.bytemiracle.base.framework.view.pagetools;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.listener.CommonAsyncListener;

import java.util.List;

/**
 * 类功能：ListView的分页数据控制器(含controller)
 *
 * @author gwwang
 * @date 2021/1/8 18:03
 */
public class ListPageToolsView extends LinearLayout {
    private static final String TAG = "ListPageToolsView";
    public static final int MAX_SHOW_PAGE_COUNT = 5;

    private View childViews;
    private TextView tvPageInfo;
    private TextView tvTotalInfo;
    private TextView tvMoveLeft;
    private TextView tvMoveRight;
    private LinearLayout llPageContainer;
    private TextView tvCountOfPage;
    private PageToolsPresenter pageToolsPresenter;

    public ListPageToolsView(Context context) {
        this(context, null);
    }

    public ListPageToolsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListPageToolsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ListPageToolsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        findViews();
    }

    // 设置viewFields
    private void findViews() {
        childViews = View.inflate(getContext(), R.layout.layout_list_page_data_tools, this);
        tvPageInfo = childViews.findViewById(R.id.tv_page_info);
        tvTotalInfo = childViews.findViewById(R.id.tv_total_info);
        tvMoveLeft = childViews.findViewById(R.id.tv_move_left);
        tvMoveRight = childViews.findViewById(R.id.tv_move_right);
        llPageContainer = childViews.findViewById(R.id.ll_pages_container);
        tvCountOfPage = childViews.findViewById(R.id.tv_count_of_page);
    }

    /**
     * 设置工具栏监听
     *
     * @param onPageInfoChangedListener
     */
    public void setToolsListener(List<PolygonDataBean> objects, CommonAsyncListener<List<PolygonDataBean>> onPageInfoChangedListener) {
        pageToolsPresenter = new PageToolsPresenter(getContext(), llPageContainer, tvPageInfo, tvTotalInfo,
                objects,
                data -> pageToolsPresenter.updatePageAt((Integer) data),
                onPageInfoChangedListener);
        tvCountOfPage.setOnClickListener(v -> pageToolsPresenter.showChooseDialog(tvCountOfPage));
        tvMoveLeft.setOnClickListener(v -> pageToolsPresenter.moveLeft());
        tvMoveRight.setOnClickListener(v -> pageToolsPresenter.moveRight());
        //设置数据
        pageToolsPresenter.showDefaultPageInfo();
    }
}
