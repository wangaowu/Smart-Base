package com.bytemiracle.base.framework;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.util.List;


/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/2/25 10:14
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        GlobalInstanceHolder.setApplicationContext(this);
        GlobalInstanceHolder.setMainHandler(new Handler(Looper.getMainLooper()));
    }

    public String getProcessName(Context cxt) {
        int pid = android.os.Process.myPid();
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}
