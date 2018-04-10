package com.gray.reader.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import com.gray.reader.util.UIUtils;

import java.lang.ref.SoftReference;

/**
 * @author wjy on 2018/4/10.
 */
public class BigTitleElement extends Element {
    public static final int DEF_TEXT_SIZE = 25;//sp
    public static final int DEF_LINE_SPACE = 5;//sp
    private String title;

    private SoftReference<Context> contextSoftReference;

    public BigTitleElement(Context context, String title) {
        contextSoftReference = new SoftReference<>(context);
        this.title = title;

    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        String[] split = title.split("\n");
        paint.setTextSize(UIUtils.dip2px(contextSoftReference.get(), DEF_TEXT_SIZE));
        int width = UIUtils.getDisplayWidth(contextSoftReference.get());
        for (int i = 0; i < split.length; i++) {
            Rect rect = new Rect();
            paint.getTextBounds(split[i], 0, split[i].length(), rect);
            int height = i * (rect.height()
                    + UIUtils.dip2px(contextSoftReference.get(), DEF_LINE_SPACE));
            y += height;
            x = (int) ((width - paint.measureText(split[i])) / 2 + 0.5f);
            canvas.drawText(title, x, y, paint);
        }
    }


}
