package com.gray.reader.page;

import android.animation.ValueAnimator;

/**
 * @author wjy on 2018/4/8.
 */
public interface IReaderPage {
    //当前页面向左移动
    void currentToPrevious(float changeX, float moveY, float moveX);

    //当前页面向右移动
    void currentToNext(float changeX, float moveY, float moveX);

    void previousToCurrent(float changeX, float moveY, float moveX);

    void nextToCurrent(float changeX, float moveY, float moveX);

    //当前页面向左移动 动画效果
    ValueAnimator animCurrentToPrevious(float changeX, float moveY, float moveX);

    //当前页面向右移动
    ValueAnimator animCurrentToNext(float changeX, float moveY, float moveX);

    ValueAnimator animPreviousToCurrent(float changeX, float moveY, float moveX);

    ValueAnimator animNextToCurrent(float changeX, float moveY, float moveX);

    void reset();
}
