package com.bytemiracle.base.framework.view.pagetools;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.listener.CommonAsyncListener;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能：list内容分页业务类
 *
 * @author gwwang
 * @date 2021/1/8 18:36
 */
public class PageToolsPresenter<T> {
    private static final String TAG = "PageToolsPresenter";

    private final int COLOR_TEXT_GRAY;
    private final int COLOR_TEXT_BLUE;

    private Context context;
    private LinearLayout llPageContainer;
    private TextView tvPageInfo;
    private TextView tvTotalInfo;
    /**
     * list所有数据集
     */
    private List<T> objects;
    /**
     * 每页的数量
     */
    private int countOfPage;
    /**
     * 当前页码
     */
    private int currentPageIndex;

    private CommonAsyncListener<List<T>> onPageInfoChangedListener;
    private CommonAsyncListener<Integer> clickPageIndexListener;

    /**
     * 构造方法
     *
     * @param context                   上下文 &  ..views
     * @param objects                   list数据集
     * @param clickPageIndexListener    点击页码监听器
     * @param onPageInfoChangedListener 页面数据改变监听器
     */
    public PageToolsPresenter(Context context,
                              LinearLayout llPageContainer,
                              TextView tvPageInfo,
                              TextView tvTotalInfo,
                              List<T> objects,
                              CommonAsyncListener<Integer> clickPageIndexListener,
                              CommonAsyncListener<List<T>> onPageInfoChangedListener) {
        this.context = context;
        this.llPageContainer = llPageContainer;
        this.tvPageInfo = tvPageInfo;
        this.tvTotalInfo = tvTotalInfo;
        this.objects = objects;
        this.clickPageIndexListener = clickPageIndexListener;
        this.onPageInfoChangedListener = onPageInfoChangedListener;

        this.COLOR_TEXT_GRAY = context.getColor(R.color.app_common_content_text_dark_333_color);
        this.COLOR_TEXT_BLUE = context.getColor(R.color.app_common_blue);
        this.countOfPage = context.getResources().getIntArray(R.array.spinner_count_of_page)[0];
    }

    /**
     * 点击了页码
     *
     * @param pageIndex
     */
    public void updatePageAt(Integer pageIndex) {
        //改变页码颜色
        changeSelfBackground(pageIndex);
        //改变第x-x的信息
        tvPageInfo.setText(makePageInfoString(pageIndex));
        //监听回调
        setPageInfoChangedListenerPageIndex(pageIndex);
    }

    /**
     * 设置初始化信息
     */
    public void showDefaultPageInfo() {
        //设置第x-x的信息
        tvPageInfo.setText(makePageInfoString(0));
        //设置总共信息
        tvTotalInfo.setText(makeTotalInfoString());
        //设置页码布局
        setPageIndexDataViews(createPageIndexes(0));
        //改变页码颜色
        changeSelfBackground(0);
        //监听回调
        setPageInfoChangedListenerPageIndex(0);
    }

    public void moveLeft() {
        if (currentPageIndex == 0) {
            //已经是最左边
            return;
        }
        updatePageAt(--currentPageIndex);
        //重新设置页码布局
        //setPageIndexDataViews(createPageIndexes(fixLeftPageIndex(currentPageIndex)));
    }

    public void moveRight() {
        if (currentPageIndex == getCeilPageCount() - 1) {
            //已经是最右边
            return;
        }
        updatePageAt(++currentPageIndex);
        //重新设置页码布局
        //setPageIndexDataViews(createPageIndexes(fixLeftPageIndex(currentPageIndex)));
    }

    /**
     * 显示选择弹窗
     *
     * @param attachView
     */
    public void showChooseDialog(TextView attachView) {
        new MaterialDialog.Builder(context)
                .title(context.getString(R.string.tips_count_of_page))
                .items(convert2StringArray(R.array.spinner_count_of_page))
                .itemsCallbackSingleChoice(
                        -1,
                        (dialog, itemView, which, text) -> {
                            if (!TextUtils.isEmpty(text)) {
                                Integer count = Integer.valueOf(text.toString());
                                PageToolsPresenter.this.setCountOfPage(count);
                                confirmNewCountOfPage(attachView, count);
                            }
                            return true;
                        })
                .positiveText(R.string.lab_choice)
                .onPositive((dialog, which) -> dialog.dismiss())
                .negativeText(R.string.lab_cancel)
                .onNegative((dialog, which) -> dialog.dismiss())
                .cancelable(true)
                .show();
    }

    /**
     * 该方法作用：
     * 使得移动页码超出边界时，显示(挪动)当前页码到最左or最右
     * TODO:暂时不实现
     *
     * @param currentPageIndex
     * @return
     */
    private int fixLeftPageIndex(int currentPageIndex) {
//        int fixedLeftIndex = currentPageIndex;
//        if (currentPageIndex > ListPageToolsView.MAX_SHOW_PAGE_COUNT) {
//            fixedLeftIndex = currentPageIndex - ListPageToolsView.MAX_SHOW_PAGE_COUNT;
//        }
//        return fixedLeftIndex;
        return currentPageIndex;
    }

    private void setPageInfoChangedListenerPageIndex(int pageIndex) {
        this.currentPageIndex = pageIndex;
        if (onPageInfoChangedListener != null) {
            onPageInfoChangedListener.doSomething(subList(pageIndex));
        }
    }

    private void setCountOfPage(int countOfPage) {
        this.countOfPage = countOfPage;
    }

    private String makePageInfoString(int beginPageIndex) {
        int beginCount = beginPageIndex * countOfPage;
        int endCount = (beginPageIndex + 1) * countOfPage;
        endCount = endCount > objects.size() ? objects.size() : endCount;
        return context.getString(R.string.tools_page_info_string, beginCount, endCount);
    }

    private String makeTotalInfoString() {
        return context.getString(R.string.tools_total_info_string, objects.size());
    }

    private String makeCountOfPageString(int totalCount) {
        return context.getString(R.string.tools_count_of_page_string, totalCount);
    }

    private void setPageIndexDataViews(List<Integer> pageIndexes) {
        llPageContainer.removeAllViews();
        for (Integer pageIndex : pageIndexes) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, -2);
            TextView itemView = new TextView(context);
            int horPadding = context.getResources().getDimensionPixelSize(R.dimen.dpx_5);
            itemView.setPadding(horPadding, 0, horPadding, 0);
            itemView.setText(String.valueOf(pageIndex + 1));
            itemView.setOnClickListener(v -> {
                changeSelfBackground(pageIndex);
                this.clickPageIndexListener.doSomething(pageIndex);
            });
            llPageContainer.addView(itemView, layoutParams);
        }
    }

    private void changeSelfBackground(int pageIndex) {
        for (int i = 0; i < this.llPageContainer.getChildCount(); i++) {
            TextView child = (TextView) this.llPageContainer.getChildAt(i);
            if (i == pageIndex) {
                //点击自己
                child.setTextColor(COLOR_TEXT_BLUE);
                child.setBackground(child.getContext().getDrawable(R.drawable.bg_blue_roundrect_2_dp_stroke));
            } else {
                //置灰其他
                child.setTextColor(COLOR_TEXT_GRAY);
                child.setBackground(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    private void confirmNewCountOfPage(TextView attachView, Integer count) {
        //改变第x-x的信息
        tvPageInfo.setText(makePageInfoString(0));
        //更新页面内数量信息
        attachView.setText(makeCountOfPageString(count));
        //重新设置页码
        setPageIndexDataViews(createPageIndexes(0));
        //改变页码颜色
        changeSelfBackground(0);
        //监听回调
        setPageInfoChangedListenerPageIndex(0);
    }

    /**
     * utils
     *
     * @param beginPageIndex
     * @return
     */
    private List<T> subList(int beginPageIndex) {
        int beginIndex = beginPageIndex * countOfPage;
        int endIndex = (beginPageIndex + 1) * countOfPage;
        endIndex = endIndex > objects.size() ? objects.size() : endIndex;
        return objects.subList(beginIndex, endIndex);
    }

    /**
     * utils
     *
     * @return
     */
    private ArrayList<Integer> createPageIndexes(int startIndex) {
        int totalPages = getCeilPageCount();
        ArrayList<Integer> pageIndexes = new ArrayList<>();
        for (int i = startIndex; i < totalPages; i++) {
            pageIndexes.add(i);
        }
        return pageIndexes;
    }

    /**
     * utils
     *
     * @return
     */
    private int getCeilPageCount() {
        return (int) Math.ceil(objects.size() / ((float) countOfPage));
    }

    /**
     * utils
     *
     * @param intArrayResId
     * @return
     */
    private CharSequence[] convert2StringArray(int intArrayResId) {
        int[] intArray = context.getResources().getIntArray(intArrayResId);
        String[] strings = new String[intArray.length];
        for (int i = 0; i < strings.length; i++) {
            strings[i] = String.valueOf(intArray[i]);
        }
        return strings;
    }

}
