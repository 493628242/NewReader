package com.gray.reader.flip;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author wjy on 2019/3/21.
 */
public class HorizontalFlip implements IFlip {

    private Paint mPaint = new Paint();

    @Override
    public FlipDirection getFlipDirection() {
        return FlipDirection.Horizontal;
    }

    @Override
    public void flipToNext(Bitmap current, Bitmap next, Canvas canvas, float deltaX, float deltaY) {
        canvas.drawBitmap(current, deltaX, deltaY, mPaint);
        canvas.drawBitmap(next, deltaX, deltaY, mPaint);
    }

    @Override
    public void flipToPrev(Bitmap current, Bitmap prev, Canvas canvas, float deltaX, float deltaY) {
        canvas.drawBitmap(current, deltaX, deltaY, mPaint);
        canvas.drawBitmap(prev, deltaX, deltaY, mPaint);
    }

    @Override
    public void reset(Bitmap current, Canvas canvas) {

    }
}
