package com.bytemiracle.base.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/1/29 10:18
 */
public class TouchScaleView extends LinearLayout {
    public TouchScaleView(Context context) {
        this(context, null);
    }

    public TouchScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TouchScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewEffectUtils.applyTouchScale(this);
    }
}
