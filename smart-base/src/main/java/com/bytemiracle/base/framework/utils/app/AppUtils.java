package com.bytemiracle.base.framework.utils.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/3/18 16:40
 */
public class AppUtils {
    /**
     * 判断包是不是debug
     *
     * @param context
     * @return
     */
    public static boolean isApkDebuggable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {

        }
        return false;
    }
}
