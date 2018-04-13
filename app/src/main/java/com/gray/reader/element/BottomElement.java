package com.gray.reader.element;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.gray.reader.ReaderLayout;
import com.gray.reader.util.UIUtils;

import java.lang.ref.SoftReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author wjy on 2018/4/10.
 */
public class BottomElement extends Element {
    public static final int DEF_TEXT_SIZE = 11;
    public static final int DEF_TEXT_COLOR = 0xFF999999;
    private static int textSize = DEF_TEXT_SIZE;
    private static int textColor = DEF_TEXT_COLOR;
    private final RectF mHeadRect;
    private final RectF mMainRect;
    private SoftReference<Context> contextSoftReference;
    private String index;
    private int batteryWidth;
    private int batteryHeight;
    private float power;
    private int mMargin = 2;    //电池内芯与边框的距离
    private int mBorder = 1;     //电池外框的宽
    private SimpleDateFormat format;
    private static ReaderLayout.pageProperty pageProperty;

    public BottomElement(Context context) {
        contextSoftReference = new SoftReference<>(context);
        batteryWidth = UIUtils.dip2px(context, 22);
        batteryHeight = UIUtils.dip2px(context, 9);
        int y = UIUtils.getDisplayHeight(context) - UIUtils.dip2px(context, Element.PADDING_BOTTOM);
        int x = UIUtils.getDisplayWidth(context) - UIUtils.dip2px(context, Element.PADDING_END);
        mHeadRect = new RectF(x - UIUtils.dip2px(context, 2),
                y - UIUtils.dip2px(context, 6), x,
                y - UIUtils.dip2px(context, 3));
        float left = x - batteryWidth;
        float top = y - batteryHeight;
        float right = mHeadRect.right - UIUtils.dip2px(context, 3);
        float bottom = mHeadRect.bottom + UIUtils.dip2px(context, 3);
        mMainRect = new RectF(left, top, right, bottom);
        format = new SimpleDateFormat("HH:mm", Locale.CHINA);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        Context context = contextSoftReference.get();
        paint.setTextSize(pageProperty.otherTextSize);
        paint.setColor(pageProperty.otherTextColor);
        x = UIUtils.dip2px(context, Element.PADDING_START);
        y = UIUtils.getDisplayHeight(context) - UIUtils.dip2px(context, Element.PADDING_BOTTOM);
        canvas.drawText(index, x, y, paint);
        String time = format.format(new Date(System.currentTimeMillis()));
        int timeMargin = 74;//dp
        x = UIUtils.getDisplayWidth(context) - UIUtils.dip2px(context, timeMargin);
        canvas.drawText(time, x, y, paint);
        drawBattery(canvas, paint);
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setPower(float power) {
        this.power = power;
    }

    private void drawBattery(Canvas canvas, Paint paint) {
        //画外框
        paint.setStyle(Paint.Style.STROKE);    //设置空心矩形
        paint.setStrokeWidth(UIUtils.dip2px(contextSoftReference.get(), mBorder));          //设置边框宽度
        paint.setColor(pageProperty.otherTextColor);
        canvas.drawRoundRect(mMainRect, UIUtils.dip2px(contextSoftReference.get(), 1),
                UIUtils.dip2px(contextSoftReference.get(), 1), paint);

        //画电池头
        paint.setStyle(Paint.Style.FILL_AND_STROKE);  //实心
        paint.setColor(pageProperty.otherTextColor);
        canvas.drawRoundRect(mHeadRect, UIUtils.dip2px(contextSoftReference.get(), 2),
                UIUtils.dip2px(contextSoftReference.get(), 2), paint);
        //画电池芯
        if (power < 0.1) {
            paint.setColor(Color.RED);
        } else {
            paint.setColor(pageProperty.otherTextColor);
        }
        int marginPX = UIUtils.dip2px(contextSoftReference.get(), mMargin);
        int left = (int) (mMainRect.left + marginPX);
        int right = (int) ((mMainRect.right - left - marginPX) * power) + left;
        int top = (int) (mMainRect.top + marginPX);
        int bottom = (int) (mMainRect.bottom - marginPX);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, paint);
    }

    public static void setPageProperty(ReaderLayout.pageProperty pageProperty) {
        BottomElement.pageProperty = pageProperty;
    }
}
