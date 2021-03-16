package com.bytemiracle.base.framework.listener;

/**
 * 类功能：通用的回调类 两个方法
 *
 * @author gwwang
 * @date 2021/1/8 15:00
 */
public interface CommonAsync2Listener<T> {
    void doSomething1(T data);

    void doSomething2(T data);
}
