package com.gray.reader.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.gray.reader.ReaderLayout;

import java.lang.ref.SoftReference;

/**
 * @author wjy on 2018/4/12.
 */
public class AuthorLineElement extends Element {
    private SoftReference<Context> contextSoftReference;
    private String content;
    private static ReaderLayout.pageProperty pageProperty;

    public static void setPageProperty(ReaderLayout.pageProperty pageProperty) {
        AuthorLineElement.pageProperty = pageProperty;
    }

    public AuthorLineElement(Context context, String content) {
        contextSoftReference = new SoftReference<>(context);
        this.content = content;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setTextSize(pageProperty.authorTextSize);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setColor(pageProperty.textColor);
        canvas.drawText(content, x, y, paint);
    }
}
