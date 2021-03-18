package com.bytemiracle.base.framework.http;

import com.bytemiracle.base.framework.GlobalInstanceHolder;
import com.bytemiracle.base.framework.fragment.AnnotationPresenter;
import com.bytemiracle.base.framework.utils.XToastUtils;
import com.bytemiracle.base.framework.utils.json.JsonParser;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/3/15 13:43
 */
public class BaseRequest {

    /**
     * 构建请求参数
     *
     * @param params
     * @return
     */
    protected HttpParams buildHttpParams(Map<String, Object> params) {
        HttpParams httpParams = new HttpParams();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                httpParams.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return httpParams;
    }

    /**
     * 构建请求参数
     *
     * @param params
     * @return
     */
    protected String buildHttpJson(Map<String, Object> params) {
        return JsonParser.toJson(wrapObjectElement(params));
    }


    /**
     * 构建请求头
     *
     * @param headers
     * @return
     */
    public static HttpHeaders buildHttpHeaders(Map<String, Object> headers) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, Object> entry : headers.entrySet()) {
                httpHeaders.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return httpHeaders;
    }

    /**
     * 将object类型元素转化为string类型
     *
     * @param maps
     * @return
     */
    protected static Map<String, String> wrapObjectElement(Map<String, Object> maps) {
        Map<String, String> stringMap = new HashMap<>();
        if (maps != null && !maps.isEmpty()) {
            for (Map.Entry<String, Object> entry : maps.entrySet()) {
                stringMap.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return stringMap;
    }

    /**
     * 生成请求tag
     *
     * @param methodName
     * @return
     */
    protected Object generateRequestTag(String methodName) {
        return methodName + System.currentTimeMillis();
    }

    /**
     * 处理请求失败
     *
     * @param response
     * @param resultListener
     */
    protected void processFailed(Response<String> response, OnReceiveResult resultListener) {
        try {
            Class<? extends Throwable> causeReason = response.getException().getCause().getClass();
            if (causeReason.getName().contains("GaiException")) {
                //没有网络
                XToastUtils.info("请检查网络设置");
                GlobalInstanceHolder.mainHandler().postDelayed(() -> resultListener.onFailed("请检查网络设置"), 1000);
            } else {
                GlobalInstanceHolder.mainHandler().post(() -> resultListener.onFailed(response.getException().getCause().getMessage()));
            }
        } catch (Exception e) {
            resultListener.onFailed(e.toString());
        }
    }

    /**
     * 处理请求成功
     *
     * @param response
     * @param resultListener
     */
    protected void processSuccess(Response<String> response, OnReceiveResult resultListener) {
        String jsonBody = response.body();
        //任何情况，都回调原始json数据（使用方选择性重写）
        resultListener.onJson(jsonBody);
        Type genType = new AnnotationPresenter(resultListener.getClass()).getGenType();
        if (genType.getClass().getName().contains("Object") || genType.getClass().getName().contains("String")) {
            //如果传入的泛型是Object|String|没传，就在onSuccess回调原始json数据
            resultListener.onSuccess(jsonBody);
        } else {
            //回调解析后的json数据
            resultListener.onSuccess(JsonParser.fromJson(jsonBody, genType));
        }
    }
}
