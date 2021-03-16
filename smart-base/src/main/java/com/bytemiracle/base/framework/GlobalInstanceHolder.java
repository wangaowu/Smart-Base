package com.bytemiracle.base.framework;

import android.app.Application;
import android.os.Handler;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.component.BaseActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/2/25 15:58
 */
public class GlobalInstanceHolder {
    private static Executor singleExecutor = Executors.newFixedThreadPool(2);

    private static BaseActivity rootActivity;

    private static Application applicationContext;

    private static Handler mainHandler;

    //两个单线程，随便用一个
    private static Executor[] executors = new Executor[]{
            Executors.newSingleThreadExecutor(),
            Executors.newSingleThreadExecutor()
    };

    public static void setApplicationContext(Application context) {
        GlobalInstanceHolder.applicationContext = context;
    }

    public static void setRootActivityContext(BaseActivity context) {
        GlobalInstanceHolder.rootActivity = context;
    }

    public static BaseActivity rootActivity() {
        return GlobalInstanceHolder.rootActivity;
    }

    public static Application applicationContext() {
        return GlobalInstanceHolder.applicationContext;
    }

    public static void setMainHandler(Handler mainHandler) {
        GlobalInstanceHolder.mainHandler = mainHandler;
    }

    public static Handler mainHandler() {
        return GlobalInstanceHolder.mainHandler;
    }

    public static Executor newSingleExecutor() {
        return executors[0];
    }

    public static int dp5() {
        return applicationContext().getResources().getDimensionPixelSize(R.dimen.dpx_5);
    }

    public static int sp14() {
        return applicationContext().getResources().getDimensionPixelSize(R.dimen.spx_14);
    }

    public static int sp18() {
        return applicationContext().getResources().getDimensionPixelSize(R.dimen.spx_18);
    }

    public static void runAsyncTask(Runnable asyncTask, Runnable uiTask) {
        singleExecutor.execute(new Runnable() {
            @Override
            public void run() {
                asyncTask.run();
                mainHandler().post(uiTask);
            }
        });
    }

}
