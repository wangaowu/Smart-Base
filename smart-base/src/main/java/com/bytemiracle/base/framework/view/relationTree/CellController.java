package com.bytemiracle.base.framework.view.relationTree;

import android.graphics.RectF;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bytemiracle.base.R;

import java.util.List;

/**
 * 类功能：TODO
 *
 * @author gwwang
 * @date 2021/2/8 11:44
 */
public class CellController {
    private static final String TAG = "CellController";

    private CellRect rootCell;

    public CellController(CellRect rootCell) {
        this.rootCell = rootCell;
    }

    public int getChildrenTotalHeight(CellRect parentCellRect, int childrenMargin) {
        int childrenSize = parentCellRect.childCells.size();
        return childrenSize * parentCellRect.bindView.getMeasuredHeight() + (childrenSize - 1) * childrenMargin;
    }

    public int getCenterY(CellRect cell) {
        return (int) (cell.bindView.getY() + cell.bindView.getMeasuredHeight() / 2);
    }

    public int getRightX(CellRect cell) {
        return (int) (cell.bindView.getX() + cell.bindView.getMeasuredWidth());
    }

    public void recursiveUpdateCollapseClipRect(CellRect cell) {
        if (cell.hasChild()) {
            cell.setCollapseClipRect(buildClipCollapseRect(cell));
            for (CellRect childCell : cell.childCells) {
                recursiveUpdateCollapseClipRect(childCell);
            }
        }
    }

    public void recursiveFindCellByText(CellRect cell, String highLightCellText, CellRect[] result) {
        if (cell.contentText.equals(highLightCellText)) {
            result[0] = cell;
        }
        if (cell.hasChild()) {
            for (CellRect childCell : cell.childCells) {
                recursiveFindCellByText(childCell, highLightCellText, result);
            }
        }
    }

    public CellRect findCellByText(CellRect cell, String highLightCellText) {
        CellRect[] result = new CellRect[1];
        recursiveFindCellByText(cell, highLightCellText, result);
        return result[0];
    }

    public void recursiveVisibleCellByCollapseState(CellRect cell) {
        visibleChildren(cell, cell.isCollapse() ? View.GONE : View.VISIBLE);
        if (cell.hasChild()) {
            for (CellRect childCell : cell.childCells) {
                recursiveVisibleCellByCollapseState(childCell);
            }
        }
    }

    public void reTraversal(CellRect cell) {
        cell.setTraversed(false);
        if (cell.hasChild()) {
            for (CellRect childCell : cell.childCells) {
                reTraversal(childCell);
            }
        }
    }

    private RectF buildClipCollapseRect(CellRect parentCell) {
        int centerY = getCenterY(parentCell);
        int rightX = getRightX(parentCell);
        int centerX = rightX + RelationTreeLayout.horizontal_margin / 4;
        int iconLeft = centerX - 2 * CanvasController.COLLAPSE_ICON_RADIUS;
        int iconTop = centerY - 2 * CanvasController.COLLAPSE_ICON_RADIUS;
        int iconRight = centerX + 2 * CanvasController.COLLAPSE_ICON_RADIUS;
        int iconBottom = centerY + 2 * CanvasController.COLLAPSE_ICON_RADIUS;
        return new RectF(iconLeft, iconTop, iconRight, iconBottom);
    }

    public void collapseCell(CellRect cell, RelationTreeLayout thisView) {
        if (cell.isCollapse()) {
            collapseOtherCells(cell);
            cell.setCollapse(false);
        } else {
            recursiveCollapseChildren(cell);
        }
        thisView.requestLayout();
    }

    public void recursiveResizeCellByScale(CellRect cell, RectF childInitRect, float childInitTextSize, float gestureScaleRatio) {
        TextView textContent = (TextView) cell.bindView.findViewById(R.id.cell_content);
        //字体大小
        textContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, childInitTextSize * gestureScaleRatio);
        //控件宽高
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textContent.getLayoutParams();
        layoutParams.width = (int) (childInitRect.width() * gestureScaleRatio);
        layoutParams.height = (int) (childInitRect.height() * gestureScaleRatio);
        int margin = (int) (cell.bindView.getContext().getResources().getDimension(R.dimen.dpx_10) * gestureScaleRatio);
        layoutParams.setMargins(margin, 0, margin, 0);
        if (cell.hasChild()) {
            for (CellRect childCell : cell.childCells) {
                recursiveResizeCellByScale(childCell, childInitRect, childInitTextSize, gestureScaleRatio);
            }
        }
    }

    public void recursiveCollapseChildren(CellRect cell) {
        cell.setCollapse(true);
        if (cell.hasChild()) {
            for (CellRect childCell : cell.childCells) {
                recursiveCollapseChildren(childCell);
            }
        }
    }

    public void recursiveClearCellHighLight(CellRect cell) {
        CellRect.clearHighLight(cell);
        if (cell.hasChild()) {
            for (CellRect childCell : cell.childCells) {
                recursiveClearCellHighLight(childCell);
            }
        }
    }

    private void collapseOtherCells(CellRect cell) {
        CellRect parent = cell.parent;
        if (parent != null) {
            for (CellRect childCell : parent.childCells) {
                if (childCell != cell) {
                    recursiveCollapseChildren(childCell);
                }
            }
        }
    }

    public void recursiveBindCellViews(CellRect parent, List<CellRect> cells, RelationTreeLayout viewGroup) {
        if (cells != null) {
            for (CellRect cell : cells) {
                bindCellView(cell, parent, viewGroup);
                recursiveBindCellViews(cell, cell.childCells, viewGroup);
            }
        }
    }

    public void bindCellView(CellRect cell, CellRect parent, RelationTreeLayout viewGroup) {
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -2);
        cell.bindView = View.inflate(viewGroup.getContext(), cell.layoutResId, null);
        cell.bindView.setBackground(cell.backgroundDrawable);
        TextView tv = cell.bindView.findViewById(R.id.cell_content);
        tv.setText(cell.contentText);
        cell.parent = isRootNode(cell) ? null : parent;
        viewGroup.addView(cell.bindView, layoutParams);
    }

    /**
     * 获取下一个cell
     * 顺序：从右 ---下  ---"左" ---左下
     *
     * @param cell
     * @return
     */
    public Pair<CellRect, RecursiveDirection> getNextCell(CellRect cell) {
        CellRect rightCell = findRightCell(0, cell.colIndex + 1, cell.childCells);
        if (rightCell != null && !rightCell.isTraversed()) {
            return new Pair<>(rightCell, RecursiveDirection.RIGHT);
        }
        CellRect bottomCell = findBottomCell(cell);
        if (bottomCell != null && !bottomCell.isTraversed()) {
            return new Pair<>(bottomCell, RecursiveDirection.BOTTOM);
        }
        CellRect leftBottomCell = findLeftBottomCell(cell);
        if (leftBottomCell != null) {
            return !leftBottomCell.isTraversed() ? new Pair<>(leftBottomCell, RecursiveDirection.LEFT_BOTTOM) : getNextCell(leftBottomCell);
        }
        return new Pair<>(rootCell, RecursiveDirection.OVER);
    }

    public CellRect findTopCell(CellRect cell) {
        int topColIndex = cell.colIndex;
        int topColRowIndex = cell.rowIndex - 1;
        //左侧根节点
        if (null != cell.parent && cell.parent.hasChild()) {
            List<CellRect> currentColCells = cell.parent.childCells;
            if (currentColCells != null) {
                for (CellRect ele : currentColCells) {
                    if (ele.colIndex == topColIndex && ele.rowIndex == topColRowIndex) {
                        return ele;
                    }
                }
            }
        }
        return null;
    }

    public CellRect findLeftBottomCell(CellRect cell) {
        //左侧非根节点
        if (null != cell.parent && cell.parent.parent != null) {
            int lbColIndex = cell.colIndex - 1;
            int lbColRowIndex = cell.parent.rowIndex + 1;
            List<CellRect> leftCells = cell.parent.parent.childCells;
            if (leftCells != null && leftCells.size() != 0) {
                if (lbColRowIndex == leftCells.size()) {
                    //已经是该列最后一个节点,返回末尾节点
                    return leftCells.get(leftCells.size() - 1);
                }
                for (CellRect leftCell : leftCells) {
                    if (leftCell.colIndex == lbColIndex && leftCell.rowIndex == lbColRowIndex) {
                        return leftCell;
                    }
                }
            }
        }
        return null;
    }

    public CellRect findBottomCell(CellRect cell) {
        int rowIndex = cell.rowIndex + 1;
        int colIndex = cell.colIndex;
        if (null != cell.parent && cell.parent.childCells != null) {
            for (CellRect childCell : cell.parent.childCells) {
                if (childCell.colIndex == colIndex && childCell.rowIndex == rowIndex) {
                    return childCell;
                }
            }
        }
        return null;
    }

    public CellRect findRightCell(int rowIndex, int colIndex, List<CellRect> childList) {
        if (childList != null) {
            for (CellRect cellRect : childList) {
                if (cellRect.rowIndex == rowIndex && cellRect.colIndex == colIndex) {
                    return cellRect;
                }
            }
        }
        return null;
    }

    public void visibleChildren(CellRect cell, int visible) {
        if (cell.hasChild()) {
            for (CellRect childCell : cell.childCells) {
                childCell.bindView.setVisibility(visible);
                visibleChildren(childCell, visible);
            }
        }
    }

    public boolean isRootNode(CellRect cell) {
        return cell.colIndex == 0 && cell.rowIndex == 0;
    }

    public CellRect findClickCell(float x, float y) {
        CellRect[] resultObject = new CellRect[1];
        recursiveFindClickCell(rootCell, x, y, resultObject);
        return resultObject[0];
    }

    private void recursiveFindClickCell(CellRect cell, float x, float y, CellRect[] resultObject) {
        RectF clipRect = new RectF(cell.bindView.getLeft(), cell.bindView.getTop(), cell.bindView.getRight(), cell.bindView.getBottom());
        if (clipRect != null && clipRect.contains(x, y) && cell.bindView.getVisibility() == View.VISIBLE) {
            resultObject[0] = cell;
        }
        if (cell.childCells != null) {
            for (CellRect childCell : cell.childCells) {
                recursiveFindClickCell(childCell, x, y, resultObject);
            }
        }
    }

    public CellRect findAttachCell(float x, float y) {
        CellRect[] resultObject = new CellRect[1];
        recursiveFindAttachCell(rootCell, x, y, resultObject);
        return resultObject[0];
    }

    private void recursiveFindAttachCell(CellRect cell, float x, float y, CellRect[] resultObject) {
        RectF clipRect = cell.getCollapseClipRect();
        if (clipRect != null && clipRect.contains(x, y) && cell.bindView.getVisibility() == View.VISIBLE) {
            resultObject[0] = cell;
        }
        if (cell.childCells != null) {
            for (CellRect childCell : cell.childCells) {
                recursiveFindAttachCell(childCell, x, y, resultObject);
            }
        }
    }

    public void openParentCollapse(CellRect cell) {
        if (cell != null) {
            if (cell.parent != null) {
                cell.parent.setCollapse(false);
            }
            openParentCollapse(cell.parent);
        }
    }

}
