package com.bytemiracle.base.framework.view.recyclerTools;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 类功能：recyclerView功能类
 *
 * @author gwwang
 * @date 2021/1/25 9:47
 */
public class RecyclerViewWrapper {
    /**
     * 目标项是否在最后一个可见项之后
     */
    private boolean mShouldScroll;
    /**
     * 记录目标项位置
     */
    private int mToPosition;
    private RecyclerView mRecyclerView;

    public RecyclerViewWrapper(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
    }

    /**
     * 滑动到末尾
     */
    public void smoothMoveToLastIndex() {
        int lastIndex = mRecyclerView.getAdapter().getItemCount() - 1;
        smoothMoveToPosition(lastIndex);
    }

    /**
     * 滑动到指定位置
     *
     * @param position
     */
    public void smoothMoveToPosition(final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));

        if (position < firstItem) {
            // 如果跳转位置在第一个可见位置之前，就smoothScrollToPosition可以直接跳转
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 跳转位置在第一个可见项之后，最后一个可见项之前
            // smoothScrollToPosition根本不会动，此时调用smoothScrollBy来滑动到指定位置
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 如果要跳转的位置在最后可见项之后，则先调用smoothScrollToPosition将要跳转的位置滚动到可见位置
            // 再通过onScrollStateChanged控制再次调用smoothMoveToPosition，执行上一个判断中的方法
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
    }

}
