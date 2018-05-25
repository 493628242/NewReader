package com.gray.reader.element;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.gray.reader.R;
import com.gray.reader.page.ReaderLayout;
import com.gray.reader.util.UIUtils;

import java.lang.ref.SoftReference;

/**
 * @author wjy on 2018/4/12.
 */
public class AuthorHeadElement extends Element {
    public static final int DEF_HEAD_HEIGHT = 105;
    private static ReaderLayout.pageProperty pageProperty;
    private int textSize;//sp
    private SoftReference<Context> contextSoftReference;
    private static Bitmap bitmap;
    private RectF rectF;
    private final int bitmapWidth = 40;
    private final int imgTitleSpace = 8;
    private String author = "作者有话说";
    private Rect rect;
    private int lineColor = 0xFF999999;

    public AuthorHeadElement(Context context) {
        contextSoftReference = new SoftReference<>(context);
        textSize = UIUtils.dip2px(context, 16);
        if (bitmap == null) {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_author);
        }
        rectF = new RectF();
        rect = new Rect();
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        Context context = contextSoftReference.get();
        int width = UIUtils.getDisplayWidth(context);
        rectF.left = width / 2 - UIUtils.dip2px(context, bitmapWidth / 2);
        rectF.right = width / 2 + UIUtils.dip2px(context, bitmapWidth / 2);
        rectF.top = 0;
        rectF.bottom = UIUtils.dip2px(context, bitmapWidth);
        rectF.top += y;
        rectF.bottom += y;
        canvas.drawBitmap(bitmap, null, rectF, paint);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(pageProperty.textColor);
        paint.setTextSize(textSize);
        int tX = width / 2;
        int tY = (int) (rectF.bottom + UIUtils.dip2px(context, imgTitleSpace) + textSize);
        canvas.drawText(author, tX, tY, paint);
        paint.getTextBounds(author, 0, author.length(), rect);
        int lineStartX = UIUtils.dip2px(context, PADDING_START);
        int lineEndX = ((width - rect.width()) / 2) - UIUtils.dip2px(context, 11);
        int lineY = tY - textSize / 2 + UIUtils.dip2px(context, 1);
        Log.e("line1", lineStartX + "\n" + lineEndX);
        paint.setColor(lineColor);
        canvas.drawLine(lineStartX, lineY, lineEndX, lineY, paint);
        lineStartX = (width + rect.width()) / 2 + UIUtils.dip2px(context, 11);
        lineEndX = width - UIUtils.dip2px(context, PADDING_END);
        Log.e("line2", lineStartX + "\n" + lineEndX);
        canvas.drawLine(lineStartX, lineY, lineEndX, lineY, paint);

    }

    public static void setPageProperty(ReaderLayout.pageProperty pageProperty) {
        AuthorHeadElement.pageProperty = pageProperty;
    }

}
