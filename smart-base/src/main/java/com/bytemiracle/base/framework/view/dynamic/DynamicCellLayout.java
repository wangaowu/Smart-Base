package com.bytemiracle.base.framework.view.dynamic;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.listener.CommonAsyncListener;
import com.bytemiracle.base.framework.view.BaseCheckPojo;

import java.util.List;

/**
 * 类功能：可以动态添加的线性布局
 *
 * @author gwwang
 * @date 2021/1/14 14:54
 */
public class DynamicCellLayout extends LinearLayout {
    private static final String TAG = "DynamicCellLayout";

    private List<TextCell> textCellList;
    /**
     * 条目布局id
     */
    private int itemLayoutId;
    /**
     * 数据改变监听器
     */
    private CommonAsyncListener<TextCell> onCellCheckedChangeListener;
    /**
     * 选中的资源文件
     */
    private int checkedColorValue;
    /**
     * 未选中的资源文件
     */
    private int normColorValue;
    private int specialCellWidth;
    private int specialCellHeight;

    public DynamicCellLayout(Context context) {
        this(context, null);
    }

    public DynamicCellLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DynamicCellLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DynamicCellLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 该方法必须在构造之后调用(在layout文件中指定布局方向)
     *
     * @param oration 布局方向
     * @return
     */
    public DynamicCellLayout oration(int oration) {
        setOrientation(oration);
        return this;
    }

    /**
     * 格子数据
     *
     * @param normColorValue              条目颜色
     * @param checkedColorValue           选中的颜色
     * @param itemLayoutId                条目id
     * @param onCellCheckedChangeListener 选中监听器(如果不指定，意味不可选)
     */
    public DynamicCellLayout cell(int normColorValue, int checkedColorValue, int itemLayoutId, CommonAsyncListener<TextCell> onCellCheckedChangeListener) {
        this.normColorValue = normColorValue;
        this.checkedColorValue = checkedColorValue;
        this.itemLayoutId = itemLayoutId;
        this.onCellCheckedChangeListener = onCellCheckedChangeListener;
        return this;
    }

    /**
     * 格子数据(不可点击)
     *
     * @param normColorValue 条目颜色
     * @param itemLayoutId   条目id
     */
    public DynamicCellLayout cell(int normColorValue, int itemLayoutId) {
        return cell(normColorValue, 0, itemLayoutId, null);
    }

    /**
     * 指定宽度
     */
    public DynamicCellLayout everyCellWidth(int specialCellWidth) {
        this.specialCellWidth = specialCellWidth;
        return this;
    }

    /**
     * 指定高度
     */
    public DynamicCellLayout everyCellHeight(int specialCellHeight) {
        this.specialCellHeight = specialCellHeight;
        return this;
    }

    /**
     * 设置数据
     *
     * @param textCellList
     */
    public DynamicCellLayout itemList(List<TextCell> textCellList) {
        this.textCellList = textCellList;
        post(() -> addChildViews());
        return this;
    }

    /**
     * 默认选中
     *
     * @param checkedIndex
     * @return
     */
    public DynamicCellLayout defaultCheck(int checkedIndex) {
        this.post(() -> checked(checkedIndex));
        return this;
    }

    /**
     * 选中第几个
     *
     * @param index
     */
    public void checked(int index) {
        try {
            if (BaseCheckPojo.getSingleCheckedIndex(textCellList) == index) {
                //当前已经选中
                return;
            }
            BaseCheckPojo.checkedSingleItem(textCellList, index);
            updateItemsBackground();
            if (onCellCheckedChangeListener != null) {
                onCellCheckedChangeListener.doSomething(textCellList.get(index));
            }
        } catch (Exception e) {
            //这块加try的原因是，及其个别的情况下，两次post会时序错乱，导致crash
            Log.e(TAG, "checked index error! " + e.getMessage());
        }
    }

    public void addItem(TextCell textItem) {
        this.textCellList.add(textItem);
    }

    public void addItemList(List<TextCell> textCellList) {
        this.textCellList.addAll(textCellList);
    }

    private void addChildViews() {
        removeAllViews();
        for (TextCell textCell : textCellList) {
            View contentView = View.inflate(getContext(), itemLayoutId, null);
            findCellTextView(contentView).setText(textCell.text);
            findCellTextView(contentView).setBackgroundColor(normColorValue);
            if (isCellCheckable()) {
                contentView.setOnClickListener(v -> checked(textCellList.indexOf(textCell)));
            }
            addView(contentView, fixLayoutParams(findCellTextView(contentView), textCell));
        }
    }

    private void updateItemsBackground() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = getChildAt(i);
            if (textCellList.get(i).isChecked()) {
                childAt.setBackgroundColor(checkedColorValue);
            } else {
                childAt.setBackgroundColor(normColorValue);
            }
        }
    }

    private LayoutParams fixLayoutParams(TextView item, TextCell textCell) {
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //首先使用父控件指定宽高
        if (getOrientation() == VERTICAL) {
            layoutParams.width = specialCellWidth == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : specialCellWidth;
            layoutParams.height = specialCellHeight == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : specialCellHeight;
        } else {
            //横向布局,设置最小宽度为2倍高度
            if (getHeight() != 0) {
                item.setMinWidth(2 * getHeight());
            }
            layoutParams.width = specialCellWidth == 0 ? ViewGroup.LayoutParams.WRAP_CONTENT : specialCellWidth;
            layoutParams.height = specialCellHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : specialCellHeight;
        }
        //其次使用自己指定宽高
        layoutParams.width = textCell.getWidth() == 0 ? layoutParams.width : textCell.getWidth();
        layoutParams.height = textCell.getHeight() == 0 ? layoutParams.height : textCell.getHeight();
        return layoutParams;
    }

    private TextView findCellTextView(int cellIndex) {
        return findCellTextView(getChildAt(cellIndex));
    }

    private TextView findCellTextView(View cellView) {
        if (onlyTextCell(cellView)) {
            return (TextView) cellView;
        }
        return cellView.findViewById(R.id.cell_tag);
    }

    private boolean onlyTextCell(View cellView) {
        return cellView instanceof TextView;
    }

    private boolean isCellCheckable() {
        return onCellCheckedChangeListener != null;
    }
}
