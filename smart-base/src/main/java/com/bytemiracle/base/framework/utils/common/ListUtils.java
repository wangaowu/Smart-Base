package com.bytemiracle.base.framework.utils.common;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/1/19 13:29
 */
public class ListUtils {

    public static int wrapperSize(List list) {
        return list == null ? 0 : list.size();
    }

    public static List wrapper(List list) {
        return list == null ? new ArrayList() : list;
    }

    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty();
    }

    public static <T> T findObject(List<T> list) {
        return null;
    }
}
