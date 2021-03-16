package com.bytemiracle.base.framework.update;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.bytemiracle.base.framework.component.AbstractSplashActivity;
import com.bytemiracle.base.framework.component.BaseActivity;
import com.bytemiracle.base.framework.http.OkGoHttp;
import com.bytemiracle.base.framework.http.OnReceiveResult;
import com.bytemiracle.base.framework.listener.CommonAsyncListener;
import com.bytemiracle.base.framework.update.bean.UpdateVersionResponse;
import com.bytemiracle.base.framework.utils.sp.EasySharedPreference;
import com.bytemiracle.base.framework.utils.XToastUtils;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * 类功能：升级业务类
 *
 * @author gwwang
 * @date 2021/3/10 11:02
 */
public class UpdatePresenter {
    private static final int UNKNOWN_VERSION_CODE = 999;
    private static final String UNKNOWN_VERSION_NAME = "最新版本";
    protected WeakReference<BaseActivity> attachActivity;
    private int newVersionCode;

    /**
     * 构造方法
     *
     * @param attachActivity 弹窗所依赖的界面
     */
    public UpdatePresenter(BaseActivity attachActivity) {
        this.attachActivity = new WeakReference<>(attachActivity);
    }

    /**
     * 获取版本名称
     *
     * @return
     */
    public String getVersionName() {
        try {
            PackageManager manager = attachActivity.get().getPackageManager();
            PackageInfo pi = manager.getPackageInfo(attachActivity.get().getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return UNKNOWN_VERSION_NAME;
        }
    }

    /**
     * 获取当前最新版本号
     * <p>
     * VersionCode	当前软件版本
     * ProjectName	项目名称
     */
    public void asyncGetNewLastVersion(CommonAsyncListener<String> listener) {
        int curVersionCode = EasySharedPreference.get().getInt(AbstractSplashActivity.KEY_VERSION_CODE);
        Map headers = new HashMap<String, Object>();
        headers.put("Authorization", "Admin@ShaanxiByteMiracle");

        Map params = new HashMap<String, String>();
        params.put("VersionCode", curVersionCode);
        params.put("ProjectName", UpdateComponent.appName); //ServerCenter/Base_Project/Authorize

        OkGoHttp.newPostRequest().asyncRequestMachine("/ServerCenter/Base_Upgrade/CompareVersion",
                headers, params, new OnReceiveResult<UpdateVersionResponse>() {
                    @Override
                    public void onSuccess(UpdateVersionResponse resultBean) {
                        if (newVersionCode == 0) {
                            UpdatePresenter.this.newVersionCode = curVersionCode;
                            listener.doSomething(getVersionName());
                        } else {
                            UpdatePresenter.this.newVersionCode = resultBean.getData().getVersionCode();
                            listener.doSomething(resultBean.getData().getVersionName());
                        }
                    }

                    @Override
                    public void onFailed(String reason) {
                        UpdatePresenter.this.newVersionCode = curVersionCode;
                        listener.doSomething(getVersionName());
                    }
                });
    }

    public void updateNewVersion(CommonAsyncListener<UpdateResponse> responseListener) {
        int curVersionCode = EasySharedPreference.get().getInt(AbstractSplashActivity.KEY_VERSION_CODE);
        if (curVersionCode >= newVersionCode) {
            XToastUtils.toast("当前为最新版本！");
            return;
        }
        Map params = new HashMap<String, String>();
        params.put("VersionCode", curVersionCode);
        params.put("ProjectName", UpdateComponent.appName);

        Map commonHeaders = new HashMap<String, Object>();
        commonHeaders.put("Authorization", "Admin@ShaanxiByteMiracle");

        OkGoHttp.newPostRequest().asyncRequestMachine("/ServerCenter/Base_Upgrade/GetPackageFile",
                commonHeaders, params, new OnReceiveResult<UpdateResponse>() {
                    @Override
                    public void onSuccess(UpdateResponse resultBean) {
                        responseListener.doSomething(resultBean);
                    }

                    @Override
                    public void onFailed(String reason) {
                        responseListener.doSomething(null);
                    }
                });
    }
}
