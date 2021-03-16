package com.bytemiracle.base.framework.fragment.subfragment.config;

/**
 * 类功能：嵌套fragment标题配置类
 *
 * @author gwwang
 * @date 2021/1/8 9:19
 */
public class SubTitleConfig {
    public enum ViewType {
        BUTTON, ICON, EMPTY
    }

    public static class RightConfig {
        public String buttonText;
        public ViewType viewType;
        public int imageResId;

        public RightConfig(ViewType viewType) {
            this.viewType = viewType;
        }

        public RightConfig(ViewType viewType, String buttonText) {
            this.viewType = viewType;
            this.buttonText = buttonText;
        }

        public RightConfig(ViewType viewType, int imageResId) {
            this.viewType = viewType;
            this.imageResId = imageResId;
        }
    }

    public RightConfig rightConfig;
    public String titleText;

}
