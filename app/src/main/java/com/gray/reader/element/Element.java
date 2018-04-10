package com.gray.reader.element;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author wjy on 2018/4/10.
 */
public abstract class Element {
    public static final int PADDING_START = 15;
    public static final int PADDING_END = 15;
    public static final int PADDING_BTOM = 15;
    public static final int PADDING_TOP = 15;
    protected int x;
    protected int y;

    public abstract void draw(Canvas canvas, Paint paint);

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
