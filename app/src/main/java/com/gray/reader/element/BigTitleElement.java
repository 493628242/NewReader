package com.gray.reader.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;

import com.gray.reader.util.UIUtils;

import java.lang.ref.SoftReference;

/**
 * @author wjy on 2018/4/10.
 */
public class BigTitleElement extends Element {
    public static final int DEF_TEXT_SIZE = 25;//sp
    public static final int DEF_LINE_SPACE = 5;//sp
    public static final int DEF_TEXT_COLOR = Color.BLACK;
    private static int textSize;
    private static int textColor;
    private String title;
    private SoftReference<Context> contextSoftReference;

    public BigTitleElement(Context context, String title) {
        contextSoftReference = new SoftReference<>(context);
        this.title = title;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        Context context = contextSoftReference.get();
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        String[] split = title.split("\n");
        paint.setTextSize(textSize);
        paint.setTextAlign(Paint.Align.CENTER);
        int width = UIUtils.getDisplayWidth(context);
        Rect rect = new Rect();
        for (int i = 0; i < split.length; i++) {
            paint.getTextBounds(split[i], 0, split[i].length(), rect);
            int height = i * (rect.height()
                    + UIUtils.dip2px(context, DEF_LINE_SPACE));
            x = width / 2;
            canvas.drawText(split[i], x, y + height, paint);
        }
    }

    public static void setTextSize(int textSize) {
        BigTitleElement.textSize = textSize;
    }

    public static void setTextColor(int textColor) {
        BigTitleElement.textColor = textColor;
    }
}
