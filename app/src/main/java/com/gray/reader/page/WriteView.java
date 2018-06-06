package com.gray.reader.page;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.gray.reader.element.BottomElement;
import com.gray.reader.element.Element;
import com.gray.reader.flip.PageFlipView;

import java.util.ArrayList;

/**
 * @author wjy on 2018/4/10.
 */
public class WriteView extends View {
    protected Paint paint;
    protected ArrayList<Element> page;
    protected int count;
    protected int index;
    protected float power;
    protected static ReaderLayout.pageProperty pageProperty;

    public WriteView(Context context) {
        super(context);
        init();
    }


    public static void setPageProperty(ReaderLayout.pageProperty pageProperty) {
        WriteView.pageProperty = pageProperty;
    }

    private void init() {
        paint = new Paint();
        resetPaint();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPage(ArrayList<Element> page) {
        this.page = page;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPower(float power) {
        this.power = power;
    }


    public void notifyData() {
        postInvalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(pageProperty.bgColor);
        if (page == null) {
            return;
        }
        for (Element element : page) {
            if (element instanceof BottomElement) {
                BottomElement element1 = (BottomElement) element;
                element1.setIndex(index + "/" + count);
                element1.setPower(power);
            }
            element.draw(canvas, paint);
            resetPaint();
        }
    }

    protected void resetPaint() {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    protected void resetPaint(Paint paint) {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

}
