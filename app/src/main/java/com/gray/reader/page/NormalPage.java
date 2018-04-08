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
    public void currentToPrevious(float moveX) {
        setTranslationX(moveX);
    }

    @Override
    public void currentToNext(float moveX) {
        setTranslationX(moveX);
    }

    @Override
    public void previousToCurrent(float moveX) {
        //moveX一定>0
        setTranslationX(0 - getDisplayWidth() + moveX);
    }

    @Override
    public void nextToCurrent(float moveX) {
        //moveX<0
        setTranslationX(getDisplayWidth() + moveX);

    }

    @Override
    public ObjectAnimator animCurrentToPrevious(float moveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        moveX, (float) (0 - getDisplayWidth()));
//        objectAnimator.start();
    }

    @Override
    public ObjectAnimator animCurrentToNext(float moveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        moveX, (float) getDisplayWidth());
//        objectAnimator.start();
    }

    @Override
    public ObjectAnimator animPreviousToCurrent(float moveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        (0 - getDisplayWidth() + moveX), 0);
//        objectAnimator.start();
    }

    @Override
    public ObjectAnimator animNextToCurrent(float moveX) {
        return
                ObjectAnimator.ofFloat(this, "translationX",
                        (getDisplayWidth() + moveX), 0);
//        objectAnimator.start();
    }
}
