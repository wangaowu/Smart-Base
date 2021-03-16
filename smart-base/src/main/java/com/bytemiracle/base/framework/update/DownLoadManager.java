package com.bytemiracle.base.framework.update;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.bytemiracle.base.framework.GlobalInstanceHolder;
import com.bytemiracle.base.framework.http.SmartDownloadRequest;
import com.bytemiracle.base.framework.utils.app.ApkInstallUtils;
import com.bytemiracle.base.framework.utils.file.FileUtil;
import com.bytemiracle.base.framework.utils.XToastUtils;

import java.io.File;
import java.io.IOException;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/3/11 11:05
 */
public class DownLoadManager {
    private static final DownLoadManager instance = new DownLoadManager();


    private DownLoadManager() {
    }

    /**
     * DownLoadManager实例
     *
     * @return
     */
    public static DownLoadManager getInstance() {
        return instance;
    }

    /**
     * 在服务内下载文件
     *
     * @param fileServerUrl
     * @param destFileDir
     * @param onDownloadChangeListener
     */
    public void downloadFileOnService(String fileServerUrl, String destFileDir, SmartDownloadRequest.OnDownloadChangeListener onDownloadChangeListener) {
        Intent intent = new Intent(GlobalInstanceHolder.applicationContext(), DownloadFileService.class);
        GlobalInstanceHolder.applicationContext().bindService(intent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                DownloadFileService.DownloadFileBinder binder = (DownloadFileService.DownloadFileBinder) service;
                binder.startDownload(fileServerUrl,
                        destFileDir,
                        //给正在下载的文件追加temp标记
                        FileUtil.appendTempFlag(FileUtil.getFileDisplayName(fileServerUrl)),
                        new SmartDownloadRequest.OnDownloadChangeListener() {
                            @Override
                            public void onProgress(float fraction) {
                                onDownloadChangeListener.onProgress(fraction);
                            }

                            @Override
                            public void onSuccess(File file) {
                                //给下载完成的文件移除temp后缀
                                FileUtil.removeTempFlag(file.getAbsolutePath());
                                onDownloadChangeListener.onSuccess(file);
                            }

                            @Override
                            public void onFailed(String reason) {
                                onDownloadChangeListener.onFailed(reason);
                            }
                        });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                onDownloadChangeListener.onFailed("下载断开");
                XToastUtils.info("下载断开!");
            }
        }, Context.BIND_AUTO_CREATE);
    }

    /**
     * apk是否存在
     *
     * @param apkName
     * @return
     */
    public boolean isExist(String apkName) {
        return FileUtil.fileIsExists(UpdateComponent.DOWNLOAD_DIR + File.separator + apkName);
    }

    /**
     * 安装apk文件
     *
     * @param apkName
     */
    public void installApk(String apkName) {
        try {
            File apkFile = new File(UpdateComponent.DOWNLOAD_DIR + File.separator + apkName);
            ApkInstallUtils.install(GlobalInstanceHolder.applicationContext(), apkFile);
        } catch (IOException e) {
            XToastUtils.info("获取apk的路径出错,安装失败!");
        }
    }
}
