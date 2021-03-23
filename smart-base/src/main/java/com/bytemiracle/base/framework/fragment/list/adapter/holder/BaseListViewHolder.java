package com.bytemiracle.base.framework.fragment.list.adapter.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * 类功能：list条目基类
 *
 * @author gwwang
 * @date 2021/2/4 15:23
 */
public abstract class BaseListViewHolder<T> extends RecyclerView.ViewHolder {
    protected Context context;

    public BaseListViewHolder(@NonNull View itemView) {
        super(itemView);
        this.context = itemView.getContext();
    }

    public abstract void bindItemData(T itemData);

    /**
     * 寻找控件
     *
     * @param id
     * @return
     */
    public <T extends View> T findView(@IdRes int id) {
        return id == 0 ? (T) itemView : itemView.findViewById(id);
    }

    /**
     * 设置文字
     *
     * @param id
     * @param sequence
     * @return
     */
    public void text(int id, CharSequence sequence) {
        TextView view = findView(id);
        view.setText(sequence);
    }

    /**
     * 设置文字
     *
     * @param id
     * @param stringRes
     * @return
     */
    public void text(@IdRes int id, @StringRes int stringRes) {
        TextView view = findView(id);
        view.setText(stringRes);
    }

    /**
     * 设置文字的颜色
     *
     * @param id
     * @param colorId
     * @return
     */
    public void textColorId(@IdRes int id, @ColorRes int colorId) {
        TextView view = findView(id);
        view.setTextColor(ContextCompat.getColor(view.getContext(), colorId));
    }

    /**
     * 设置图片
     *
     * @param id
     * @param imageId
     * @return
     */
    public void image(@IdRes int id, int imageId) {
        ImageView view = findView(id);
        view.setImageResource(imageId);
    }

    /**
     * 设置图片
     *
     * @param id
     * @param remoteUrl
     * @return
     */
    public void image(@IdRes int id, String remoteUrl) {
        ImageView view = findView(id);
        RequestOptions requestOptions = new RequestOptions();
        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(remoteUrl).into(view);
    }

    /**
     * 设置控件的点击监听
     *
     * @param id
     * @param listener
     * @return
     */
    public void click(@IdRes int id, final View.OnClickListener listener) {
        View view = findView(id);
        if (listener != null) {
            view.setOnClickListener(listener);
        }
    }

    /**
     * 设置控件是否可显示
     *
     * @param id
     * @param visibility
     * @return
     */
    public void visible(@IdRes int id, int visibility) {
        View view = findView(id);
        view.setVisibility(visibility);
    }


    /**
     * 设置背景
     *
     * @param viewId
     * @param resId
     * @return
     */
    public void backgroundResId(int viewId, int resId) {
        View view = findView(viewId);
        view.setBackgroundResource(resId);
    }
}
