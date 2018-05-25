package com.gray.reader.page;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.gray.reader.util.UIUtils;

/**
 * @author wjy on 2018/4/8.
 */
public class NormalPage extends WriteView implements IReaderPage {
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
    public void currentToPrevious(float changeX, float moveY, float moveX) {
        setTranslationX(changeX);
    }

    @Override
    public void currentToNext(float changeX, float moveY, float moveX) {
        setTranslationX(changeX);
    }

    @Override
    public void previousToCurrent(float changeX, float moveY, float moveX) {
        //moveX一定>0
        setTranslationX(0 - UIUtils.getDisplayWidth(getContext()) + changeX);
    }

    @Override
    public void nextToCurrent(float changeX, float moveY, float moveX) {
        //moveX<0
        setTranslationX(UIUtils.getDisplayWidth(getContext()) + changeX);

    }

    @Override
    public ObjectAnimator animCurrentToPrevious(float changeX, float moveY, float moveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        moveX == 0 ? 0 : changeX, (float) (0 - UIUtils.getDisplayWidth(getContext())));
//        objectAnimator.start();
    }

    @Override
    public ObjectAnimator animCurrentToNext(float changeX, float moveY, float moveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        moveX == 0 ? 0 : changeX, (float) UIUtils.getDisplayWidth(getContext()));
//        objectAnimator.start();
    }

    @Override
    public ObjectAnimator animPreviousToCurrent(float changeX, float moveY, float moveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        moveX == 0 ? 0 - UIUtils.getDisplayWidth(getContext())
                                : (0 - UIUtils.getDisplayWidth(getContext()) + changeX), 0);
//        objectAnimator.start();
    }

    @Override
    public ObjectAnimator animNextToCurrent(float changeX, float moveY, float moveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        moveX == 0 ? UIUtils.getDisplayWidth(getContext())
                                : (UIUtils.getDisplayWidth(getContext()) + changeX), 0);
//        objectAnimator.start();
    }

    @Override
    public void reset() {
        setX(0);
    }
}
