package com.gray.reader.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gray.reader.util.UIUtils;

import java.lang.ref.SoftReference;

/**
 * @author wjy on 2018/4/10.
 */
public class SmallTitleElement extends Element {
    public static final int DEF_TEXT_SIZE = 11;
    private String title;

    private SoftReference<Context> contextSoftReference;

    public SmallTitleElement(Context context, String title) {
        contextSoftReference = new SoftReference<>(context);
        this.title = title;

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(UIUtils.dip2px(contextSoftReference.get(), DEF_TEXT_SIZE));
        canvas.drawText(title, x, y, paint);
    }

}
