package com.gray.reader.page;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author wjy on 2018/4/6.
 */
public abstract class BasePage extends View implements IReaderPage {
    private Paint paint;

    public BasePage(Context context) {
        this(context, null);
    }

    public BasePage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasePage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
        paint.setTextSize(50);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.GREEN);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(getDisplayWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getDisplayHeight(), MeasureSpec.EXACTLY));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(System.currentTimeMillis() + "", (float) 200,
                (float) 200, paint);
    }

    protected int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    protected int getDisplayHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }


}
