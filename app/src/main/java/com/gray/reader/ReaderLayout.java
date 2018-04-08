package com.gray.reader;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.gray.reader.page.BasePage;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
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
        Log.e("index", index + "-");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                moveX = 0;
                Log.e("downX", downX + "_");
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
        if (index == 0) {
            return;
        }
        ObjectAnimator objectAnimator = currentPage.animCurrentToNext(moveX);
        ObjectAnimator objectAnimator1 = previousPage.animPreviousToCurrent(moveX);
        objectAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                afterToPrevious();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
        objectAnimator1.start();
    }

    //翻到下一页
    private void moveToNext(float moveX) {
        Log.e("moveToNext", "下一页");
        //是否还有下一页
        if (pages.size() == 1) {
            return;
        }
        ObjectAnimator objectAnimator = currentPage.animCurrentToPrevious(moveX);
        ObjectAnimator objectAnimator1 = nextPage.animNextToCurrent(moveX);
        objectAnimator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                afterToNext();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
        objectAnimator1.start();
    }

    private void childMove(float moveX) {
        if (pages.isEmpty()) {
            return;
        }
        if (moveX > 0) {
            if (index == 0) {
                return;
            }
            currentPage.currentToNext(moveX);
            previousPage.previousToCurrent(moveX);
        } else {
            if (pages.size() == 1) {
                return;
            }
            currentPage.currentToPrevious(moveX);
            nextPage.nextToCurrent(moveX);
        }
    }


    private void afterToPrevious() {
        //重新确认页面位置
        pages.push(previousPage);
        previousPage = pages.removeLast();
        currentPage = pages.getFirst();
        nextPage = pages.getLast();
        --index;
    }

    private void afterToNext() {
        //重新确认页面位置
        pages.add(previousPage);
        previousPage = pages.removeFirst();
        currentPage = pages.getFirst();
        nextPage = pages.getLast();
        ++index;
    }


    //创建页面
    public void setPage(Class clazz) {
        if (!BasePage.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("class需要继承BasePage");
        }
        try {
            Constructor constructor = clazz.getConstructor(Context.class);

            for (int i = 0; i < 2; ++i) {
                BasePage basePage = (BasePage) constructor.newInstance(getContext());
                pages.add(0, basePage);
            }
            previousPage = (BasePage) constructor.newInstance(getContext());
            previousPage.setBackgroundColor(Color.RED);
            addView(previousPage, 0 - getDisplayWidth(), 0);
            nextPage = pages.getLast();
            nextPage.setBackgroundColor(Color.BLUE);
            addView(nextPage, getDisplayWidth(), 0);
            currentPage = pages.getFirst();
            currentPage.setBackgroundColor(Color.YELLOW);
            addView(currentPage);
        } catch (NoSuchMethodException
                | InstantiationException
                | InvocationTargetException
                | IllegalAccessException e) {
            Log.e("Error", Arrays.toString(e.getStackTrace()));
        }

    }

    private int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int getDisplayHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}
