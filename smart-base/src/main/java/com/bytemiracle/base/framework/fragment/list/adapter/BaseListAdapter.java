package com.bytemiracle.base.framework.fragment.list.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytemiracle.base.framework.fragment.list.adapter.holder.BaseListViewHolder;
import com.bytemiracle.base.framework.utils.common.ListUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 类功能：多类型消息条目适配器
 *
 * @author gwwang
 * @date 2021/2/4 14:21
 */
public class BaseListAdapter<T> extends RecyclerView.Adapter<BaseListViewHolder> {
    private static final String TAG = "BaseListAdapter";

    private InitViewHolderListener initViewHolderListener;
    private List<T> listData = new ArrayList<>();

    public interface InitViewHolderListener<V> {
        int getItemLayoutId();

        void bindViewHolder(BaseListViewHolder holder, int position, V t);
    }

    public BaseListAdapter(InitViewHolderListener initViewHolderListener) {
        this.initViewHolderListener = initViewHolderListener;
    }

    /**
     * 设置数据
     *
     * @param messageList
     */
    public void refresh(List<T> messageList) {
        this.listData.clear();
        this.listData.addAll(messageList);
        notifyDataSetChanged();
    }

    /**
     * 获取数据
     *
     * @return 当前适配器的数据
     */
    public List<T> getListData() {
        return listData;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseListViewHolder holder, int position) {
        initViewHolderListener.bindViewHolder(holder, position, listData.get(position));
    }

    @NonNull
    @Override
    public BaseListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(initViewHolderListener.getItemLayoutId(), parent, false);
        return new BaseListViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return ListUtils.isEmpty(listData) ? 0 : listData.size();
    }
}
