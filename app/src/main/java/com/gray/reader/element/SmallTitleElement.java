package com.gray.reader.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.gray.reader.page.ReaderLayout;

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
    private static ReaderLayout.pageProperty pageProperty;
    private SoftReference<Context> contextSoftReference;

    public SmallTitleElement(Context context, String title) {
        contextSoftReference = new SoftReference<>(context);
        this.title = title;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(pageProperty.otherTextSize);
        paint.setColor(pageProperty.otherTextColor);
        canvas.drawText(title, x, y, paint);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static void setPageProperty(ReaderLayout.pageProperty pageProperty) {
        SmallTitleElement.pageProperty = pageProperty;
    }
}
