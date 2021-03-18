package com.bytemiracle.base.framework.http;

import android.app.Application;

import com.bytemiracle.base.framework.utils.app.AppUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;

import java.util.HashMap;
import java.util.Map;

/**
 * 类功能：基于okGo封装适用本应用的网络请求，含：
 * 1.get请求，post请求,上传请求，下载请求
 * 2.配置公共的请求头
 *
 * @author gwwang
 * @date 2021/3/12 10:14
 */
public class OkGoHttp {
    private static final String TAG = "OkGoHttp";
    public static String BASE_MACHINE_URL;
    public static String BASE_API_URL;

    /**
     * 初始化
     *
     * @param context 上下文
     */
    public static void init(Application context, String apiUrl, String machineUrl) {
        init(context, apiUrl, machineUrl, new HashMap<>());
    }

    /**
     * 初始化
     *
     * @param context 上下文
     * @param headers 全局的请求头
     */
    public static void init(Application context, String apiUrl, String machineUrl, Map<String, Object> headers) {
        OkGoHttp.BASE_API_URL = apiUrl;
        OkGoHttp.BASE_MACHINE_URL = machineUrl;
        SmartHttpClient smartHttpClientBuild = SmartHttpClient.newClient().configTimeout();
        if (AppUtils.isApkDebuggable(context)) {
            smartHttpClientBuild.configLog(TAG);
        }
        OkGo.getInstance().init(context)
                .setOkHttpClient(smartHttpClientBuild.build())
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(2)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(BaseRequest.buildHttpHeaders(headers));   //全局公共头
    }

    /**
     * 创建get请求
     *
     * @return
     */
    public static SmartGetRequest newGetRequest() {
        return SmartGetRequest.get();
    }

    /**
     * 创建post请求
     *
     * @return
     */
    public static SmartPostRequest newPostRequest() {
        return SmartPostRequest.get();
    }

    /**
     * 创建下载请求
     */
    public static SmartDownloadRequest newDownloadRequest() {
        return SmartDownloadRequest.get();
    }

    /**
     * 创建上传请求
     */
    public static SmartUploadRequest newUploadRequest() {
        return SmartUploadRequest.get();
    }
}
