package com.bytemiracle.base.framework.fragment.dynamicitem;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/3/23 15:02
 */
public class ItemData {
    /**
     * 条目类型
     */
    public ItemType itemType;
    public String flagText;
    public String content;
    public String editHint;
    public int centerIconResID;
    public String[] radioTexts;
    public String btnText;

    public ItemData(ItemType itemType, String flagText) {
        this.itemType = itemType;
        this.flagText = flagText;
    }

    public ItemData content(String content) {
        this.content = content;
        return this;
    }

    public ItemData rightButtonText(String btnText) {
        this.btnText = btnText;
        return this;
    }

    public ItemData editHint(String editHint) {
        this.editHint = editHint;
        return this;
    }

    public ItemData centerIcon(int centerIconResID) {
        this.centerIconResID = centerIconResID;
        return this;
    }

    public ItemData radioTexts(String... radioTexts) {
        this.radioTexts = radioTexts;
        return this;
    }

    public enum ItemType {
        /**
         * 普通内容
         */
        CONTENT,
        /**
         * 携带中心图标
         */
        CENTER_ICON,
        /**
         * 多行编辑
         */
        MULTI_EDIT,
        /**
         * 单选
         */
        RADIO_GROUP
    }
}
