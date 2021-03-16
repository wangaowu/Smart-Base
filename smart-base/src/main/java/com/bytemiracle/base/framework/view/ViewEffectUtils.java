package com.bytemiracle.base.framework.view;

import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * 类功能：view效果处理类
 *
 * @author gwwang
 * @date 2021/1/29 10:15
 */
public class ViewEffectUtils {
    private static final float VIEW_SCALE_RATIO = .95f;

    /**
     * 点击缩放
     *
     * @param view
     */
    public static void applyTouchScale(View view) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEvent.ACTION_DOWN == event.getAction()) {
                    scaleView(view, VIEW_SCALE_RATIO);
                    view.postDelayed(() -> {
                        scaleView(view, 1);
                    }, 100);
                }
                return false;
            }
        });
    }

    private static void scaleView(View view, float scaleRatio) {
        view.setScaleX(scaleRatio);
        view.setScaleY(scaleRatio);
    }

    private static void darkView(View view){
        view.setForeground(new ColorDrawable(0x30000000));
    }


}
