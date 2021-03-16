package com.bytemiracle.base.framework.http;

import com.bytemiracle.base.framework.listener.CommonAsync2Listener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类功能：基于okGo封装的上传请求
 *
 * @author gwwang
 * @date 2021/3/12 10:14
 */
public class SmartUploadRequest extends BaseRequest {
    private static final String TAG = "SmartUploadRequest";
    private static final SmartUploadRequest instance = new SmartUploadRequest();

    public static SmartUploadRequest get() {
        return instance;
    }

    /**
     * 创建get请求
     *
     * @param methodName     文件的服务器路径
     * @param params         请求参数
     *                       * @param multiFilesKey  上传文件的key
     * @param uploadFiles    上传的文件
     * @param uploadListener 上传回调
     * @return
     */
    public Object asyncUploadRequest(String methodName,
                                     Map<String, Object> params,
                                     String multiFilesKey,
                                     List<File> uploadFiles,
                                     CommonAsync2Listener uploadListener) {
        return asyncUploadRequest(methodName, new HashMap<>(), params, multiFilesKey, uploadFiles, uploadListener);
    }

    /**
     * 创建get请求
     *
     * @param methodName     文件的服务器路径
     * @param headers        请求头
     * @param params         请求参数
     * @param multiFilesKey  上传文件的key
     * @param uploadFiles    上传的文件
     * @param uploadListener 上传回调
     * @return
     */
    protected Object asyncUploadRequest(String methodName,
                                        Map<String, Object> headers,
                                        Map<String, Object> params,
                                        String multiFilesKey,
                                        List<File> uploadFiles,
                                        CommonAsync2Listener uploadListener) {
        Object requestTag = generateRequestTag(methodName);
        OkGo.<String>post(OkGoHttp.BASE_API_URL + methodName)
                .tag(requestTag)//
                .headers(buildHttpHeaders(headers))
                .params(buildHttpParams(params))
                .addFileParams(multiFilesKey, uploadFiles)           // 这种方式为同一个key，上传多个文件
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        uploadListener.doSomething1("上传成功");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        uploadListener.doSomething2("上传失败");
                    }
                });
        return requestTag;
    }
}
