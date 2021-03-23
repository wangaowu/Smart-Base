package com.bytemiracle.base.framework.component;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.ColorUtils;
import androidx.fragment.app.Fragment;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.fragment.AnnotationPresenter;
import com.bytemiracle.base.framework.fragment.BaseFragment;
import com.bytemiracle.base.framework.fragment.CoreFragmentManager;
import com.bytemiracle.base.framework.utils.app.SoftKeyboardUtils;
import com.bytemiracle.base.framework.utils.common.ListUtils;
import com.bytemiracle.base.framework.view.ShadowLinearLayout;
import com.bytemiracle.base.framework.view.apptitle.AppTitleController;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xui.widget.spinner.materialspinner.MaterialSpinner;
import com.xuexiang.xui.widget.tabbar.VerticalTabLayout;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 基础容器Activity
 *
 * @author gwwang
 * @since 2021/02/23 11:21
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    public static final int STATUS_BAR_HEIGHT = 100;

    Unbinder mUnbinder;

    protected ShadowLinearLayout contentContainer;
    protected CoreFragmentManager coreFragmentManager;
    private AppTitleController appTitleController;
    private int rootFlContainerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int statusBarColor = getStatusBarColor();
        contentContainer = initStatusBarStyle(statusBarColor);
        super.onCreate(savedInstanceState);
        setContentView();
        mUnbinder = ButterKnife.bind(this);
        addStatusBarColor(statusBarColor);
        addCommonTitleBar();
        initViewsWithSavedInstanceState(savedInstanceState);
    }

    protected void initViewsWithSavedInstanceState(Bundle savedInstanceState) {

    }

    /**
     * 设置根布局
     */
    protected void setContentView() {
        int layoutId = getLayoutId();
        boolean isNoneLayout = layoutId == -1;
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        contentContainer.addView(isNoneLayout ? generateBaseLayout() : LayoutInflater.from(this).inflate(layoutId, null), layoutParams);
        setContentView(contentContainer);
    }

    protected int getLayoutId() {
        return -1;
    }

    private FrameLayout generateBaseLayout() {
        this.rootFlContainerId = View.generateViewId();
        coreFragmentManager = CoreFragmentManager.newInstance(this, rootFlContainerId);
        FrameLayout baseLayout = new FrameLayout(this);
        baseLayout.setId(rootFlContainerId);
        baseLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return baseLayout;
    }

    public void openContentFragment(Class<? extends BaseFragment> clazz) {
        coreFragmentManager.switch2Fragment(clazz);
    }

    public void popFragment(Class<? extends BaseFragment> clazz) {
        coreFragmentManager.popFragment(new AnnotationPresenter(clazz).findDefinedFragmentTag());
    }

    protected boolean isShowFragment(Class<? extends BaseFragment> clazz) {
        BaseFragment fragment = coreFragmentManager.findFragment(clazz);
        if (fragment != null) {
            return !fragment.isHidden;
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    /**
     * 设置顶部状态栏颜色
     */
    protected int getStatusBarColor() {
        return getColor(R.color.app_common_status_bar_color);
    }

    /**
     * 设置顶部状态栏颜色
     *
     * @return
     */
    protected int getStatusBarHeight() {
        return STATUS_BAR_HEIGHT;
    }

    /**
     * 高亮标题栏颜色
     *
     * @return
     */
    protected boolean needLightTitleBarChild() {
        return false;
    }

    /**
     * 获取右侧spinner控件
     *
     * @return
     */
    public MaterialSpinner getBarRightSpinner() {
        return appTitleController.getRightSpinner();
    }

    /**
     * 获取右侧按钮图标
     *
     * @return
     */
    public Button getBarRightButton() {
        return appTitleController.getRightButton();
    }

    /**
     * 初始化顶部状态栏
     */
    private void addStatusBarColor(int statusBarColor) {
        View statusBarView = new View(this);
        // StatusBarUtils.getStatusBarHeight(this);
        statusBarView.setBackgroundColor(statusBarColor);
        contentContainer.addView(statusBarView, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getStatusBarHeight()));
    }

    private ShadowLinearLayout initStatusBarStyle(int color) {
        StatusBarUtils.initStatusBarStyle(this, !isLightColor(color), Color.TRANSPARENT);
        ShadowLinearLayout contentContainer = new ShadowLinearLayout(this);
        contentContainer.setOrientation(LinearLayout.VERTICAL);
        return contentContainer;
    }

    protected String showTitleBar() {
        return null;
    }

    private void addCommonTitleBar() {
        String title = showTitleBar();
        if (!TextUtils.isEmpty(title)) {
            appTitleController = new AppTitleController(this).insert2Parent((ViewGroup) contentContainer, 1);
            appTitleController.ivBack.setOnClickListener(getLeftClickListener());
            appTitleController.resetBackgroundColor(getStatusBarColor());
            appTitleController.setNeedLight(needLightTitleBarChild());
            appTitleController.tvTitle.setText(title);
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.dpx_45);
            appTitleController.wrapShadowEffect(contentContainer, dimensionPixelSize + getStatusBarHeight());
        }
    }

    public void updateTitleText(String titleText) {
        if (appTitleController != null) {
            appTitleController.tvTitle.setText(titleText);
        }
    }

    private View.OnClickListener getLeftClickListener() {
        return v -> {
            if (allowBackHome()) {
                finish();
            }
        };
    }

    /**
     * 是否亮色
     *
     * @param color
     * @return
     */
    private boolean isLightColor(int color) {
        double v = ColorUtils.calculateLuminance(color);
        return v >= 0.5;
    }

    /**
     * 软键盘是否展示
     *
     * @return
     */
    public boolean isShowSoftKeyboard() {
        return SoftKeyboardUtils.isShowing(this);
    }

    /**
     * 隐藏软键盘
     */
    public void hideSoftKeyboard(View v) {
        SoftKeyboardUtils.hideSoftInput(v);
    }

    /**
     * 是否允许回退到主页fragment
     */
    protected boolean allowBackHome() {
        CoreFragmentManager coreFragmentManager = CoreFragmentManager.newInstance(this, this.rootFlContainerId);
        List<BaseFragment> fragments = coreFragmentManager.getFragments();
        if (!ListUtils.isEmpty(fragments)) {
            for (Fragment fragment : fragments) {
                BaseFragment bf = (BaseFragment) fragment;
                if (!bf.allowBackHomeFragment()) {
                    return false;
                }
            }
        }
        return true;
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

    @Override
    public void onBackPressed() {
        if (!allowBackHome()) {
            return;
        }
        super.onBackPressed();
    }

    /**
     * -------------------------------------点击非输入区域键盘消失--------------------------------------------
     **/
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (SoftKeyboardUtils.isShouldHideInput(v, ev)) {
                hideSoftKeyboard(v);
            }
        }
        return super.dispatchTouchEvent(ev);
    }
}
