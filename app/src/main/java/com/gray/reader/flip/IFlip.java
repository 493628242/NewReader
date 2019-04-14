package com.gray.reader.flip;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * @author wjy on 2019/3/21.
 */
public interface IFlip {

    /**
     * 获取翻页的方向
     * 可用于判断是点击位置是否可以翻页
     */
    FlipDirection getFlipDirection();

    /**
     * 实现翻下一页的动画效果
     *
     * @param current 当前显示的bitmap
     * @param next    下一页的bitmap
     * @param canvas  画板
     * @param deltaX  手指划过的距离x
     * @param deltaY  手指划过的距离y
     */
    void flipToNext(Bitmap current, Bitmap next, Canvas canvas, float deltaX, float deltaY);

    /**
     * 实现翻前一页的动画效果
     *
     * @param current 当前显示的bitmap
     * @param prev    前一页的bitmap
     * @param canvas  画板
     * @param deltaX  手指划过的距离x
     * @param deltaY  手指划过的距离y
     */
    void flipToPrev(Bitmap current, Bitmap prev, Canvas canvas, float deltaX, float deltaY);


    void reset(Bitmap current,Canvas canvas);

    enum FlipDirection {
        Horizontal, vertical
    }
}
