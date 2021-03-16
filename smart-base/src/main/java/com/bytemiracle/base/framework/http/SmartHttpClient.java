package com.bytemiracle.base.framework.http;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * 类功能：网络请求配置类
 *
 * @author gwwang
 * @date 2021/3/12 10:20
 */
public class SmartHttpClient {
    private static final String TAG = "SmartOkHttpClient";

    private final OkHttpClient.Builder builder;
    private static final SmartHttpClient instance = new SmartHttpClient();

    public SmartHttpClient() {
        builder = new OkHttpClient.Builder();
    }

    public static SmartHttpClient newClient() {
        return instance;
    }

    /**
     * log打印级别，决定了log显示的详细程度
     *
     * @return
     */
    public SmartHttpClient configLog(String logTag) {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(TAG + "_" + logTag);
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);
        return this;
    }

    /**
     * 配置超时时间
     *
     * @return
     */
    public SmartHttpClient configTimeout() {
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        return this;
    }

    public OkHttpClient build() {
        return builder.build();
    }
}
