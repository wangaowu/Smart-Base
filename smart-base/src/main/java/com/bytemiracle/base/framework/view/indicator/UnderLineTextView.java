package com.bytemiracle.base.framework.view.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextPaint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.bytemiracle.base.R;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/3/10 9:49
 */
public class UnderLineTextView extends AppCompatTextView {

    public static int STROKE_WIDTH;

    public UnderLineTextView(Context context) {
        this(context, null);
    }

    public UnderLineTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UnderLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.STROKE_WIDTH = getResources().getDimensionPixelSize(R.dimen.divider_1dp);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        TextPaint paint = getPaint();
        paint.setStrokeWidth(STROKE_WIDTH);
        canvas.drawLine(0, getHeight() - STROKE_WIDTH, getWidth(), getHeight() - STROKE_WIDTH, paint);
    }
}
