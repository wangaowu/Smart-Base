package com.bytemiracle.base.framework.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownLoadUtil {

    public static final String DOWNLOADPATH = Environment.getExternalStorageDirectory() + File.separator + "smart-assistant" + File.separator+"msgFiles"+ File.separator;

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(600000, TimeUnit.MILLISECONDS)
            .readTimeout(10000,TimeUnit.MILLISECONDS)
            .writeTimeout(10000,TimeUnit.MILLISECONDS).build();

    //下载文件方法
    public static void downloadFile(String url, final ProgressListener listener, Callback callback){
        //增加拦截器
        OkHttpClient client = okHttpClient.newBuilder().addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response response = chain.proceed(chain.request());
                return response.newBuilder().body(new ProgressResponseBody(response.body(), listener)).build();
            }
        }).build();

        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(callback);
    }




}
