package com.bytemiracle.base.framework.update;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bytemiracle.base.framework.http.OkGoHttp;
import com.bytemiracle.base.framework.http.SmartDownloadRequest;

/**
 * 类功能：下载文件服务
 *
 * @author gwwang
 * @date 2021/3/10 11:32
 */
public class DownloadFileService extends Service {
    private static final String TAG = "DownloadFileService";
    private static final int DOWNLOAD_NOTIFY_ID = 1000;
    private static final String CHANNEL_ID = "channel_id";
    private static final CharSequence CHANNEL_NAME = "channel_name";

    private DownloadFileBinder downloadFileBinder = new DownloadFileBinder();


    @Override
    public void onCreate() {
        super.onCreate();
        UpdateComponent.setUpdateRunning(true);
        initNotification();
    }

    /**
     * 初始化通知
     */
    private void initNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(false);
            channel.enableLights(false);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = getNotificationBuilder();
        notificationManager.notify(DOWNLOAD_NOTIFY_ID, mBuilder.build());
    }

    private NotificationCompat.Builder getNotificationBuilder() {
        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("正在更新")
                .setSmallIcon(UpdateComponent.appLogo)
                .setOngoing(true)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
    }

    /**
     * 更新监听代理对象
     */
    public class DownloadFileBinder extends Binder {
        public void startDownload(String downloadUrl, String destFileDir, String destFileName, SmartDownloadRequest.OnDownloadChangeListener downLoadListener) {
            OkGoHttp.newDownloadRequest().asyncDownloadRequest(downloadUrl, destFileDir, destFileName, downLoadListener);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return downloadFileBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        UpdateComponent.setUpdateRunning(false);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        UpdateComponent.setUpdateRunning(false);
        super.onDestroy();
    }
}