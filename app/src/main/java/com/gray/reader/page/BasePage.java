package com.gray.reader.page;

import android.animation.ObjectAnimator;
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
public abstract class BasePage extends View {
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
        paint.setTextSize(20);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
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
        canvas.drawText(System.currentTimeMillis() + "", (float) getDisplayHeight(),
                (float) getDisplayHeight(), paint);
    }

    protected int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    protected int getDisplayHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    //当前页面向左移动
    public abstract void currentToPrevious(float moveX);

    //当前页面向右移动
    public abstract void currentToNext(float moveX);

    public abstract void previousToCurrent(float moveX);

    public abstract void nextToCurrent(float moveX);

    //当前页面向左移动 动画效果
    public abstract ObjectAnimator animCurrentToPrevious(float moveX);

    //当前页面向右移动
    public abstract ObjectAnimator animCurrentToNext(float moveX);

    public abstract ObjectAnimator animPreviousToCurrent(float moveX);

    public abstract ObjectAnimator animNextToCurrent(float moveX);

}
