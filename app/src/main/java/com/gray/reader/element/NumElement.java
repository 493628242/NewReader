package com.gray.reader.element;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * @author wjy on 2019/4/14.
 */
public class NumElement extends Element {

    private int num;

    public NumElement(int num) {
        this.num = num;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(20);
        paint.setColor(Color.BLACK);
        canvas.drawText(String.valueOf(num),
                0, 0, paint);
    }
}
