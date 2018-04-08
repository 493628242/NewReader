package com.gray.reader.page;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author wjy on 2018/4/6.
 */
public abstract class BasePage extends View {
    public BasePage(Context context) {
        this(context, null);
    }

    public BasePage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasePage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //当前页面向左移动
    public abstract void currentToPrevious(float moveX);

    //当前页面向右移动
    public abstract void currentToNext(float moveX);

    public abstract void previousToCurrent(float moveX);

    public abstract void nextToCurrent(float moveX);

}
