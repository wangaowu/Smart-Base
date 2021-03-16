package com.bytemiracle.base.framework.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bytemiracle.base.R;

/**
 * 类功能：支持简单的rtl自定义布局 ( !!!不支持 竖向)
 *
 * @author gwwang
 * @date 2021/2/4 16:23
 */
public class RTLLinearLayout extends LinearLayout {
    private static final String TAG = "RTLLinearLayout";

    private boolean openRTL;

    public RTLLinearLayout(Context context) {
        this(context, null);
    }

    public RTLLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RTLLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public RTLLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void openRTL(boolean openRTL) {
        this.openRTL = openRTL;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //因为依赖线性布局第一次，所以不能del line_71
        super.onLayout(changed, l, t, r, b);
        String tagSupportRtlBackground = getContext().getString(R.string.tag_support_rtl_background);
        int width = r - l;
        for (int index = 0; index < getChildCount(); index++) {
            View child = getChildAt(index);
            //适配rtl背景
            adapterRTLBackground(tagSupportRtlBackground, child);
            //适配rtl权重
            adapterViewGravity(child);
            //适配rtl布局位置
            adapterLayoutPosition(width, child);
        }
    }

    private void adapterLayoutPosition(int width, View child) {
        int left = child.getLeft();
        int top = child.getTop();
        int right = child.getRight();
        int bottom = child.getBottom();
        int newLeft = width - right;
        int newRight = width - left;
        if (openRTL) {
            child.layout(newLeft, top, newRight, bottom);
        } else {
            child.layout(left, top, right, bottom);
        }
    }

    private void adapterViewGravity(View child) {
        if (child instanceof LinearLayout && ((LinearLayout) child).getOrientation() == LinearLayout.VERTICAL) {
            //竖向的linearLayout
            ((LinearLayout) child).setGravity(openRTL ? Gravity.RIGHT : Gravity.LEFT);
        }
    }

    private void adapterRTLBackground(String tagSupportRtlBackground, View child) {
        if (tagSupportRtlBackground.equals(child.getTag())) {
            child.setBackgroundResource(openRTL ? R.drawable.bg_white_stroke_im_right : R.drawable.bg_white_stroke_im_left);
        }
    }
}
