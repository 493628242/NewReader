package com.gray.reader;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.gray.reader.page.IReaderPage;

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
    //移动距离X
    private float mMoveX;
    //移动距离Y
    private float mMoveY;

    private final float minMove = 50f;
    //用于存放书籍页面
    private LinkedList<IReaderPage> pages;

    private IReaderPage currentPage;
    private IReaderPage previousPage;
    private IReaderPage nextPage;
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
                mMoveX = 0;
                Log.e("downX", downX + "_");
                return true;
            case MotionEvent.ACTION_MOVE:
                mMoveX = event.getX();
                mMoveY = event.getY();
                childMove(mMoveX - downX, mMoveY);
                return true;
            case MotionEvent.ACTION_UP:
                float changeX = mMoveX - downX;
                if (mMoveX != 0 && Math.abs(changeX) > minMove) {
                    if (changeX < 0) {
                        moveToNext(changeX, mMoveY, mMoveX);
                    } else {
                        moveToPrevious(changeX, mMoveY, mMoveX);
                    }
                } else {
                    int widthPer3 = getDisplayWidth() / 3;
                    if (downX < widthPer3) {
                        Log.e("AA", "上翻页");
                        moveToPrevious(changeX, mMoveY, mMoveX);
                        return true;
                    } else if (downX > 2 * widthPer3) {
                        Log.e("AA", "下翻页");
                        moveToNext(changeX, mMoveY, mMoveX);
                        return true;
                    } else {
                        currentPage.reset();
                        return performClick();
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    //翻到前一页
    private void moveToPrevious(float moveX, float moveY, float mMoveX) {
        Log.e("moveToPrevious", "前一页");
        if (index == 0) {
            return;
        }
        ValueAnimator objectAnimator = currentPage.animCurrentToNext(moveX, moveY, mMoveX);
        if (objectAnimator != null) {
            objectAnimator.start();
        }
        ValueAnimator objectAnimator1 = previousPage.animPreviousToCurrent(moveX, moveY, mMoveX);
        if (objectAnimator1 != null) {
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
            objectAnimator1.start();
        }
    }

    //翻到下一页
    private void moveToNext(float moveX, float moveY, float mMoveX) {
        Log.e("moveToNext", "下一页");
        //是否还有下一页
        if (pages.size() == 1) {
            return;
        }
        ValueAnimator objectAnimator = currentPage.animCurrentToPrevious(moveX, moveY, mMoveX);
        if (objectAnimator != null) {
            objectAnimator.addListener(new Animator.AnimatorListener() {
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
        }
        ValueAnimator objectAnimator1 = nextPage.animNextToCurrent(moveX, moveY, mMoveX);
        if (objectAnimator1 != null) {
            objectAnimator1.start();
        } else {

        }
    }

    private void childMove(float moveX, float moveY) {
        if (pages.isEmpty()) {
            return;
        }
        if (moveX > 0) {
            if (index == 0) {
                return;
            }
            currentPage.currentToNext(moveX, moveY, mMoveX);
            previousPage.previousToCurrent(moveX, moveY, mMoveX);
        } else {
            if (pages.size() == 1) {
                return;
            }
            currentPage.currentToPrevious(moveX, moveY, mMoveX);
            nextPage.nextToCurrent(moveX, moveY, mMoveX);
        }
    }

    private void afterToPrevious() {
        //重新确认页面位置
        pages.push(previousPage);
        previousPage = pages.removeLast();
        currentPage = pages.getFirst();
        nextPage = pages.getLast();
        currentPage.reset();
        previousPage.reset();
        nextPage.reset();
        ((View) currentPage).bringToFront();
        --index;
    }

    private void afterToNext() {
        //重新确认页面位置
        pages.add(previousPage);
        previousPage = pages.removeFirst();
        currentPage = pages.getFirst();
        nextPage = pages.getLast();
        currentPage.reset();
        previousPage.reset();
        nextPage.reset();
        ((View) currentPage).bringToFront();
        ++index;
    }


    //创建页面
    public void setPage(Class clazz) {
        if (!IReaderPage.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("class需要继承IReaderPage");
        }
        try {
            Constructor constructor = clazz.getConstructor(Context.class);
            for (int i = 0; i < 2; ++i) {
                IReaderPage IReaderPage = (IReaderPage) constructor.newInstance(getContext());
                pages.add(0, IReaderPage);
            }
            previousPage = (IReaderPage) constructor.newInstance(getContext());
            ((View) previousPage).setBackgroundColor(Color.RED);
            addView((View) previousPage, 0 - getDisplayWidth(), 0);
            nextPage = pages.getLast();
            ((View) nextPage).setBackgroundColor(Color.BLUE);
            addView((View) nextPage, getDisplayWidth(), 0);
            currentPage = pages.getFirst();
            ((View) currentPage).setBackgroundColor(Color.YELLOW);
            addView((View) currentPage);
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
