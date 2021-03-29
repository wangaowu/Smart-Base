package com.bytemiracle.base.framework.utils.timer;

import com.bytemiracle.base.framework.utils.XToastUtils;

/**
 * 类功能：增时器
 *
 * @author gwwang
 * @date 2021/3/25 11:34
 */
public class CountIncreaseUtil extends CountDownUtil {
    private static final int TOTAL = Integer.MAX_VALUE;
    private final CountDownUtil countDownUtil;
    private CountDownUtil.TickDelegate tickDelegate;

    /**
     * 获取 CountDownTimerUtils
     *
     * @return CountDownTimerUtils
     */
    public static CountIncreaseUtil newInstance() {
        return new CountIncreaseUtil();
    }

    /**
     * 设置定时器增加监听
     *
     * @param tickDelegate
     */
    public void setIncreaseTickDelegate(CountDownUtil.TickDelegate tickDelegate) {
        this.tickDelegate = tickDelegate;
    }

    private CountIncreaseUtil() {
        countDownUtil = CountDownUtil.newInstance()
                .setMillisInFuture(TOTAL)// 倒计时总时间
                // 每隔多久回调一次onTick
                .setCountDownInterval(CountDownUtil.ONE_SECOND)
                // 每回调一次onTick执行
                .setTickDelegate(new CountDownUtil.TickDelegate() {
                    @Override
                    public void onTick(long pMillisUntilFinished) {
                        tickDelegate.onTick(TOTAL - pMillisUntilFinished);
                    }
                })
                // 结束倒计时执行
                .setFinishDelegate(new CountDownUtil.FinishDelegate() {
                    @Override
                    public void onFinish() {
                        XToastUtils.info("倒计时完成!");
                    }
                });
    }

    public void cancel() {
        countDownUtil.cancel();
    }

    public void start() {
        countDownUtil.start();
    }
}
