package com.bytemiracle.base.framework.update;

import com.bytemiracle.base.framework.component.BaseActivity;
import com.bytemiracle.base.framework.listener.CommonAsyncListener;
import com.bytemiracle.base.framework.utils.XToastUtils;

/**
 * 类功能：自动升级
 *
 * @author gwwang
 * @date 2021/3/10 11:43
 */
public class AutoUpdatePresenter extends UpdatePresenter {
    /**
     * 构造方法
     *
     * @param attachActivity 弹窗所依赖的界面
     */
    public AutoUpdatePresenter(BaseActivity attachActivity) {
        super(attachActivity);
    }

    public void autoUpdate() {
        asyncGetNewLastVersion(data -> updateNewVersion(new CommonAsyncListener<UpdateResponse>() {
            @Override
            public void doSomething(UpdateResponse data) {
                if (data == null) {
                    XToastUtils.info("升级失败!");
                    return;
                }
                new UpdateDialogController(attachActivity.get()).createDialog(data).show();
            }
        }));
    }
}
