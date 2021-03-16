package com.bytemiracle.base.framework.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bytemiracle.base.R;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/1/29 18:00
 */
public class ShadowLinearLayout extends LinearLayout {

    private int verticalPosition;
    private Bitmap shadowBitmap;

    public ShadowLinearLayout(Context context) {
        this(context, null);
    }

    public ShadowLinearLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShadowLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initDetachWidowRecycle();
    }

    /**
     * 设置阴影
     *
     * @param shadowResId      阴影id
     * @param verticalPosition 竖向y位置
     */
    public void setShadowOn(int shadowResId, int verticalPosition) {
        this.shadowBitmap = BitmapFactory.decodeResource(getResources(), shadowResId);
        this.verticalPosition = verticalPosition;
        postInvalidate();
    }

    /**
     * 绘制阴影
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (shadowBitmap != null && !shadowBitmap.isRecycled()) {
            int shadowHeight = getResources().getDimensionPixelSize(R.dimen.dpx_shadow_height);
            Rect srcRect = new Rect(0, 0, shadowBitmap.getWidth(), shadowBitmap.getHeight());
            Rect distRect = new Rect(0, verticalPosition, getWidth(), verticalPosition + shadowHeight);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            canvas.drawBitmap(shadowBitmap, srcRect, distRect, paint);
        }
    }

    private void initDetachWidowRecycle() {
        addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (shadowBitmap != null && !shadowBitmap.isRecycled()) {
                    shadowBitmap.recycle();
                }
            }
        });
    }
}
