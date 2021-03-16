package com.bytemiracle.base.framework.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.component.BaseActivity;
import com.bytemiracle.base.framework.view.apptitle.AppTitleController;
import com.xuexiang.xui.widget.tabbar.VerticalTabLayout;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类功能：基础fragment
 *
 * @author gwwang
 * @date 2021/1/7 16:20
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";

    protected AppTitleController appTitleController;

    private Unbinder mUnbinder;
    protected View mRootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, mRootView);
        initArgs();
        initCommonTitleBar();
        initViews();
        //首次add不走onHiddenChanged(),需要此处调用
        initViewsData();
        return mRootView;
    }

    protected abstract int getLayoutId();

    protected void initArgs() {
    }

    protected void initViews() {
    }

    protected void switchContentFragment(Class<? extends BaseFragment> clazz) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).openContentFragment(clazz);
        }
    }

    protected void popFragment(Class<? extends BaseFragment> clazz) {
        FragmentActivity activity = getActivity();
        if (activity instanceof BaseActivity) {
            ((BaseActivity) activity).popFragment(clazz);
        }
    }

    private void initCommonTitleBar() {
        if (showTitleBar()) {
            appTitleController = new AppTitleController(getContext()).insert2Parent((ViewGroup) mRootView, 0);
            appTitleController.ivBack.setOnClickListener(getLeftClickListener());
            appTitleController.tvTitle.setText(new AnnotationPresenter(getClass()).findDefinedFragmentTag());
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.dpx_45);
            appTitleController.wrapShadowEffect(mRootView, dimensionPixelSize);
        }
    }

    public AppTitleController getAppTitleController() {
        return appTitleController;
    }

    /**
     * 非首次add，使用该方法刷新数据
     *
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initViewsData();
        }
    }

    protected void initViewsData() {

    }

    protected boolean showTitleBar() {
        return false;
    }

    /**
     * fragment中的childFragment是否显示
     */
    public boolean isHidden = false;

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    /**
     * 三方控件第一次不走监听的问题
     *
     * @param viewObject
     */
    public void wrapVerticalTabLayoutBugs(VerticalTabLayout viewObject) {
        AnnotationPresenter annotationPresenter = new AnnotationPresenter(VerticalTabLayout.class);
        annotationPresenter.setPrivateFieldValue(viewObject, "mSelectedTab", null);
    }

    /**
     * 是否允许系统返回按键返回home
     *
     * @return
     */
    public boolean allowBackHomeFragment() {
        return true;
    }

    protected View.OnClickListener getLeftClickListener() {
        return v -> {
            if (allowBackHomeFragment()) {
                getActivity().finish();
            }
        };
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }
}
