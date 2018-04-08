package com.gray.reader.page;

import android.animation.ValueAnimator;

/**
 * @author wjy on 2018/4/8.
 */
public interface IReaderPage {
    //当前页面向左移动
    void currentToPrevious(float moveX, float moveY, float mMoveX);

    //当前页面向右移动
    void currentToNext(float moveX, float moveY, float mMoveX);

    void previousToCurrent(float moveX, float moveY, float mMoveX);

    void nextToCurrent(float moveX, float moveY, float mMoveX);

    //当前页面向左移动 动画效果
    ValueAnimator animCurrentToPrevious(float moveX, float moveY, float mMoveX);

    //当前页面向右移动
    ValueAnimator animCurrentToNext(float moveX, float moveY, float mMoveX);

    ValueAnimator animPreviousToCurrent(float moveX, float moveY, float mMoveX);

    ValueAnimator animNextToCurrent(float moveX, float moveY, float mMoveX);

    void reset();
}
