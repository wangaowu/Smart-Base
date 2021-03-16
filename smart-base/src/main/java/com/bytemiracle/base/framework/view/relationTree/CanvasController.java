package com.bytemiracle.base.framework.view.relationTree;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * 类功能：TODO:
 *
 * @author gwwang
 * @date 2021/2/9 9:01
 */
public class CanvasController {
    public static final int STROKE_WIDTH = 5;
    public static final int COLLAPSE_ICON_RADIUS = 20;
    private final Paint paint;

    public Canvas canvas;
    private int lineColor;

    /**
     * 构造器
     *
     * @param canvas    画布
     * @param lineColor 线条颜色
     */
    public CanvasController(Canvas canvas, int lineColor) {
        this.canvas = canvas;
        this.lineColor = lineColor;
        this.paint = initPaint();
    }

    private Paint initPaint() {
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(lineColor);
        return paint;
    }

    //-号
    public void drawCollapseOpenIcon(CellRect attachedCell, CellController cellController) {
//        if (!attachedCell.hasChild() || (attachedCell.hasChild() && attachedCell.childCells.size() == 1)) {
//            //没有叶子节点，或者有一个叶子节点
//            return;
//        }
        int centerX = (int) attachedCell.getCollapseClipRect().centerX();
        int centerY = (int) attachedCell.getCollapseClipRect().centerY();

        canvas.drawCircle(centerX, centerY, COLLAPSE_ICON_RADIUS, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, COLLAPSE_ICON_RADIUS - 1.5f * STROKE_WIDTH, paint);
        paint.setColor(lineColor);
        canvas.drawLine(centerX - COLLAPSE_ICON_RADIUS + 2f * STROKE_WIDTH, centerY, centerX + COLLAPSE_ICON_RADIUS - 2f * STROKE_WIDTH, centerY, paint);
    }

    //+号
    public void drawCollapseCloseIcon(CellRect attachedCell, CellController cellController) {
//        if (!attachedCell.hasChild() || (attachedCell.hasChild() && attachedCell.childCells.size() == 1)) {
//            //没有叶子节点，或者有一个叶子节点
//            return;
//        }
        int rightX = cellController.getRightX(attachedCell);
        int centerX = (int) attachedCell.getCollapseClipRect().centerX();
        int centerY = (int) attachedCell.getCollapseClipRect().centerY();

        canvas.drawCircle(centerX, centerY, COLLAPSE_ICON_RADIUS, paint);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(centerX, centerY, COLLAPSE_ICON_RADIUS - 1.5f * STROKE_WIDTH, paint);
        paint.setColor(lineColor);
        canvas.drawLine(centerX - COLLAPSE_ICON_RADIUS + 2f * STROKE_WIDTH, centerY, centerX + COLLAPSE_ICON_RADIUS - 2f * STROKE_WIDTH, centerY, paint);
        canvas.drawLine(centerX, centerY - COLLAPSE_ICON_RADIUS + 2f * STROKE_WIDTH, centerX, centerY + COLLAPSE_ICON_RADIUS - 2f * STROKE_WIDTH, paint);

        //连接线
        canvas.drawLine(rightX, centerY, centerX - COLLAPSE_ICON_RADIUS, centerY, paint);
    }

    public void drawRect(RectF ff) {
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        canvas.drawRect(ff, paint);
    }


    public void drawPolygonLine(CellRect cell, CellRect toCell, CellController cellController) {
        int startY = cellController.getCenterY(cell);
        int endY = cellController.getCenterY(toCell);
        drawPolygonLine(cell.bindView.getRight(), startY, toCell.bindView.getX(), endY);
    }

    private void drawPolygonLine(float startX, float startY, float endX, float endY) {
        if (startY == endY) {
            //水平线
            canvas.drawLine(startX, startY, endX, endY, paint);
            return;
        }
        //折线
        //取个中间值作为直线拐点
        float centerX = (startX + endX) * .5f;
        //低平线
        canvas.drawLine(startX, startY, centerX, startY, paint);
        //竖线
        canvas.drawLine(centerX, startY, centerX, endY, paint);
        //高平线
        canvas.drawLine(centerX - STROKE_WIDTH * .5f, endY, endX, endY, paint);
    }
}
