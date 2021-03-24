package com.bytemiracle.base.framework.component;

import android.os.Bundle;

import com.bytemiracle.base.framework.update.UpdatePresenter;
import com.bytemiracle.base.framework.utils.sp.EasySharedPreference;

/**
 * 类功能：初始化activity
 *
 * @author gwwang
 * @date 2021/3/2 13:08
 */
public abstract class AbstractSplashActivity extends BaseActivity {
    public static final String TAG = "SplashActivity";
    public static final String KEY_VERSION_CODE = "version_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断升级
        int preVersionCode = EasySharedPreference.get().getInt(KEY_VERSION_CODE);
        int currentVersionCode = new UpdatePresenter(this).getVersionCode();
        //首次安装或者升级
        if (preVersionCode == 0 || currentVersionCode <= preVersionCode) {
            doOnResume();
            return;
        }
        //本次启动是升级
        if (onUpdateVersionCode()) {
            doOnResume();
        }
        EasySharedPreference.get().putInt(KEY_VERSION_CODE, currentVersionCode);
    }

    protected abstract void doOnResume();

    /**
     * 版本号升级
     */
    protected abstract boolean onUpdateVersionCode();
}
