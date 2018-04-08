package com.gray.reader;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.gray.reader.page.BasePage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.LinkedList;

/**
 * @author wjy on 2018/4/6.
 */
public class ReaderLayout extends FrameLayout {
    //起始点击位置
    private float downX;
    //移动距离
    private float moveX;

    private final float minMove = 50f;
    //用于存放书籍页面
    private LinkedList<BasePage> pages;

    private BasePage currentPage;
    private BasePage previousPage;
    private BasePage nextPage;
    //当前页数
    private int index;

    public ReaderLayout(@NonNull Context context) {
        this(context, null);
    }

    public ReaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        pages = new LinkedList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                moveX = event.getX() - downX;
                childMove(moveX);
                return true;
            case MotionEvent.ACTION_UP:
                if (Math.abs(moveX) > minMove) {
                    if (moveX < 0) {
                        moveToNext(moveX);
                    } else {
                        moveToPrevious(moveX);
                    }
                } else {
                    int widthPer3 = getDisplayWidth() / 3;
                    if (downX < widthPer3) {
                        Log.e("AA", "上翻页");
                        moveToPrevious(moveX);
                        return true;
                    } else if (downX > 2 * widthPer3) {
                        Log.e("AA", "下翻页");
                        moveToNext(moveX);
                        return true;
                    } else {
                        return performClick();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    //翻到前一页
    private void moveToPrevious(float moveX) {
        Log.e("moveToPrevious", "前一页");
        View childAt = getChildAt(0);
        int width = childAt.getWidth();
        childAt.setTranslationX(width);
    }

    //翻到下一页
    private void moveToNext(float moveX) {
        Log.e("moveToNext", "下一页");
        View childAt = getChildAt(0);
        int width = childAt.getWidth();
        ObjectAnimator objectAnimator =
                ObjectAnimator.ofFloat(childAt, "translationX", moveX, (float) (0 - width));
        objectAnimator.start();
    }

    private void childMove(float moveX) {
        View childAt = getChildAt(0);
        childAt.setTranslationX(moveX);
        if (pages.isEmpty()) {
            return;
        }
        if (moveX > 0) {
            pages.getFirst().currentToNext(moveX);
        } else {
            pages.getFirst().currentToPrevious(moveX);
        }
    }


    public void setPage(Class clazz) {
        if (!BasePage.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("class需要继承BasePage");
        }
        try {
            Constructor constructor = clazz.getConstructor(Context.class);

            for (int i = 0; i < 2; ++i) {
                BasePage basePage = (BasePage) constructor.newInstance(getContext());
                pages.add(0, basePage);
                addView(basePage, i);
            }
            previousPage = (BasePage) constructor.newInstance(getContext());

        } catch (NoSuchMethodException
                | InstantiationException
                | InvocationTargetException
                | IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    private int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int getDisplayHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}
