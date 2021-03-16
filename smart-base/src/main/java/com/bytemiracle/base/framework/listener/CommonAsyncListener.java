package com.bytemiracle.base.framework.listener;

/**
 * 类功能：通用的回调类
 *
 * @author gwwang
 * @date 2021/1/8 15:00
 */
public interface CommonAsyncListener<T> {
    void doSomething(T data);
}
