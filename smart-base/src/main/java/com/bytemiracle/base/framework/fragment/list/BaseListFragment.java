package com.bytemiracle.base.framework.fragment.list;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.fragment.BaseFragment;
import com.bytemiracle.base.framework.fragment.list.adapter.BaseListAdapter;
import com.bytemiracle.base.framework.fragment.list.adapter.holder.BaseListViewHolder;
import com.bytemiracle.base.framework.listener.CommonAsyncListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;

/**
 * 类功能：仅有滑动listview的基类fragment
 * 泛型H：ViewHolder
 * 泛型V：数据类型
 *
 * @author gwwang
 * @date 2021/3/22 17:07
 */
public abstract class BaseListFragment<H extends BaseListViewHolder<V>, V> extends BaseFragment {

    protected SwipeRecyclerView rvContent;
    protected BaseListAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_base_list;
    }

    @Override
    protected void initViews() {
        rvContent = mRootView.findViewById(R.id.tv_content);
        //设置布局管理器
        rvContent.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        //设置分割线
        int color = getContext().getColor(R.color.gray_2_divider);
        DefaultItemDecoration defaultItemDecoration = new DefaultItemDecoration(color);
        rvContent.addItemDecoration(defaultItemDecoration);
        //设置条目点击监听
        rvContent.setAdapter(adapter = new BaseListAdapter(initViewHolder(), getItemClickListener()));
    }

    protected abstract BaseListAdapter.InitViewHolderListener<H, V> initViewHolder();

    protected abstract CommonAsyncListener<V> getItemClickListener();
}
