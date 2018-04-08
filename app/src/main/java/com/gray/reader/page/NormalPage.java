package com.gray.reader.page;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author wjy on 2018/4/8.
 */
public class NormalPage extends BasePage {
    public NormalPage(Context context) {
        this(context, null);
    }

    public NormalPage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalPage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void currentToPrevious(float moveX, float moveY, float mMoveX) {
        setTranslationX(moveX);
    }

    @Override
    public void currentToNext(float moveX, float moveY, float mMoveX) {
        setTranslationX(moveX);
    }

    @Override
    public void previousToCurrent(float moveX, float moveY, float mMoveX) {
        //moveX一定>0
        setTranslationX(0 - getDisplayWidth() + moveX);
    }

    @Override
    public void nextToCurrent(float moveX, float moveY, float mMoveX) {
        //moveX<0
        setTranslationX(getDisplayWidth() + moveX);

    }

    @Override
    public ObjectAnimator animCurrentToPrevious(float moveX, float moveY, float mMoveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        mMoveX == 0 ? 0 : moveX, (float) (0 - getDisplayWidth()));
//        objectAnimator.start();
    }

    @Override
    public ObjectAnimator animCurrentToNext(float moveX, float moveY, float mMoveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        mMoveX == 0 ? 0 : moveX, (float) getDisplayWidth());
//        objectAnimator.start();
    }

    @Override
    public ObjectAnimator animPreviousToCurrent(float moveX, float moveY, float mMoveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        mMoveX == 0 ? 0 - mMoveX : (0 - getDisplayWidth() + moveX), 0);
//        objectAnimator.start();
    }

    @Override
    public ObjectAnimator animNextToCurrent(float moveX, float moveY, float mMoveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        mMoveX == 0 ? getDisplayWidth() : (getDisplayWidth() + moveX), 0);
//        objectAnimator.start();
    }

    @Override
    public void reset() {
        setX(0);
    }
}
