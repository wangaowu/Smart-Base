package com.bytemiracle.base.framework.view.indicator;

import android.graphics.Bitmap;

/**
 * 类功能：指示器数据
 *
 * @author gwwang
 * @date 2021/1/11 10:58
 */
public class Indicator {
    /**
     * 指示器类型
     */
    public enum Type {
        /**
         * 星号
         */
        STAR,
        /**
         * 点
         */
        DOT,
        /**
         * 圆（的时候可携带文字）
         */
        CIRCLE,
        /**
         * 图片
         */
        BITMAP
    }

    public enum AlignType {
        RIGHT, RIGHT_OF_TEXT, LEFT
    }

    public Type configType;
    public String text;
    public Bitmap bitmap;
    public int colorValue;
    public AlignType alignType;
    public int indicatorPadding;
    public Class<? extends BaseDrawIndicator> drawPresenter;

    public static class Build {
        Indicator build = new Indicator();

        public Build configType(Type type) {
            build.configType = type;
            build.drawPresenter = mathPresenterClass(type);
            return this;
        }

        public Build text(String text) {
            build.text = text;
            return this;
        }

        public Build bitmap(Bitmap bitmap) {
            build.bitmap = bitmap;
            return this;
        }

        public Build color(int colorValue) {
            build.colorValue = colorValue;
            return this;
        }

        public Build alignType(AlignType alignType) {
            build.alignType = alignType;
            return this;
        }

        public Build padding(int indicatorPadding) {
            build.indicatorPadding = indicatorPadding;
            return this;
        }

        public Indicator create() {
            return build;
        }

        private Class mathPresenterClass(Type type) {
            switch (type) {
                case DOT:
                    return DrawDotIndicator.class;
                case BITMAP:
                    return DrawBitmapIndicator.class;
                case STAR:
                    return DrawStarIndicator.class;
                case CIRCLE:
                    return DrawCircleIndicator.class;
            }
            return DrawDotIndicator.class;
        }

    }
}
