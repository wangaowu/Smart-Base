package com.bytemiracle.base.framework.view.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;

import com.bytemiracle.base.R;

/**
 * 类功能：绘制圆圈(有文本)
 *
 * @author gwwang
 * @date 2021/1/11 14:03
 */
public class DrawCircleIndicator extends BaseDrawIndicator {
    /**
     * 快速创建一个指示器
     *
     * @param context
     * @param indicatorText
     * @return
     */
    public static Indicator quickCreate(Context context, String indicatorText) {
        int redValue = context.getColor(R.color.app_common_dark_red);
        int indicatorPadding = context.getResources().getDimensionPixelSize(R.dimen.dpx_20);
        return new Indicator.Build()
                .configType(Indicator.Type.CIRCLE)
                .color(redValue)
                .text(indicatorText)
                .padding(indicatorPadding).create();
    }

    @Override
    public void draw() {
        if (TextUtils.isEmpty(indicator.text)) {
            //不绘制文本为空的圆圈
            return;
        }
        int circleRadius = calcRadius();
        int cx = view.getWidth() - view.getPaddingRight() - indicator.indicatorPadding - circleRadius;
        int cy = view.getHeight() / 2;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(indicator.colorValue);
        canvas.drawCircle(cx, cy, circleRadius, paint);
        drawCircleText(cx, canvas);
    }

    private void drawCircleText(int circleCenterX, Canvas canvas) {
        float indicatorTextSize = view.getTextSize() * .8f;
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(indicatorTextSize);
        paint.setAntiAlias(true);
        float textWidth = measureText(view.getPaint(), indicator.text);
        float drawTextLeft = circleCenterX - textWidth / 2;
        canvas.drawText(indicator.text, drawTextLeft, view.getBaseline() - indicatorTextSize / 8, paint);
    }

    /**
     * 计算绘制的半径
     * 1.当文字为空时，半径为宽度的1/4
     * 2.当文字不为空时，半径为文字大小的一半
     *
     * @return
     */
    private int calcRadius() {
        if (TextUtils.isEmpty(indicator.text)) {
            return (int) (view.getHeight() * .25f);
        } else {
            return (int) (view.getTextSize() * .5f);
        }
    }
}
