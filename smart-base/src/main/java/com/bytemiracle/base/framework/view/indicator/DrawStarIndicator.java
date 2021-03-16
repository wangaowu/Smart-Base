package com.bytemiracle.base.framework.view.indicator;

/**
 * 类功能：绘制星号
 *
 * @author gwwang
 * @date 2021/1/11 14:03
 */
public class DrawStarIndicator extends BaseDrawIndicator {
    private static final String TEXT_STAR = "*";

    @Override
    public void draw() {
        int cx = (int) (measureText(view.getPaint(), view.getText().toString()) + indicator.indicatorPadding);
        view.getPaint().setColor(indicator.colorValue);
        canvas.drawText(TEXT_STAR, cx, view.getBaseline(), view.getPaint());
    }
}
