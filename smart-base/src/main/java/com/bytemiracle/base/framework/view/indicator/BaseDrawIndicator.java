package com.bytemiracle.base.framework.view.indicator;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.TextView;

/**
 * 类功能：绘制基类
 *
 * @author gwwang
 * @date 2021/1/11 14:09
 */
public abstract class BaseDrawIndicator {
    protected TextView view;
    protected Canvas canvas;
    protected Indicator indicator;

    public BaseDrawIndicator init(TextView textView, Indicator indicator, Canvas canvas) {
        this.view = textView;
        this.indicator = indicator;
        this.canvas = canvas;
        return this;
    }

    protected float measureText(Paint textPaint, String text) {
        return textPaint.measureText(text);
    }

    public abstract void draw();
}
