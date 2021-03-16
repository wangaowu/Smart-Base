package com.bytemiracle.base.framework.view.relationTree;

import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.bytemiracle.base.R;

import java.util.List;

/**
 * 类功能：此处可以理解为链表结构中的双向链表
 *
 * @author gwwang
 * @date 2021/2/8 11:46
 */
public class CellRect {
    //node的数据和view
    public int colIndex;
    public int rowIndex;
    public View bindView;
    public int layoutResId;
    //背景资源
    public Drawable backgroundDrawable;
    //绑定数据
    public Object bindData;
    //文本
    public String contentText;
    //后勾和前勾
    public List<CellRect> childCells;
    public CellRect parent;
    //是否折叠状态
    private boolean collapse;
    //折叠按钮矩形
    private RectF collapseClipRect;
    //是否遍历过该节点
    private boolean traversed;

    public RectF getCollapseClipRect() {
        return collapseClipRect;
    }

    public void setCollapseClipRect(RectF collapseClipRect) {
        this.collapseClipRect = collapseClipRect;
    }

    public boolean hasChild() {
        return childCells != null && childCells.size() != 0;

    }

    /**
     * 构造方法
     *
     * @param colIndex 列索引
     * @param rowIndex 横索引
     * @param collapse 折叠状态
     */
    public CellRect(int colIndex, int rowIndex, boolean collapse) {
        this.colIndex = colIndex;
        this.rowIndex = rowIndex;
        this.collapse = collapse;
    }

    public boolean isTraversed() {
        return traversed;
    }

    public void setTraversed(boolean traversed) {
        this.traversed = traversed;
    }

    @Override
    public String toString() {
        return "CellRect{" +
                "colIndex=" + colIndex +
                ", rowIndex=" + rowIndex +
                ", collapse=" + collapse +
                '}';
    }

    public boolean isCollapse() {
        return collapse;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    /**
     * 高亮
     *
     * @param cell
     */
    public static void makeHighLight(CellRect cell) {
        ((TextView) cell.bindView.findViewById(R.id.cell_content)).getPaint().setUnderlineText(true);
        ((TextView) cell.bindView.findViewById(R.id.cell_content)).getPaint().setFakeBoldText(true);
    }

    /**
     * 清除高亮
     *
     * @param cell
     */
    public static void clearHighLight(CellRect cell) {
        ((TextView) cell.bindView.findViewById(R.id.cell_content)).getPaint().setUnderlineText(false);
        ((TextView) cell.bindView.findViewById(R.id.cell_content)).getPaint().setFakeBoldText(false);
    }
}
