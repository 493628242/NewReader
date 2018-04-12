package com.gray.reader.page;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.gray.reader.R;

/**
 * @author wjy on 2018/4/8.
 */
public class FlipPage extends BasePage {
    private Paint pointPaint;//绘制各标识点的画笔
    private Paint bgPaint;//背景画笔
    private Paint pathAPaint;//绘制A区域画笔
    private Paint pathBPaint;//绘制B区域画笔
    private Paint pathCPaint;//绘制C区域画笔

    private MyPoint a, f, g, e, h, c, j, b, k, d, i;
    private Path pathA;
    private Path pathB;
    private Path pathC;

    private Bitmap bitmap;//缓存bitmap
    private Canvas bitmapCanvas;

    private int defaultWidth;//默认宽度
    private int defaultHeight;//默认高度

    private int viewWidth;
    private int viewHeight;
    private String mode = STYLE_LOWER_RIGHT;
    public static final String STYLE_TOP_RIGHT = "STYLE_TOP_RIGHT";//f点在右上角
    public static final String STYLE_LOWER_RIGHT = "STYLE_LOWER_RIGHT";//f点在右下角

    public FlipPage(Context context) {
        this(context, null);
    }

    public FlipPage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        defaultWidth = 600;
        defaultHeight = 1000;

        a = new MyPoint();
        f = new MyPoint();
        g = new MyPoint();
        e = new MyPoint();
        h = new MyPoint();
        c = new MyPoint();
        j = new MyPoint();
        b = new MyPoint();
        k = new MyPoint();
        d = new MyPoint();
        i = new MyPoint();

        pointPaint = new Paint();
        pointPaint.setColor(Color.RED);
        pointPaint.setTextSize(25);
        pointPaint.setStyle(Paint.Style.STROKE);

        bgPaint = new Paint();
        bgPaint.setColor(Color.GREEN);
        //书页正面背景
        pathAPaint = new Paint();
        pathAPaint.setColor(Color.WHITE);
        pathAPaint.setAntiAlias(true);//设置抗锯齿

        pathBPaint = new Paint();
        pathBPaint.setColor(getResources().getColor(R.color.blue_light));
//        pathBPaint.setColor(0x00fff00f);
        pathBPaint.setAntiAlias(true);//设置抗锯齿
        pathBPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));

        //书页背面
        pathCPaint = new Paint();
        pathCPaint.setColor(Color.YELLOW);
        pathCPaint.setAntiAlias(true);//设置抗锯齿
        pathCPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
//        pathCPaint.setStyle(Paint.Style.STROKE);

        pathA = new Path();
        pathB = new Path();
        pathC = new Path();


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = measureSize(defaultHeight, heightMeasureSpec);
        int width = measureSize(defaultWidth, widthMeasureSpec);
        setMeasuredDimension(width, height);
        viewWidth = width;
        viewHeight = height;
        a.x = -1;
        a.y = -1;
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {


        super.onDraw(canvas);
        bitmap = Bitmap.createBitmap(getDisplayWidth(), getDisplayHeight(),
                Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        if (a.x == -1 && a.y == -1) {
            bitmapCanvas.drawPath(getPathDefault(), pathAPaint);
        } else {
            if (f.x == viewWidth && f.y == 0) {
                bitmapCanvas.drawPath(getPathAFromTopRight(), pathAPaint);
            } else if (f.x == viewWidth && f.y == viewHeight) {
                bitmapCanvas.drawPath(getPathAFromLowerRight(), pathAPaint);
            }
            bitmapCanvas.drawPath(getPathC(), pathCPaint);
//            bitmapCanvas.drawPath(getPathB(), pathBPaint);
        }
        canvas.drawBitmap(bitmap, 0, 0, null);
    }

    /**
     * 设置触摸点
     *
     * @param x
     * @param y
     * @param style
     */
    public void setTouchPoint(float x, float y, String style) {
        switch (style) {
            case STYLE_TOP_RIGHT:
                f.x = viewWidth;
                f.y = 0;
                break;
            case STYLE_LOWER_RIGHT:
                f.x = viewWidth;
                f.y = viewHeight;
                break;
            default:
                break;
        }
        //如果大于0则设置a点坐标重新计算各标识点位置，否则a点坐标不变
        MyPoint touchPoint = new MyPoint(x, y);
//        if (calcPointCX(touchPoint, f) > 0) {
        a.x = x;
        a.y = y;
        calcPointsXY(a, f);
//        } else {
//            calcPointsXY(a, f);
//        }
        postInvalidate();
    }

    /**
     * 回到默认状态
     */
    public void setDefaultPath() {
        a.x = -1;
        a.y = -1;
        postInvalidate();
    }

    /**
     * 绘制默认的界面
     *
     * @return
     */
    private Path getPathDefault() {
        pathA.reset();
        pathA.lineTo(0, viewHeight);
        pathA.lineTo(viewWidth, viewHeight);
        pathA.lineTo(viewWidth, 0);
        pathA.close();
        return pathA;
    }

    /**
     * 获取f点在右上角的pathA
     *
     * @return
     */
    private Path getPathAFromTopRight() {
        pathA.reset();
        pathA.lineTo(c.x, c.y);//移动到c点
        pathA.quadTo(e.x, e.y, b.x, b.y);//从c到b画贝塞尔曲线，控制点为e
        pathA.lineTo(a.x, a.y);//移动到a点
        pathA.lineTo(k.x, k.y);//移动到k点
        pathA.quadTo(h.x, h.y, j.x, j.y);//从k到j画贝塞尔曲线，控制点为h
        pathA.lineTo(viewWidth, viewHeight);//移动到右下角
        pathA.lineTo(0, viewHeight);//移动到左下角
        pathA.close();
        return pathA;
    }

    /**
     * 获取f点在右下角的pathA
     *
     * @return
     */
    private Path getPathAFromLowerRight() {
        pathA.reset();
        pathA.lineTo(0, viewHeight);//移动到左下角
        pathA.lineTo(c.x, c.y);//移动到c点
        pathA.quadTo(e.x, e.y, b.x, b.y);//从c到b画贝塞尔曲线，控制点为e
        pathA.lineTo(a.x, a.y);//移动到a点
        pathA.lineTo(k.x, k.y);//移动到k点
        pathA.quadTo(h.x, h.y, j.x, j.y);//从k到j画贝塞尔曲线，控制点为h
        pathA.lineTo(viewWidth, 0);//移动到右上角
        pathA.close();//闭合区域
        return pathA;
    }

    /**
     * 绘制区域B
     *
     * @return
     */
    private Path getPathB() {
        pathB.reset();
        pathB.lineTo(0, viewHeight);//移动到左下角
        pathB.lineTo(viewWidth, viewHeight);//移动到右下角
        pathB.lineTo(viewWidth, 0);//移动到右上角
        pathB.close();//闭合区域
        return pathB;
    }

    /**
     * 绘制区域C
     *
     * @return
     */
    private Path getPathC() {
        pathC.reset();
        pathC.moveTo(i.x, i.y);//移动到i点
        pathC.lineTo(d.x, d.y);//移动到d点
        pathC.lineTo(b.x, b.y);//移动到b点
        pathC.lineTo(a.x, a.y);//移动到a点
        pathC.lineTo(k.x, k.y);//移动到k点
        pathC.close();//闭合区域
        return pathC;
    }

    /**
     * 计算各点坐标
     *
     * @param a
     * @param f
     */
    private void calcPointsXY(MyPoint a, MyPoint f) {
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        h.x = f.x;
        h.y = g.y - (f.x - g.x) * (f.x - g.x) / (f.y - g.y);

        c.x = e.x - (f.x - e.x) / 2;
        c.y = f.y;

        j.x = f.x;
        j.y = h.y - (f.y - h.y) / 2;

        b = getIntersectionPoint(a, e, c, j);
        k = getIntersectionPoint(a, h, c, j);

        d.x = (c.x + 2 * e.x + b.x) / 4;
        d.y = (2 * e.y + c.y + b.y) / 4;

        i.x = (j.x + 2 * h.x + k.x) / 4;
        i.y = (2 * h.y + j.y + k.y) / 4;
    }

    /**
     * 计算两线段相交点坐标
     *
     * @param lineOne_My_pointOne
     * @param lineOne_My_pointTwo
     * @param lineTwo_My_pointOne
     * @param lineTwo_My_pointTwo
     * @return 返回该点
     */
    private MyPoint getIntersectionPoint(MyPoint lineOne_My_pointOne, MyPoint lineOne_My_pointTwo, MyPoint lineTwo_My_pointOne, MyPoint lineTwo_My_pointTwo) {
        float x1, y1, x2, y2, x3, y3, x4, y4;
        x1 = lineOne_My_pointOne.x;
        y1 = lineOne_My_pointOne.y;
        x2 = lineOne_My_pointTwo.x;
        y2 = lineOne_My_pointTwo.y;
        x3 = lineTwo_My_pointOne.x;
        y3 = lineTwo_My_pointOne.y;
        x4 = lineTwo_My_pointTwo.x;
        y4 = lineTwo_My_pointTwo.y;
        float pointX = ((x1 - x2) * (x3 * y4 - x4 * y3) - (x3 - x4) * (x1 * y2 - x2 * y1))
                / ((x3 - x4) * (y1 - y2) - (x1 - x2) * (y3 - y4));
        float pointY = ((y1 - y2) * (x3 * y4 - x4 * y3) - (x1 * y2 - x2 * y1) * (y3 - y4))
                / ((y1 - y2) * (x3 - x4) - (x1 - x2) * (y3 - y4));
        return new MyPoint(pointX, pointY);
    }

    /**
     * 计算C点的X值
     *
     * @param a
     * @param f
     * @return
     */
    private float calcPointCX(MyPoint a, MyPoint f) {
        MyPoint g, e;
        g = new MyPoint();
        e = new MyPoint();
        g.x = (a.x + f.x) / 2;
        g.y = (a.y + f.y) / 2;

        e.x = g.x - (f.y - g.y) * (f.y - g.y) / (f.x - g.x);
        e.y = f.y;

        return e.x - (f.x - e.x) / 2;
    }


    @Override
    public void currentToPrevious(float changeX, float moveY, float moveX) {
        if (mode == null || mode.isEmpty()) {
            mode = moveY > getDisplayHeight() / 2 ? STYLE_LOWER_RIGHT :
                    STYLE_TOP_RIGHT;
        }
        setTouchPoint(moveX, moveY, mode);
    }

    @Override
    public void currentToNext(float changeX, float moveY, float moveX) {
        setX(0);
        setY(0);
    }

    @Override
    public void previousToCurrent(float changeX, float moveY, float moveX) {
        if (mode == null || mode.isEmpty()) {
            mode = moveY > getDisplayHeight() / 2 ? STYLE_LOWER_RIGHT :
                    STYLE_TOP_RIGHT;
        }
        setTouchPoint(moveX, moveY, mode);
    }

    @Override
    public void nextToCurrent(float changeX, float moveY, float moveX) {
        setX(0);
        setY(0);
    }

    @Override
    public ValueAnimator animCurrentToPrevious(float changeX, final float moveY, float moveX) {
        ValueAnimator animator = ValueAnimator.ofFloat(moveX, 0 - getDisplayWidth());
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curValue = (float) animation.getAnimatedValue();
                Log.e("aaa", curValue + "@");
                FlipPage.this.setTouchPoint(curValue, moveY, mode);
            }
        });
        return animator;
    }

    @Override
    public ValueAnimator animCurrentToNext(float changeX, final float moveY, float moveX) {
        return ObjectAnimator.ofFloat(this, "translationX", 0, 0);
    }

    @Override
    public ValueAnimator animPreviousToCurrent(float changeX, final float moveY, float moveX) {
        ValueAnimator animator = ValueAnimator.ofFloat(moveX, getDisplayWidth());
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float curValue = (float) animation.getAnimatedValue();
                Log.e("curValue", curValue + "!");
                FlipPage.this.setTouchPoint(curValue, moveY, mode);
            }
        });
        return animator;
    }

    @Override
    public ValueAnimator animNextToCurrent(float changeX, float moveY, float moveX) {
        return ObjectAnimator.ofFloat(this, "translationX", 0, 0);

    }

    @Override
    public void reset() {
        setDefaultPath();
    }

    private class MyPoint {
        float x, y;

        MyPoint() {
        }

        MyPoint(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}
