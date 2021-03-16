package com.bytemiracle.base.framework.view.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 类功能：有指示器的textview封装
 *
 * @author gwwang
 * @date 2021/1/8 11:37
 */
public class IndicatorTextView extends AppCompatTextView {
    private static final String TAG = "IndicatorTextView";

    private Indicator indicator;

    public IndicatorTextView(Context context) {
        super(context);
    }

    public IndicatorTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndicatorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        drawForeIndicator(canvas);
        canvas.save();
    }

    /**
     * 设置显示的指示器
     *
     * @param indicatorConfig
     */
    public void showIndicator(Indicator indicatorConfig) {
        this.indicator = indicatorConfig;
        postInvalidate();
    }

    private void drawForeIndicator(Canvas canvas) {
        if (indicator != null) {
            try {
                BaseDrawIndicator baseDrawIndicator = indicator.drawPresenter.newInstance();
                baseDrawIndicator.init(this, indicator, canvas).draw();
            } catch (Exception e) {
                Log.e(TAG, "drawPresenterClass newInstance error: ");
            }
        }
    }
}
