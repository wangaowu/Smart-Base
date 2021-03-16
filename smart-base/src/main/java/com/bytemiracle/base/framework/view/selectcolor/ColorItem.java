package com.bytemiracle.base.framework.view.selectcolor;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/1/18 11:45
 */
public class ColorItem extends View {
    private static final String TAG = "ColorSelector";

    private static final int UN_SPECIAL_SIZE = 50;

    private int viewWidth;
    private int viewHeight;

    private Paint colorPaint = new Paint();
    private Rect rect;
    private int colorValue;
    private float OUTER_RADIUS;
    private float INNER_RADIUS;
    private float animationRadius = 0;

    private boolean isSelected;
    private float INNER_SOLID_RADIUS;
    private OnClickListener listener;

    public ColorItem(Context context) {
        this(context, null);
    }

    public ColorItem(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        colorPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewWidth = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY ? MeasureSpec.getSize(widthMeasureSpec) : UN_SPECIAL_SIZE;
        viewHeight = MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY ? MeasureSpec.getSize(heightMeasureSpec) : UN_SPECIAL_SIZE;
        rect = new Rect(0, 0, viewWidth, viewHeight);
        OUTER_RADIUS = rect.width() * .4f;
        INNER_RADIUS = rect.width() * .3f;
        INNER_SOLID_RADIUS = rect.width() * .2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSelected) {
            canvas.drawColor(Color.WHITE);
            canvas.drawCircle(rect.centerX(), rect.centerY(), OUTER_RADIUS, colorPaint);
            colorPaint.setColor(Color.WHITE);
            canvas.drawCircle(rect.centerX(), rect.centerY(), animationRadius, colorPaint);
        }
        colorPaint.setColor(colorValue);
        canvas.drawCircle(rect.centerX(), rect.centerY(), INNER_SOLID_RADIUS, colorPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSelected = true;
                postAnimation(OUTER_RADIUS, INNER_RADIUS);
                return true;
            case MotionEvent.ACTION_UP:
                if (listener != null) {
                    listener.onClick(this);
                }
                return super.onTouchEvent(event);
        }
        return super.onTouchEvent(event);
    }

    private void postAnimation(float max_radius, float min_radius) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(max_radius, min_radius);
        valueAnimator.setDuration(150);
        valueAnimator.addUpdateListener(animation -> {
            animationRadius = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        valueAnimator.start();
    }

    /**
     * 初始化颜色
     *
     * @param colorValue
     * @return
     */
    public ColorItem initColor(int colorValue) {
        this.colorValue = colorValue;
        if (colorPaint != null) {
            colorPaint.setColor(colorValue);
            postInvalidate();
        }
        return this;
    }

    /**
     * 设置监听器
     *
     * @param listener
     * @return
     */
    public ColorItem listener(OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    /**
     * 获取选中状态
     *
     * @return
     */
    @Override
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * 设置选中状态
     *
     * @param selected
     */
    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
        postInvalidate();
    }
}
