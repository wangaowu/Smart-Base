package com.bytemiracle.base.framework.utils.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageView;

/**
 * 类功能：bitmap处理类
 *
 * @author gwwang
 * @date 2021/1/12 11:03
 */
public class BitmapWrapper {

    private Bitmap srcBitmap;

    public BitmapWrapper(Bitmap srcBitmap) {
        this.srcBitmap = srcBitmap;
    }

    /**
     * 替换imageView的颜色
     *
     * @param attachView 控件
     * @param resId      资源id
     * @param newColor   新颜色
     */
    public static void quickApply(ImageView attachView, int resId, int newColor) {
        Context context = attachView.getContext();
        Bitmap srcBitmap = BitmapFactory.decodeResource(context.getResources(), resId);
        Bitmap newBitmap = new BitmapWrapper(srcBitmap).replaceColor(newColor);
        srcBitmap.recycle();
        attachView.setImageBitmap(newBitmap);
    }




    /**
     * 替换颜色
     *
     * @param newColor 新资源颜色
     * @return
     */
    public Bitmap replaceColor(int newColor) {
        int r = Color.red(newColor);
        int g = Color.green(newColor);
        int b = Color.blue(newColor);

        int width = srcBitmap.getWidth();
        int height = srcBitmap.getHeight();
        int[] pixels = new int[width * height];
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                int alpha = Color.alpha(srcBitmap.getPixel(row, col));
                if (alpha != 0) {
                    pixels[col * width + row] = Color.argb(alpha, r, g, b);
                } else {
                    pixels[col * width + row] = 0;
                }
            }
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
    }
}