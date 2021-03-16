package com.bytemiracle.base.framework.view.volumeView;

import android.view.View;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * 类功能：声音波纹动画控制器
 *
 * @author gwwang
 * @date 2021/2/2 18:07
 */
public class VolumeViewController {
    private VolumeView volumeView;
    private Executor e = Executors.newSingleThreadExecutor();
    private boolean isRecording = false;

    public boolean isRecording() {
        return isRecording;
    }

    public VolumeViewController(VolumeView volumeView) {
        this.volumeView = volumeView;
        // 避免内存泄露
        volumeView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (isRecording && volumeView != null) {
                    stopAnimation();
                }
            }
        });
    }

    public void playAnimation() {
        isRecording = true;
        volumeView.start();
        e.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isRecording) {
                        volumeView.post(new Runnable() {
                            @Override
                            public void run() {
                                volumeView.setVolume(getRandom(0, 100));
                            }
                        });
                        Thread.sleep(10);
                    }
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
    }

    public void stopAnimation() {
        if (isRecording) {
            isRecording = false;
            volumeView.stop();
        }
    }


    private int getRandom(int min, int max) {
        Random random = new Random();
        int r = random.nextInt(max) % (max - min + 1) + min;
        return r;
    }


}
