package com.bytemiracle.base.framework.fragment.subfragment.config;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bytemiracle.base.R;

/**
 * 类功能：标题控制类
 *
 * @author gwwang
 * @date 2021/1/8 9:20
 */
public class SubTitle {
    private static final String TAG = "SubTitle";

    /**
     * 配置信息
     */
    private SubTitleConfig config;
    private Button btnRight;

    public SubTitle(SubTitleConfig config) {
        this.config = config;
    }

    /**
     * 获取标题内右侧按钮
     *
     * @return
     */
    public View getRightButton() {
        return btnRight;
    }

    /**
     * 创建view
     *
     * @param context 上下文
     * @return
     */
    public View createSettingsSubTitle(Context context) {
        View subTitleView = View.inflate(context, R.layout.layout_settings_sub_title, null);
        init(subTitleView);
        return subTitleView;
    }

    private void init(View contentView) {
        TextView tvSubTitle = contentView.findViewById(R.id.tv_sub_title);
        ViewGroup flRight = contentView.findViewById(R.id.fl_right);
        btnRight = contentView.findViewById(R.id.btn_right);
        ImageView ivRight = contentView.findViewById(R.id.iv_right);
        if (config == null) {
            Log.d(TAG, "subConfig == null , please check!!");
            return;
        }
        tvSubTitle.setText(config.titleText);
        switch (config.rightConfig.viewType) {
            case EMPTY:
                flRight.setVisibility(View.GONE);
                break;
            case BUTTON:
                flRight.setVisibility(View.VISIBLE);
                btnRight.setVisibility(View.VISIBLE);
                ivRight.setVisibility(View.GONE);
                btnRight.setText(config.rightConfig.buttonText);
                break;
            case ICON:
                flRight.setVisibility(View.VISIBLE);
                btnRight.setVisibility(View.GONE);
                ivRight.setVisibility(View.VISIBLE);
                ivRight.setImageResource(config.rightConfig.imageResId);
                break;
        }
    }
}
