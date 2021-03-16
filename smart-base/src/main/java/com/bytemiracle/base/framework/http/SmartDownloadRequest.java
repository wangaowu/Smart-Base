package com.bytemiracle.base.framework.http;

import android.util.Log;

import com.bytemiracle.base.framework.GlobalInstanceHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * 类功能：基于okGo封装的下载请求
 *
 * @author gwwang
 * @date 2021/3/12 10:14
 */
public class SmartDownloadRequest extends BaseRequest {
    private static final String TAG = "SmartDownloadRequest";
    private static final SmartDownloadRequest instance = new SmartDownloadRequest();

    public interface OnDownloadChangeListener {
        void onProgress(float fraction);

        void onSuccess(File file);

        void onFailed(String reason);
    }

    public static SmartDownloadRequest get() {
        return instance;
    }

    /**
     * 创建get请求
     *
     * @param fileRemoteUrl  文件的服务器路径
     * @param descFileDir    本地文件的存储路径
     * @param descFileName   本地文件名
     * @param resultListener 下载回调
     * @return
     */
    public Object asyncDownloadRequest(String fileRemoteUrl,
                                       String descFileDir,
                                       String descFileName,
                                       OnDownloadChangeListener resultListener) {
        return asyncDownloadRequest(fileRemoteUrl, descFileDir, descFileName, new HashMap<>(), new HashMap<>(), resultListener);
    }


    /**
     * 创建get请求
     *
     * @param fileRemoteUrl  文件的服务器路径
     * @param descFileDir    本地文件的存储路径
     * @param descFileName   本地文件名
     * @param headers        请求头
     * @param params         请求参数
     * @param resultListener 下载回调
     * @return
     */
    protected Object asyncDownloadRequest(String fileRemoteUrl,
                                          String descFileDir,
                                          String descFileName,
                                          Map<String, Object> headers,
                                          Map<String, Object> params,
                                          OnDownloadChangeListener resultListener) {
        Object requestTag = generateRequestTag(fileRemoteUrl);
        OkGo.<File>get(fileRemoteUrl)
                .tag(requestTag)
                .headers(buildHttpHeaders(headers))
                .params(buildHttpParams(params))
                .execute(new FileCallback(descFileDir, descFileName) {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        Log.d(TAG, "开始下载:" + fileRemoteUrl);
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        GlobalInstanceHolder.mainHandler().post(() -> resultListener.onSuccess(response.body()));
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        GlobalInstanceHolder.mainHandler().post(() -> resultListener.onProgress(progress.fraction));
                    }

                    @Override
                    public void onError(Response<File> response) {
                        try {
                            Class<? extends Throwable> causeReason = response.getException().getCause().getClass();
                            if (causeReason.getName().contains("GaiException")) {
                                GlobalInstanceHolder.mainHandler().post(() -> resultListener.onFailed("请检查网络设置"));
                            }
                        } catch (Exception e) {
                            GlobalInstanceHolder.mainHandler().post(() -> resultListener.onFailed(e.toString()));
                        }
                    }
                });
        return requestTag;
    }
}
