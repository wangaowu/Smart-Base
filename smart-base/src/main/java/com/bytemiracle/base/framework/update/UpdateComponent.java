package com.bytemiracle.base.framework.update;

/**
 * 类功能：更新模块的数据持有类
 *
 * @author gwwang
 * @date 2021/3/11 16:25
 */
public class UpdateComponent {
    public static int appLogo;
    public static String appName;
    public static String DOWNLOAD_DIR;

    /**
     * 设置更新组件所需要的资源
     *
     * @param appLogo      app的图标
     * @param appName      app的名称
     * @param DOWNLOAD_DIR 下载app的路径
     */
    public static void initResource(int appLogo, String appName, String DOWNLOAD_DIR) {
        UpdateComponent.appLogo = appLogo;
        UpdateComponent.appName = appName;
        UpdateComponent.DOWNLOAD_DIR = DOWNLOAD_DIR;
    }

    /**
     * 更新服务是否正在运行
     */
    public static boolean updateServiceIsRunning = false;

    public static void setUpdateRunning(boolean isUpdating) {
        updateServiceIsRunning = isUpdating;
    }

}
