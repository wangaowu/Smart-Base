package com.bytemiracle.base.framework.utils.string;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能：文本工具类
 *
 * @author gwwang
 * @date 2021/1/12 11:03
 */
public class AppTextUtil {

    public static boolean isSameValue(Double first, Double second) {
        return first - second == 0;
    }

    public static boolean isZero(Double value) {
        return isSameValue(value, 0d);
    }


    public static List<String> buildSpeakFileList(String fileTitle, List<String> paths) {
        String prefix = getFilePrefix(fileTitle);
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < paths.size(); i++) {
            strings.add(prefix + ".." + (i + 1) + ".wav");
        }
        return strings;
    }

    /**
     * 构建文件前缀
     *
     * @param fileTitle
     * @return
     */
    public static String getFilePrefix(String fileTitle) {
        return fileTitle.length() > 3 ? fileTitle.substring(0, 3) : fileTitle;
    }
}
