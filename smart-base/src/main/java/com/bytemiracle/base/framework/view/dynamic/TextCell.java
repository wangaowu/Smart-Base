package com.bytemiracle.base.framework.view.dynamic;

import com.bytemiracle.base.framework.view.BaseCheckPojo;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/1/14 14:58
 */
public class TextCell extends BaseCheckPojo {
    /**
     * 文本
     */
    public String text;
    /**
     * 指定宽度
     */
    private int width;
    /**
     * 指定高度
     */
    private int height;
    /**
     * 附带的数据
     */
    private Object bundle;

    public TextCell(String text) {
        this.text = text;
    }

    public TextCell(String text, int width) {
        this.text = text;
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Object getBundle() {
        return bundle;
    }

    public void setBundle(Object bundle) {
        this.bundle = bundle;
    }
}
