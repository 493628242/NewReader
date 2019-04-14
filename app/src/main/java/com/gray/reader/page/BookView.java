package com.gray.reader.page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.gray.reader.element.NumElement;
import com.gray.reader.flip.IFlip;

/**
 * @author wjy on 2019/3/21.
 */
public class BookView extends View {

    private Bitmap mCurrentBitmap; //位置位于第一
    private Bitmap mNextBitmap; //第二
    private Bitmap mPrevBitmap; //第三
    private Canvas mCanvas;  //用于对bitmap进行绘制

    private int mWidth = -1; //宽
    private int mHeight = -1;//高
    //二分之一
    private int mHalfWidth = -1;
    private int mHalfHeight = -1;
    //三分之一
    private int mThirdWidth = -1;
    private int mThirdHeight = -1;

    private int mMinScroll = 200;//触发翻页动画的最小像素
    public static final int PRESS_MENU = 0;//位置在View中间的，menu区域
    public static final int PRESS_NEXT = 1;//下一页
    public static final int PRESS_PREV = 2;//前一页
    private int mPressState;
    private boolean isScrolling = false;
    private IFlip mFlipModel;//翻页模式设置
    private float mDownX = -1;//手指落下坐标x
    private float mDownY = -1;//手指落下坐标y
    private Paint mPaint;


    public void setFlipModel(IFlip flipModel) {
        this.mFlipModel = flipModel;
    }

    public BookView(Context context) {
        this(context, null);
    }

    public BookView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BookView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    //初始化
    private void init() {
        mCanvas = new Canvas();
        mPaint = new Paint();
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCurrentBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        mNextBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        mPrevBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.RGB_565);
        initWH(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        switch (mPressState) {
            case PRESS_NEXT:
                if (index >= 100) {
                    return;
                }
                mFlipModel.flipToNext(mCurrentBitmap, mNextBitmap, canvas, 0, 0);
                Bitmap bitmap = mCurrentBitmap;
                mCurrentBitmap = mNextBitmap;
                mNextBitmap = mPrevBitmap;
                mPrevBitmap = bitmap;
                index++;
                drawNum(mPrevBitmap, index + 1);
                break;
            case PRESS_PREV:
                if (index == 0) {
                    return;
                }
                mFlipModel.flipToPrev(mCurrentBitmap, mPrevBitmap, canvas, 0, 0);
                Bitmap temp = mCurrentBitmap;
                mCurrentBitmap = mPrevBitmap;
                mPrevBitmap = mNextBitmap;
                mNextBitmap = temp;
                index--;
                drawNum(mPrevBitmap, index - 1);
                break;
            default:
                canvas.drawBitmap(mCurrentBitmap, 0, 0, mPaint);
        }
    }

    /**
     * 设置宽高属性
     *
     * @param w 宽
     * @param h 高
     */
    private void initWH(int w, int h) {
        Log.e("w&h", w + "\n" + h);
        mWidth = w;
        mHeight = h;
        mHalfWidth = mWidth >> 1;
        mHalfHeight = mHeight >> 1;
        mThirdHeight = mHeight / 3;
        mThirdWidth = mWidth / 3;
        drawNum(mCurrentBitmap, index);
        drawNum(mNextBitmap, index + 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.e("ACTION_MOVE", "move");
                //只要出现滑动则isScrolling置为true代表正在滑动
                isScrolling = true;
                break;
            case MotionEvent.ACTION_UP:
                mPressState = getPressStates(event.getX(), event.getY());
                if (!isScrolling
                        && mPressState == PRESS_MENU) {
                    //代表点击位置是在菜单区域
                    performClick();
                } else {
                    //点击区域是在外围,进行翻页操作
                    if (!isScrolling) {
                        postInvalidate();
                    } else {
                        switch (mFlipModel.getFlipDirection()) {
                            case Horizontal://水平方向看x
                                Log.e("up", Math.abs(event.getX() - mDownX) + "!");
                                if (event.getX() > mDownX) {
                                    mPressState = PRESS_PREV;
                                } else {
                                    mPressState = PRESS_NEXT;
                                }
                                break;
                            case vertical://竖直方向看y
                                Log.e("up", Math.abs(event.getY() - mDownY) + "!");
                                if (event.getY() > mDownY) {
                                    mPressState = PRESS_PREV;
                                } else {
                                    mPressState = PRESS_NEXT;
                                }
                                break;
                        }
                        postInvalidate();
                    }
                }
                isScrolling = false;
                break;
        }

        return true;
    }

    /**
     * 是否位置在menu区域
     */
    private int getPressStates(float x, float y) {
        if (x > mHalfWidth && y > mHalfHeight
                && x < mThirdWidth << 1 && y < mThirdHeight << 1) {
            return PRESS_MENU;
        }
        if (mFlipModel == null) {
            throw new RuntimeException("需要设置翻页模式");
        }
        switch (mFlipModel.getFlipDirection()) {
            case vertical:
                if (y > mHalfHeight) {
                    return PRESS_NEXT;
                } else {
                    return PRESS_PREV;
                }
            case Horizontal:
            default:
                if (x > mHalfWidth) {
                    return PRESS_NEXT;
                } else {
                    return PRESS_PREV;
                }
        }
    }


    //text
    int index = 0;

    private void drawNum(Bitmap bitmap, int index) {
        mCanvas.setBitmap(bitmap);
        mCanvas.drawColor(index % 2 == 0 ? Color.YELLOW : Color.GREEN);
        mPaint.setColor(Color.BLACK);
        mPaint.setTextSize(50);
        mCanvas.drawText(String.valueOf(index), mHalfWidth, mHalfHeight, mPaint);
    }
}
