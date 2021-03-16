package com.bytemiracle.base.framework.view.apptitle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.view.ShadowLinearLayout;

/**
 * 类功能：应用的title
 *
 * @author gwwang
 * @date 2021/1/29 15:12
 */
public class AppTitleController {

    private Context context;
    private int heightPixel;
    public ImageButton ivBack;
    public TextView tvTitle;
    public Button btn;
    private View contentView;

    public AppTitleController(Context context) {
        this(context, context.getResources().getDimensionPixelSize(R.dimen.dpx_45));
    }

    public AppTitleController(Context context, int heightPixel) {
        this.context = context;
        this.heightPixel = heightPixel;
        initViews();
    }

    private void initViews() {
        contentView = View.inflate(context, R.layout.layout_app_title, null);
        ivBack = contentView.findViewById(R.id.iv_back);
        tvTitle = contentView.findViewById(R.id.tv_title);
        btn = contentView.findViewById(R.id.btn);
    }

    public AppTitleController insert2Parent(ViewGroup viewGroup, int index) {
        if (viewGroup instanceof LinearLayout && ((LinearLayout) viewGroup).getOrientation() == LinearLayout.VERTICAL) {
            //竖向的线性布局
            viewGroup.addView(contentView, index, new LinearLayout.LayoutParams(-1, heightPixel));
        }
        return this;
    }

    /**
     * 因为阴影已有架构已不满足，所以需要这个来做
     */
    public void wrapShadowEffect(View linearLayout, int shadowVerticalPosition) {
        linearLayout.setBackgroundColor(context.getColor(R.color.app_common_bg_white));
        if (linearLayout instanceof ShadowLinearLayout) {
            ShadowLinearLayout ll = (ShadowLinearLayout) linearLayout;
            ll.setShadowOn(R.drawable.bg_title_transparent_shadow, shadowVerticalPosition);
        }
    }
}
