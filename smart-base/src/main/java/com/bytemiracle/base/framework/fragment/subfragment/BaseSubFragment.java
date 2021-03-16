package com.bytemiracle.base.framework.fragment.subfragment;

import android.view.View;
import android.widget.LinearLayout;

import com.bytemiracle.base.framework.fragment.BaseFragment;
import com.bytemiracle.base.framework.fragment.subfragment.config.SubTitle;
import com.bytemiracle.base.framework.fragment.subfragment.config.SubTitleConfig;

/**
 * 类功能：基础fragment(存在于fragment内的fragment)
 *
 * @author gwwang
 * @date 2021/1/7 16:20
 */
public abstract class BaseSubFragment extends BaseFragment {
    private static final String TAG = "BaseSubFragment";
    private SubTitle subTitle;

    @Override
    protected void initViews() {
        initSubTitleLayout();
        super.initViews();
    }

    @Override
    protected boolean showTitleBar() {
        return false;
    }

    protected abstract SubTitleConfig getSubTitleConfig();

    protected void initSubTitleLayout() {
        SubTitleConfig subTitleConfig = getSubTitleConfig();
        if (subTitleConfig != null) {
            if (mRootView instanceof LinearLayout && ((LinearLayout) mRootView).getOrientation() == LinearLayout.VERTICAL) {
                //竖向的线性布局才可以添加title
                LinearLayout rootView = (LinearLayout) mRootView;
                subTitle = new SubTitle(subTitleConfig);
                rootView.addView(subTitle.createSettingsSubTitle(getContext()), 0);
            }
        }
    }

    protected View getSubTitleRightButton() {
        return subTitle.getRightButton();
    }
}
