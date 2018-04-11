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
    public static final int DEF_TEXT_COLOR = 0xFF999999;
    private static int textSize = DEF_TEXT_SIZE;
    private static int textColor = DEF_TEXT_COLOR;

    private String title;

    private SoftReference<Context> contextSoftReference;

    public SmallTitleElement(Context context, String title) {
        contextSoftReference = new SoftReference<>(context);
        this.title = title;

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        Context context = contextSoftReference.get();
        paint.setTextSize(UIUtils.dip2px(context, DEF_TEXT_SIZE));
        paint.setTextSize(UIUtils.dip2px(context, textSize));
        paint.setColor(textColor);
        canvas.drawText(title, x, y, paint);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
