package com.bytemiracle.base.framework.view;

import org.greenrobot.greendao.annotation.Transient;

/**
 * 类功能：指示器
 *
 * @author gwwang
 * @date 2021/1/26 15:47
 */
public class BaseIndicator extends BaseCheckPojo {
    @Transient
    private int indicatorText;

    public int getIndicatorText() {
        return indicatorText;
    }

    public void setIndicatorText(int indicatorText) {
        this.indicatorText = indicatorText;
    }
}
