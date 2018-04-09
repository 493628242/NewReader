package com.gray.reader.page;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.gray.reader.R;
import com.gray.reader.ReadView;

/**
 * @author wjy on 2018/4/6.
 */
public abstract class BasePage extends FrameLayout implements IReaderPage {
    public ReadView smallTitle;
    public ReadView bigTitle;
    public ReadView content;

    public BasePage(Context context) {
        this(context, null);
    }

    public BasePage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BasePage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.layout_page, this,
                false);
        smallTitle = inflate.findViewById(R.id.small_title);
        bigTitle = inflate.findViewById(R.id.big_title);
        content = inflate.findViewById(R.id.content);
        addView(inflate);
        inflate.setBackgroundColor(Color.GREEN);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(MeasureSpec.makeMeasureSpec(getDisplayWidth(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(getDisplayHeight(), MeasureSpec.EXACTLY));
    }


    protected int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    protected int getDisplayHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }


}
