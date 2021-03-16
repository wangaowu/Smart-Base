package com.bytemiracle.base.framework.view;

import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能：选中数据的基类
 *
 * @author gwwang
 * @date 2021/1/11 9:59
 */
public class BaseCheckPojo {
    /**
     * 是否选中
     */
    @Transient
    private boolean isChecked = false;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /**
     * 清空选中条目
     *
     * @param checkPojos 数据集合
     */
    public static boolean isEmptyCheck(List<? extends BaseCheckPojo> checkPojos) {
        if (checkPojos != null) {
            for (int i = 0; i < checkPojos.size(); i++) {
                if (checkPojos.get(i).isChecked()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 是否全选
     *
     * @param checkPojos 数据集合
     */
    public static boolean isAllCheck(List<? extends BaseCheckPojo> checkPojos) {
        if (checkPojos != null) {
            for (int i = 0; i < checkPojos.size(); i++) {
                if (!checkPojos.get(i).isChecked()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 清空选中条目
     *
     * @param checkPojos 数据集合
     */
    public static void clearCheckedItem(List<? extends BaseCheckPojo> checkPojos) {
        if (checkPojos != null) {
            for (int i = 0; i < checkPojos.size(); i++) {
                checkPojos.get(i).setChecked(false);
            }
        }
    }

    /**
     * 选中所有条目
     *
     * @param checkPojos 数据集合
     */
    public static void checkAllItem(List<? extends BaseCheckPojo> checkPojos) {
        if (checkPojos != null) {
            for (int i = 0; i < checkPojos.size(); i++) {
                checkPojos.get(i).setChecked(true);
            }
        }
    }

    /**
     * 选中条目
     *
     * @param checkPojos   数据集合
     * @param itemPosition 选中的条目位置
     */
    public static void checkedSingleItem(List<? extends BaseCheckPojo> checkPojos, int itemPosition) {
        if (checkPojos != null) {
            for (int i = 0; i < checkPojos.size(); i++) {
                BaseCheckPojo pojo = checkPojos.get(i);
                if (i == itemPosition) {
                    pojo.setChecked(true);
                } else if (pojo.isChecked()) {
                    pojo.setChecked(false);
                }
            }
        }
    }

    /**
     * 获取选中的条目
     *
     * @param checkPojos 数据集合
     * @return 选中的条目信息
     */
    public static List<? extends BaseCheckPojo> getCheckedItems(List<? extends BaseCheckPojo> checkPojos) {
        List<BaseCheckPojo> results = new ArrayList<>();
        if (checkPojos != null) {
            for (int i = 0; i < checkPojos.size(); i++) {
                BaseCheckPojo pojo = checkPojos.get(i);
                if (pojo.isChecked()) {
                    results.add(pojo);
                }
            }
        }
        return results;
    }

    /**
     * 获取选中的条目
     *
     * @param checkPojos 数据集合
     * @return 选中的条目信息
     */
    public static BaseCheckPojo getSingleCheckedItem(List<? extends BaseCheckPojo> checkPojos) {
        if (checkPojos != null) {
            for (int i = 0; i < checkPojos.size(); i++) {
                BaseCheckPojo pojo = checkPojos.get(i);
                if (pojo.isChecked()) {
                    return pojo;
                }
            }
        }
        return null;
    }

    /**
     * 获取选中的条目
     *
     * @param checkPojos 数据集合
     * @return 选中的条目信息
     */
    public static int getSingleCheckedIndex(List<? extends BaseCheckPojo> checkPojos) {
        if (checkPojos != null) {
            for (int i = 0; i < checkPojos.size(); i++) {
                BaseCheckPojo pojo = checkPojos.get(i);
                if (pojo.isChecked()) {
                    return i;
                }
            }
        }
        return -1;
    }


}
