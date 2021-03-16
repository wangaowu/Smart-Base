package com.bytemiracle.base.framework.http;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/3/12 14:53
 */
public abstract class OnReceiveResult<T> {
    public abstract void onSuccess(T resultBean);

    public abstract void onFailed(String reason);

    public void onJson(String json) {
    }
}
