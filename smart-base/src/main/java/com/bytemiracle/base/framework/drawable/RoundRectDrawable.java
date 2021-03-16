package com.bytemiracle.base.framework.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Very simple drawable that draws a rounded rectangle background with arbitrary corners and also
 * reports proper outline for L.
 * <p>
 * Simpler and uses less resources compared to GradientDrawable or ShapeDrawable.
 */
public class RoundRectDrawable extends Drawable {
    private int outBackgroundColor;
    private int innerBackgroundColor;
    private int strokeWidth;
    float mRadius;
    final Paint mPaint;
    final RectF mBounds;

    public RoundRectDrawable(int outBackgroundColor, float radius) {
        this(outBackgroundColor, outBackgroundColor, 0, radius);
    }

    public RoundRectDrawable(int outBackgroundColor, int innerBackgroundColor, int strokeWidth, float radius) {
        this.outBackgroundColor = outBackgroundColor;
        this.innerBackgroundColor = innerBackgroundColor;
        this.strokeWidth = strokeWidth;
        this.mRadius = radius;
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        this.mBounds = new RectF();
    }

    @Override
    public void draw(Canvas canvas) {
        mPaint.setColor(outBackgroundColor);
        canvas.drawRoundRect(mBounds, mRadius, mRadius, mPaint);

        mPaint.setColor(innerBackgroundColor);
        RectF innerBounds = new RectF(strokeWidth, strokeWidth, mBounds.right - strokeWidth, mBounds.bottom - strokeWidth);
        canvas.drawRoundRect(innerBounds, mRadius, mRadius, mPaint);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mBounds.set(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    @Override
    public void getOutline(Outline outline) {
        outline.setRoundRect(getBounds(), mRadius);
    }

    public void setRadius(float radius) {
        if (radius == mRadius) {
            return;
        }
        mRadius = radius;
        invalidateSelf();
    }

    @Override
    public void setAlpha(int alpha) {
        // not supported because older versions do not support
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // not supported because older versions do not support
    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    public float getRadius() {
        return mRadius;
    }
}