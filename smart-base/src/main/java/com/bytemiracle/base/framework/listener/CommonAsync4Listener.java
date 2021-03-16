package com.bytemiracle.base.framework.listener;

/**
 * 类功能：通用的回调类 1个方法
 *
 * @author gwwang
 * @date 2021/1/8 15:00
 */
public interface CommonAsync4Listener<T> {
    /**
     * 获取数据接口
     *
     * @return
     */
    T getInitData();
}
