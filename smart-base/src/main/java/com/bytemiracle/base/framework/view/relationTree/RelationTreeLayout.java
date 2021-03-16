package com.bytemiracle.base.framework.view.relationTree;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bytemiracle.base.R;
import com.bytemiracle.base.framework.listener.CommonAsyncListener;

import java.util.ArrayList;

/**
 * 类功能：关系图
 *
 * @author gwwang
 * @date 2021/2/6 11:43
 */
public class RelationTreeLayout extends ViewGroup {
    private static final String TAG = "RelationTreeLayout";
    private static final float HOR_MARGIN_RATIO = .6f;
    private static final float VER_MARGIN_RATIO = .3f;
    //cell颜色列表（每一列使用同一个颜色）
    public static final int[] CELL_COLORS = new int[]{0xff000bd3, 0xffdc8701, 0xff8cbe00, 0xff09b880, 0xff008bf6, 0xff007947};
    //cell横向间距
    public static int horizontal_margin = 200;
    //cell竖向间距
    public static int vertical_margin = 100;
    private static final int POLYGON_RADIUS = 50;
    //最大最小缩放值
    public static final float MAX_SCALE_RATIO = 1.0f;
    private static final float MIN_SCALE_RATIO = 0.5f;

    //cell/canvas控制器
    private CellController cellController;
    private CommonAsyncListener scrollListener;
    private CanvasController canvasController;

    //控件高度
    private int viewHeight;
    //当前布局的xy
    int layoutX = 0, layoutY = 0;

    /**
     * 数据
     */
    private CellRect rootCell;
    public CommonAsyncListener onClickCellListener;

    private ScaleGestureDetector mScaleDetector;
    private float gestureScaleRatio = MIN_SCALE_RATIO;

    public RelationTreeLayout(Context context) {
        this(context, null);
    }

    public RelationTreeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RelationTreeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutX = 0;
        layoutY = 0;
        viewHeight = getMeasuredHeight();
        recursiveLayoutCell(rootCell, RecursiveDirection.RIGHT);
        cellController.recursiveUpdateCollapseClipRect(rootCell);
        cellController.recursiveVisibleCellByCollapseState(rootCell);
        cellController.recursiveResizeCellByScale(rootCell, childInitRect, childInitTextSize, gestureScaleRatio);
    }

    public void setHighLightCell(String highLightCellText) {
        CellRect highLightCell = cellController.findCellByText(rootCell, highLightCellText);
        if (highLightCell != null) {
            resetCanvas();
            CellRect.makeHighLight(highLightCell);
            cellController.recursiveCollapseChildren(rootCell);
            cellController.openParentCollapse(highLightCell);
            gestureScaleRatio = MIN_SCALE_RATIO;
            scrollListener.doSomething(null);
        }
    }

    private void recursiveLayoutCell(CellRect cell, RecursiveDirection rDirection) {
        if (cell == null || rDirection == RecursiveDirection.OVER) {
            //节点
            return;
        }
        int childWidth = cell.bindView.getMeasuredWidth();
        int childHeight = cell.bindView.getMeasuredHeight();
        if (cellController.isRootNode(cell)) {
            //根node
            cell.bindView.layout(layoutX, viewHeight / 2, layoutX + childWidth, viewHeight / 2 + childHeight);
        } else if (cell.bindView.getVisibility() == VISIBLE) {
            //非根node
            updateXY(rDirection, cell);
            cell.bindView.layout(layoutX, layoutY, layoutX + childWidth, layoutY + childHeight);
        }
        cell.setTraversed(true);
        Pair<CellRect, RecursiveDirection> nextCell = cellController.getNextCell(cell);
        recursiveLayoutCell(nextCell.first, nextCell.second);
    }

    private int fixChildrenVerticalOffset(CellRect cell) {
        CellRect parent = cell.parent;
        if (parent == null) {
            return 0;
        }
        int childrenTotalHeight = cellController.getChildrenTotalHeight(parent, vertical_margin);
        float parentRCY = parent.bindView.getY() + parent.bindView.getMeasuredHeight() * .5f;
        //非移动画布的自动偏移
//        int viewHeight = getMeasuredHeight();
//        if (childrenTotalHeight > viewHeight) {
//            //子节点总高度大于屏幕高度
//            return 0;
//        }
//        if (parentRCY <= viewHeight * .5f) {
//            //父节点处于屏幕中上
//            if (childrenTotalHeight * .5f < parentRCY) {
//                //可以放下以父节点在子节点中央的情况
//                return (int) (parentRCY - childrenTotalHeight * .5f);
//            } else {
//                //放不下
//                return (int) parent.bindView.getY();
//            }
//        } else {
//            //父节点处于屏幕中下
//            if (childrenTotalHeight * .5f < viewHeight - parentRCY) {
//                //可以放下以父节点在子节点中央的情况
//                return (int) (parentRCY - childrenTotalHeight * .5f);
//            } else {
//                //放不下
//                return (int) (parentRCY + parent.bindView.getMeasuredHeight() / 2 - childrenTotalHeight);
//            }
//        }
        //移动画布的偏移
        return (int) (parentRCY - childrenTotalHeight * .5f);
    }

    private void updateXY(RecursiveDirection recursiveDirection, CellRect cell) {
        switch (recursiveDirection) {
            case RIGHT:
                layoutX = layoutX + cell.bindView.getMeasuredWidth() + horizontal_margin;
                layoutY = fixChildrenVerticalOffset(cell);
                break;
            case BOTTOM:
                layoutY = layoutY + cell.bindView.getMeasuredHeight() + vertical_margin;
                break;
            case LEFT_BOTTOM:
                CellRect topCell = cellController.findTopCell(cell);
                layoutX = (int) topCell.bindView.getX();
                layoutY = (int) topCell.bindView.getY() + topCell.bindView.getMeasuredHeight() + vertical_margin;
                break;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        canvasController = new CanvasController(canvas, getResources().getColor(R.color.relation_tree_line_color));
        link2Children(this.rootCell);
    }

    private void link2Children(CellRect cell) {
        if (cell.hasChild()) {
            if (!cell.isCollapse()) {
                for (CellRect childCell : cell.childCells) {
                    canvasController.drawPolygonLine(cell, childCell, cellController);
                    link2Children(childCell);
                }
                canvasController.drawCollapseOpenIcon(cell, cellController);
            } else {
                canvasController.drawCollapseCloseIcon(cell, cellController);
            }
        }
    }


    private double calcSqrt(float x, float y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            gestureScaleRatio *= detector.getScaleFactor();
            gestureScaleRatio = Math.max(MIN_SCALE_RATIO, Math.min(gestureScaleRatio, MAX_SCALE_RATIO));
            //重新调用onMeasure布局
            requestLayout();
            return true;
        }
    }

    private float clickX;
    private float clickY;
    private float moveDistanceTwice;
    private boolean isMultiPointMode = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleDetector.onTouchEvent(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                isMultiPointMode = false;
                moveDistanceTwice = 0;
                clickX = event.getX();
                clickY = event.getY();
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                isMultiPointMode = true;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getPointerCount() > 1 || !isMultiPointMode) {
                    //多指的时候响应move事件，优化缩放不随中点
                    scrollBy(-(int) (event.getX() - clickX), -(int) (event.getY() - clickY));
                    moveDistanceTwice += calcSqrt(event.getX() - clickX, event.getY() - clickY);
                    if (moveDistanceTwice > MotionEvent.AXIS_DISTANCE && scrollListener != null) {
                        scrollListener.doSomething(null);
                    }
                    clickX = event.getX();
                    clickY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isMultiPointMode) {
                    if (moveDistanceTwice < MotionEvent.AXIS_DISTANCE) {
                        //判定手指没有移动，再响应点击事件
                        //1.响应折叠
                        CellRect bindCell = cellController.findAttachCell(event.getX() + getScrollX(), event.getY() + getScrollY());
                        if (bindCell != null) {
                            cellController.collapseCell(bindCell, this);
                            return false;
                        }
                        //2.响应cell点击
                        CellRect clickCell = cellController.findClickCell(event.getX() + getScrollX(), event.getY() + getScrollY());
                        if (clickCell != null && onClickCellListener != null) {
                            onClickCellListener.doSomething(clickCell.bindData);
                            return false;
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private RectF childInitRect;
    private float childInitTextSize;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置初始的子view大小(不随手势)
        cellController.reTraversal(rootCell);
        if (childInitRect == null && getChildCount() > 0) {
            View rootView = getChildAt(0);
            measureChild(rootView, widthMeasureSpec, heightMeasureSpec);
            childInitRect = new RectF(0, 0, rootView.getMeasuredWidth(), rootView.getMeasuredHeight());
            childInitTextSize = ((TextView) rootView.findViewById(R.id.cell_content)).getTextSize();
        }
        //设置初始margin（随手势）
        horizontal_margin = (int) (HOR_MARGIN_RATIO * gestureScaleRatio * childInitRect.width());
        vertical_margin = (int) (VER_MARGIN_RATIO * gestureScaleRatio * childInitRect.height());
        //测量子view
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            int wM = MeasureSpec.makeMeasureSpec((int) (childInitRect.width() * gestureScaleRatio), MeasureSpec.EXACTLY);
            int hM = MeasureSpec.makeMeasureSpec((int) (childInitRect.height() * gestureScaleRatio), MeasureSpec.EXACTLY);
            measureChild(childView, wM, hM);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 重置滚动位置
     */
    public void resetCanvas() {
        gestureScaleRatio = MIN_SCALE_RATIO;
        cellController.recursiveClearCellHighLight(rootCell);
        setScrollX(0);
        setScrollY(0);
        requestLayout();
    }

    /**
     * 设置树状结构图的cell数据
     *
     * @param rootCell            根节点cell数据
     * @param onClickCellListener cell的点击事件
     */
    public void setCellData(CellRect rootCell, CommonAsyncListener onClickCellListener, CommonAsyncListener scrollListener) {
        this.rootCell = rootCell;
        this.onClickCellListener = onClickCellListener;
        this.cellController = new CellController(rootCell);
        this.scrollListener = scrollListener;
        removeAllViews();

        ArrayList<CellRect> recursiveArrays = new ArrayList<>();
        recursiveArrays.add(this.rootCell);
        cellController.recursiveBindCellViews(this.rootCell, recursiveArrays, this);
        requestLayout();
    }

}
