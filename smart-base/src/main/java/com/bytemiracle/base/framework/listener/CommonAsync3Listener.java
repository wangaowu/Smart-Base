package com.bytemiracle.base.framework.listener;

/**
 * 类功能：通用的回调类 两个参数
 *
 * @author gwwang
 * @date 2021/1/8 15:00
 */
public interface CommonAsync3Listener<T, V> {
    void doSomething(T data1, V data2);
}
