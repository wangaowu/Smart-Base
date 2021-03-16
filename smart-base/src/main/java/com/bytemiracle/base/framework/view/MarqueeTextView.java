package com.bytemiracle.base.framework.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 类功能：跑马灯textView，usage：
 * <com.example.MarqueeText
 *     android:id="@+id/AMTV1"
 *     android:layout_width="400dip"
 *     android:layout_height="wrap_content"
 *     android:layout_marginLeft="80dip"
 *     android:textSize="25sp"
 *     android:textColor="@android:color/black"
 *     android:lines="1"
 *     android:focusable="true"
 *     android:focusableInTouchMode="true"
 *     android:scrollHorizontally="true"
 *     android:marqueeRepeatLimit="marquee_forever"
 *     android:ellipsize="marquee"
 *     android:background="#2FFFFFFF"
 *     android:text="这才是真正的文字跑马灯效果,文字移动速度，文字移动方向，文字移动的样式，动画等等……"
 *     />
 *
 * @author gwwang
 * @date 2021/2/1 14:26
 */
public class MarqueeTextView extends TextView {
    public MarqueeTextView(Context con) {
        super(con);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {

    }
}