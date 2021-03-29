package com.bytemiracle.base.framework.view.tabLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.fragment.BaseFragment;
import com.bytemiracle.base.framework.utils.common.ListUtils;
import com.bytemiracle.base.framework.utils.image.BitmapWrapper;
import com.bytemiracle.base.framework.view.BaseCheckPojo;

import java.util.List;

/**
 * 类功能：底部tab栏
 *
 * @author gwwang
 * @date 2021/3/17 13:26
 */
public class BottomTabLayout extends LinearLayout {
    /**
     * 选中的颜色
     */
    private int FOCUS_COLOR_VALUE = 0;
    /**
     * 未选中的颜色
     */
    private int NORMAL_COLOR_VALUE = 0;


    public interface OnTabCheckChangedListener {
        void onTabChecked(Pojo pojo);
    }

    public BottomTabLayout(Context context) {
        this(context, null);
    }

    public BottomTabLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(LinearLayout.HORIZONTAL);
        setBackgroundColor(getResources().getColor(R.color.app_common_light_gray_2));
        FOCUS_COLOR_VALUE = getResources().getColor(R.color.app_common_bg_blue);
        NORMAL_COLOR_VALUE = getResources().getColor(R.color.app_common_content_text_light_gray_color);
    }

    public void setTabs(List<Pojo> tabs, OnTabCheckChangedListener onTabCheckedListener) {
        if (!ListUtils.isEmpty(tabs)) {
            LayoutParams layoutParams = new LayoutParams(0, -1);
            layoutParams.weight = 1;

            for (Pojo tab : tabs) {
                View cell = View.inflate(getContext(), R.layout.bottom_cell_layout, null);
                TextView textView = cell.findViewById(R.id.tv_content);
                ImageView iv = cell.findViewById(R.id.iv);
                cell.setOnClickListener(v -> {
                    checkCell(cell, tab, tabs, onTabCheckedListener);
                });
                textView.setText(tab.text);
                iv.setImageResource(tab.bottomDrawableResId);
                addView(cell, layoutParams);
            }
        }
        //默认选中第一个
        checkCell(getChildAt(0), tabs.get(0), tabs, onTabCheckedListener);
    }

    private void checkCell(View cell, Pojo tab, List<Pojo> tabs, OnTabCheckChangedListener onTabClickListener) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            TextView textView = child.findViewById(R.id.tv_content);
            ImageView iv = child.findViewById(R.id.iv);
            if (cell == child) {
                textView.setTextColor(FOCUS_COLOR_VALUE);
                BitmapWrapper.quickApply(iv, tabs.get(i).bottomDrawableResId, FOCUS_COLOR_VALUE);
            } else {
                textView.setTextColor(NORMAL_COLOR_VALUE);
                BitmapWrapper.quickApply(iv, tabs.get(i).bottomDrawableResId, NORMAL_COLOR_VALUE);
            }
        }
        onTabClickListener.onTabChecked(tab);
    }


    /**
     * 数据实体
     */
    public static class Pojo extends BaseCheckPojo {
        public int bottomDrawableResId;
        public String text;
        private Class<? extends BaseFragment> fragmentClazz;
        private Object bundle;

        public Pojo(int bottomDrawableResId, String text, Class<? extends BaseFragment> fragmentClazz) {
            this.bottomDrawableResId = bottomDrawableResId;
            this.text = text;
            setFragment(fragmentClazz);
        }

        public Class<? extends BaseFragment> getFragment() {
            return fragmentClazz;
        }

        public void setFragment(Class<? extends BaseFragment> fragmentClazz) {
            this.fragmentClazz = fragmentClazz;
        }

        public Object getBundle() {
            return bundle;
        }

        public void setBundle(Object bundle) {
            this.bundle = bundle;
        }
    }
}
