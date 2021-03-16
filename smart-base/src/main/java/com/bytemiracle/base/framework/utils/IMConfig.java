package com.bytemiracle.base.framework.utils;

/**
 * Created by heavyrain lee on 2017/11/24.
 */

public class IMConfig {

    /**
     * 仅仅是host，没有http开头，也不用配置端口，<strong> 底层会使用默认的80端口</strong>，
     * 不可配置为127.0.0.1 或者 192.168.0.1
     */
    public static String IM_SERVER_HOST;

    public static void initIMServerHost(String imServerHost) {
        IM_SERVER_HOST = imServerHost;
    }
}
