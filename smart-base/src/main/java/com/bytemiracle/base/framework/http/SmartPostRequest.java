package com.bytemiracle.base.framework.http;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 类功能：基于okGo封装的POST请求
 *
 * @author gwwang
 * @date 2021/3/12 10:14
 */
public class SmartPostRequest extends BaseRequest {
    private static final String TAG = "SmartPostRequest";
    private static final SmartPostRequest instance = new SmartPostRequest();

    public static SmartPostRequest get() {
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
        return asyncRequestApi(methodName, new HashMap<>(), params, true, resultListener);
    }

    /**
     * 创建api form表单请求
     *
     * @param methodName
     * @param params
     * @param
     * @return
     */
    public Object asyncRequestApiUseForm(String methodName, Map<String, Object> params, OnReceiveResult resultListener) {
        return asyncRequestApi(methodName, new HashMap<>(), params, false, resultListener);
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
    public Object asyncRequestApi(String methodName, Map<String, Object> headers, Map<String, Object> params, boolean useJson, OnReceiveResult resultListener) {
        return asyncPostRequest(OkGoHttp.BASE_API_URL, methodName, headers, params, useJson, resultListener);
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
        return asyncPostRequest(OkGoHttp.BASE_MACHINE_URL, methodName, headers, params, true, resultListener);
    }


    /**
     * 创建get请求
     *
     * @param baseUrl    baseUrl的路径
     * @param methodName 请求的action
     * @param headers    请求头
     * @param params     请求参数
     * @param useJson    使用json参数
     * @return
     */
    protected Object asyncPostRequest(String baseUrl, String methodName,
                                      Map<String, Object> headers,
                                      Map<String, Object> params,
                                      boolean useJson,
                                      OnReceiveResult resultListener) {
        Object requestTag = generateRequestTag(methodName);
        PostRequest<String> postRequest = OkGo
                .<String>post(baseUrl + methodName)
                .tag(requestTag)
                .headers(buildHttpHeaders(headers));
        if (useJson) {
            postRequest.upJson(buildHttpJson(params));
        } else {
            postRequest.params(buildHttpParams(params));
        }
        postRequest.execute(new StringCallback() {
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
