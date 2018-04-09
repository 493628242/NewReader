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
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.gray.reader.page.BasePage;
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
    private volatile int index;
    private ReaderAdapter adapter;
    private boolean animStatus1 = false;
    private boolean animStatus2 = false;

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

    public void setAdapter(ReaderAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("index", index + "-");
        if (!animStatus1 && !animStatus2) {
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
        }
        return super.onTouchEvent(event);
    }

    //翻到前一页
    private synchronized void moveToPrevious(float moveX, float moveY, float mMoveX) {
        Log.e("moveToPrevious", "前一页");
        if (index == 0) {
            return;
        }
        addPreviousToParent(moveX, previousPage);
        ValueAnimator objectAnimator = currentPage.animCurrentToNext(moveX, moveY, mMoveX);
        if (objectAnimator != null) {
            objectAnimator.start();
        }
        ValueAnimator objectAnimator1 = previousPage.animPreviousToCurrent(moveX, moveY, mMoveX);
        if (objectAnimator1 != null) {
            objectAnimator1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animStatus1 = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    afterToPrevious();
                    animStatus1 = false;

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
    private synchronized void moveToNext(float moveX, float moveY, float mMoveX) {
        Log.e("moveToNext", "下一页");
        //是否还有下一页
        if (pages.size() == 1) {
            return;
        }
        addNextToParent(moveX, nextPage);
        ValueAnimator objectAnimator = currentPage.animCurrentToPrevious(moveX, moveY, mMoveX);
        if (objectAnimator != null) {
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animStatus2 = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    afterToNext();
                    animStatus2 = false;
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

    private synchronized void childMove(float moveX, float moveY) {
        if (pages.isEmpty()) {
            return;
        }
        if (moveX > 0) {
            if (index == 0) {
                return;
            }
            addPreviousToParent(moveX, previousPage);
            currentPage.currentToNext(moveX, moveY, mMoveX);
            previousPage.previousToCurrent(moveX, moveY, mMoveX);
        } else {
            if (pages.size() == 1) {
                return;
            }
            addNextToParent(moveX, nextPage);
            currentPage.currentToPrevious(moveX, moveY, mMoveX);
            nextPage.nextToCurrent(moveX, moveY, mMoveX);
        }
    }

    //添加页面进入parent
    private void addPreviousToParent(float moveX, final IReaderPage readerPage) {
        if (indexOfChild((View) readerPage) == -1) {
            addView((View) readerPage, (int) (0 - getDisplayWidth() + moveX), 0);
            ((View) readerPage).getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            adapter.loadPrevious((BasePage) readerPage, index - 1);
                            ((View) readerPage).getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        }
                    });
        }
    }

    //添加页面进入parent
    private void addNextToParent(float moveX, final IReaderPage readerPage) {
        if (indexOfChild((View) readerPage) == -1) {
            addView((View) readerPage, (int) (getDisplayWidth() + moveX), 0);
            ((View) readerPage).getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            adapter.loadNext((BasePage) readerPage, index + 1);
                            ((View) readerPage).getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);

                        }
                    });
        }
    }

    private synchronized void afterToPrevious() {
        //重新确认页面位置
        pages.add(0, previousPage);
        previousPage = pages.removeLast();
        currentPage = pages.getFirst();
        nextPage = pages.getLast();
        previousPage.reset();
        nextPage.reset();
        currentPage.reset();
        resetView();
        --index;
    }

    private synchronized void afterToNext() {
        //重新确认页面位置
        pages.add(previousPage);
        previousPage = pages.removeFirst();
        currentPage = pages.getFirst();
        nextPage = pages.getLast();
        previousPage.reset();
        nextPage.reset();
        currentPage.reset();
        resetView();
        ++index;
    }

    private void resetView() {
        removeView((View) previousPage);
        removeView((View) nextPage);
        removeView((View) currentPage);
        addView((View) currentPage, 0, 0);
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
            ((View) previousPage).setBackgroundColor(Color.TRANSPARENT);
            addView((View) previousPage, 0 - getDisplayWidth(), 0);
            nextPage = pages.getLast();
            ((View) nextPage).setBackgroundColor(Color.TRANSPARENT);
            addView((View) nextPage, getDisplayWidth(), 0);
            currentPage = pages.getFirst();
            ((View) currentPage).setBackgroundColor(Color.TRANSPARENT);
            addView((View) currentPage);
            //加载第一章数据
            ((View) currentPage).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    adapter.loadFirst((BasePage) currentPage, index);
                    ((View) currentPage).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

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
