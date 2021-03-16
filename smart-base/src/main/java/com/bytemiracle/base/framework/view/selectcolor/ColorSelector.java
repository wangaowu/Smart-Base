package com.bytemiracle.base.framework.view.selectcolor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.bytemiracle.base.framework.view.BaseCheckPojo;

import java.util.List;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/1/18 14:01
 */
public class ColorSelector extends LinearLayout {
    private List<ColorItemData> colorItemDataList;
    private OnColorSelectedListener listener;

    public ColorSelector(Context context) {
        this(context, null);
    }

    public ColorSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(List<ColorItemData> colorItemDataList, OnColorSelectedListener listener) {
        this.colorItemDataList = colorItemDataList;
        this.listener = listener;
        for (int i = 0; i < colorItemDataList.size(); i++) {
            ColorItemData colorItemData = colorItemDataList.get(i);
            ((ColorItem) getChildAt(i)).initColor(colorItemData.colorValue).listener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectItem(v, colorItemData);
                    if (listener != null) {
                        listener.onSelected(colorItemData);
                    }
                }
            }).setSelected(colorItemData.isChecked());
        }
    }

    public List<ColorItemData> getColorItemDataList() {
        return colorItemDataList;
    }

    private void selectItem(View v, ColorItemData colorItemData) {
        for (int i = 0; i < getChildCount(); i++) {
            ColorItem child = (ColorItem) getChildAt(i);
            child.setSelected(child == v);
            colorItemData.setChecked(child == v);
        }
    }

    public interface OnColorSelectedListener {
        void onSelected(ColorItemData colorItemData);
    }

    public static class ColorItemData extends BaseCheckPojo {
        public int colorValue;
        public ColorItemData(int colorValue) {
            this.colorValue = colorValue;
        }
    }


}
