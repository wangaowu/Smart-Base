package com.bytemiracle.base.framework.view.relationTree;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/2/9 15:50
 */
public class AnimationController {

    private final ValueAnimator valueAnimator;

    public AnimationController() {
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new DecelerateInterpolator());

    }

    List<View> views = new ArrayList();

    public AnimationController applyViews(View view) {
        views.add(view);
        return this;
    }

    public void startAlphaVisible(long duration) {
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(animation -> {
            Float animatedValue = (Float) animation.getAnimatedValue();
            for (View view : views) {
                view.post(() -> view.setAlpha(animatedValue));
            }
        });
        valueAnimator.start();
    }
}

