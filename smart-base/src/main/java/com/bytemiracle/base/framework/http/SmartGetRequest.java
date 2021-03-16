package com.bytemiracle.base.framework.http;


import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.HashMap;
import java.util.Map;

/**
 * 类功能：基于okGo封装哥GET请求
 *
 * @author gwwang
 * @date 2021/3/12 10:14
 */
public class SmartGetRequest extends BaseRequest {
    private static final String TAG = "SmartGetRequest";
    private static final SmartGetRequest instance = new SmartGetRequest();

    public static SmartGetRequest get() {
        return instance;
    }

    /**
     * 创建api请求
     *
     * @param methodName
     * @param params
     * @param
     * @return
     */
    public Object asyncRequestApi(String methodName, Map<String, Object> params, OnReceiveResult resultListener) {
        return asyncRequestApi(methodName, new HashMap<>(), params, resultListener);
    }

    /**
     * 创建api请求
     *
     * @param methodName
     * @param headers
     * @param params
     * @param
     * @return
     */
    public Object asyncRequestApi(String methodName, Map<String, Object> headers, Map<String, Object> params, OnReceiveResult resultListener) {
        return asyncGetRequest(OkGoHttp.BASE_API_URL, methodName, headers, params, resultListener);
    }

    /**
     * 创建machine请求
     *
     * @param methodName
     * @param params
     * @param
     * @return
     */
    public Object asyncRequestMachine(String methodName, Map<String, Object> params, OnReceiveResult resultListener) {
        return asyncRequestMachine(methodName, new HashMap<>(), params, resultListener);
    }

    /**
     * 创建machine请求
     *
     * @param methodName
     * @param headers
     * @param params
     * @param
     * @return
     */
    public Object asyncRequestMachine(String methodName, Map<String, Object> headers, Map<String, Object> params, OnReceiveResult resultListener) {
        return asyncGetRequest(OkGoHttp.BASE_MACHINE_URL, methodName, headers, params, resultListener);
    }

    /**
     * 创建get请求
     *
     * @param baseUrl    baseUrl的路径
     * @param methodName 请求的action
     * @param headers    请求头
     * @param params     请求参数
     * @return
     */
    protected Object asyncGetRequest(String baseUrl, String methodName, Map<String, Object> headers, Map<String, Object> params, OnReceiveResult resultListener) {
        Object requestTag = generateRequestTag(methodName);
        OkGo.<String>get(baseUrl + methodName)
                .tag(requestTag)
                .headers(buildHttpHeaders(headers))
                .params(buildHttpParams(params))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        processSuccess(response, resultListener);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        processFailed(response, resultListener);
                    }
                });
        return requestTag;
    }
}
