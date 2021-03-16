package com.bytemiracle.base.framework.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSON;
import com.bytemiracle.base.framework.utils.common.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 类功能：CoreFragmentManager
 *
 * @author gwwang
 * @date 2021/1/7 14:08
 */
public class CoreFragmentManager {
    private static final String TAG = "CoreFragmentManager";

    private FragmentManager coreFragmentManager;
    private int fragmentContainerId;

    private CoreFragmentManager(Object componentThis, int containerId) {
        if (componentThis instanceof Activity) {
            this.coreFragmentManager = ((AppCompatActivity) componentThis).getSupportFragmentManager();
        } else if (componentThis instanceof BaseFragment) {
            this.coreFragmentManager = ((BaseFragment) componentThis).getChildFragmentManager();
        }
//        else{
//          throw new Exception("组件必须实现 BaseFragment / AppCompatActivity");
//        }
        this.fragmentContainerId = containerId;
    }

    /**
     * 获得单例
     *
     * @param componentThis 组件实例
     * @param containerId   fragmentManager强关联的containerId
     */
    public static CoreFragmentManager newInstance(Object componentThis, int containerId) {
        return new CoreFragmentManager(componentThis, containerId);
    }

    /**
     * 切换到fragment
     *
     * @param fragmentClazz 字节码
     * @param data          数据
     * @param newStack      是否创建栈实例
     */
    public void switch2Fragment(Class<? extends BaseFragment> fragmentClazz, Bundle data, boolean newStack) {
        String fragmentTag = new AnnotationPresenter(fragmentClazz).findDefinedFragmentTag();
        int[] fragmentAnims = new AnnotationPresenter(fragmentClazz).findDefinedFragmentAnim();
        BaseFragment fragment = (BaseFragment) coreFragmentManager.findFragmentByTag(fragmentTag);
        if (fragment != null) {
            if (fragment.isHidden) {
                //已经add,但没有显示
                FragmentTransaction fragmentTransaction = hideOtherFragments(fragment);
                if (fragmentAnims != null) {
                    fragmentTransaction.setCustomAnimations(fragmentAnims[0], fragmentAnims[1]);
                }
                fragmentTransaction.show(fragment).commitAllowingStateLoss();
                fragment.setHidden(false);
            } else {
                //已经add,正在显示
            }
            return;
        }
        //未add
        addFragment(fragmentClazz, fragmentTag, fragmentAnims, newStack, data);
    }

    /**
     * 弹出切换到fragment
     *
     * @param popThis       弹出的字节码
     * @param fragmentClazz 切换到的字节码
     * @param data          数据
     */
    public void pop2Fragment(Class<? extends BaseFragment> popThis,
                             Class<? extends BaseFragment> fragmentClazz,
                             Bundle data) {
        if (popThis.getName().equals(fragmentClazz.getName())) {
            //自己替换自己使用replace形式，因异步
            replaceFragment(fragmentClazz, data);
        } else {
            //不同则使用弹栈的形式
            popFragment(new AnnotationPresenter(popThis).findDefinedFragmentTag());
            switch2Fragment(fragmentClazz, data);
        }
    }

    /**
     * 弹出切换到fragment
     *
     * @param popThis       弹出的字节码
     * @param fragmentClazz 切换到的字节码
     */
    public void pop2Fragment(Class<? extends BaseFragment> popThis,
                             Class<? extends BaseFragment> fragmentClazz) {
        pop2Fragment(popThis, fragmentClazz, null);
    }

    /**
     * 切换到fragment
     *
     * @param fragmentClazz 字节码
     */
    public void switch2Fragment(Class<? extends BaseFragment> fragmentClazz) {
        switch2Fragment(fragmentClazz, null);
    }

    /**
     * 切换到fragment
     *
     * @param fragmentClazz 字节码
     * @param data          数据
     */
    public void switch2Fragment(Class<? extends BaseFragment> fragmentClazz, Bundle data) {
        switch2Fragment(fragmentClazz, data, false);
    }

    /**
     * 查找fragment
     *
     * @param fragmentClazz
     * @param <T>
     * @return
     */
    public <T extends BaseFragment> T findFragment(Class<T> fragmentClazz) {
        String fragmentTag = new AnnotationPresenter(fragmentClazz).findDefinedFragmentTag();
        return (T) find(fragmentTag);
    }

    private Fragment find(String fragmentTag) {
        return coreFragmentManager.findFragmentByTag(fragmentTag);
    }

    /**
     * 该fragment是否正在展示
     *
     * @param fragmentTag
     * @return
     */
    public boolean isShow(String fragmentTag) {
        Fragment fragment = find(fragmentTag);
        if (fragment != null) {
            return !((BaseFragment) fragment).isHidden;
        }
        return false;
    }

    /**
     * 弹出该fragment
     *
     * @param fragmentTag
     */
    public void popFragment(String fragmentTag) {
        Fragment fragment = find(fragmentTag);
        if (fragment != null) {
            removeFragment(fragment);
        }
    }

    /**
     * 获取所有的fragment
     *
     * @return
     */
    public List<BaseFragment> getFragments() {
        List<BaseFragment> fragments = new ArrayList<>();
        List<Fragment> allFragments = coreFragmentManager.getFragments();
        if (!ListUtils.isEmpty(allFragments)) {
            for (Fragment ele : allFragments) {
                if (fragmentContainerId == ele.getId() && ele instanceof BaseFragment) {
                    fragments.add((BaseFragment) ele);
                }
            }
        }
        return fragments;
    }

    /**
     * 从hashMap中得到参数的json格式
     *
     * @param params 页面map形式参数
     * @return json格式参数
     */
    private String buildParams(Map<String, String> params) {
        if (params == null) {
            return "";
        }
        String result = JSON.toJSONString(params);
        Log.d(TAG, "params:" + result);
        return result;
    }

    private boolean contains(List<Fragment> fragments, Fragment fragment) {
        if (fragments != null) {
            for (Fragment f : fragments) {
                if (f == fragment) {
                    return true;
                }
            }
        }
        return false;
    }

    private FragmentTransaction hideAllFragment() {
        FragmentTransaction fragmentTransaction = coreFragmentManager.beginTransaction();
        List<BaseFragment> fragments = getFragments();
        for (BaseFragment fragment : fragments) {
            fragmentTransaction.hide(fragment);
            fragment.setHidden(true);
        }
        return fragmentTransaction;
    }

    private void showFragment(Fragment fragment) {
        coreFragmentManager.beginTransaction().show(fragment).commitAllowingStateLoss();
    }

    private void removeFragment(Fragment fragment) {
        coreFragmentManager.beginTransaction().remove(fragment).commitAllowingStateLoss();
        ((BaseFragment) fragment).setHidden(true);
    }

    private void replaceFragment(Class<? extends BaseFragment> fragmentClazz, Bundle data) {
        try {
            BaseFragment fragment = fragmentClazz.newInstance();
            if (data != null) {
                fragment.setArguments(data);
            }
            coreFragmentManager.beginTransaction().replace(fragmentContainerId, fragment).commitAllowingStateLoss();
            fragment.setHidden(false);
        } catch (Exception e) {
            Log.e(TAG, fragmentClazz.getName() + ", newInstance Error: " + e.getMessage());
        }
    }

    private boolean isSame(Fragment a, Fragment b) {
        return a.getClass().getName().equals(b.getClass().getName()) && a == b;
    }

    private FragmentTransaction hideOtherFragments(BaseFragment showFragment) {
        List<BaseFragment> fragments = getFragments();
        FragmentTransaction fragmentTransaction = coreFragmentManager.beginTransaction();
        for (BaseFragment fragment : fragments) {
            if (!isSame(fragment, showFragment) && !fragment.isHidden) {
                //其他fragment正在显示
                fragmentTransaction.hide(fragment);
                fragment.setHidden(true);
            }
        }
        return fragmentTransaction;
    }

    private void addFragment(Class<? extends BaseFragment> fragmentClazz, String fragmentTag, int[] fragmentAnims, boolean newStack, Bundle data) {
        try {
            BaseFragment fragment = fragmentClazz.newInstance();
            if (data != null) {
                fragment.setArguments(data);
            }
            FragmentTransaction fragmentTransaction = hideAllFragment();
            if (fragmentAnims != null) {
                fragmentTransaction.setCustomAnimations(fragmentAnims[0], fragmentAnims[1]);
            }
            if (newStack) {
                fragmentTransaction.add(fragmentContainerId, fragment, fragmentTag).addToBackStack(fragmentTag).commitAllowingStateLoss();
            } else {
                fragmentTransaction.add(fragmentContainerId, fragment, fragmentTag).commitAllowingStateLoss();
            }
            fragment.setHidden(false);
        } catch (Exception e) {
            Log.e(TAG, fragmentClazz.getName() + ", newInstance Error: " + e.getMessage());
        }
    }
}
