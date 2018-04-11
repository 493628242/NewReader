package com.gray.reader;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.gray.reader.element.BigTitleElement;
import com.gray.reader.element.BottomElement;
import com.gray.reader.element.Element;
import com.gray.reader.element.LineElement;
import com.gray.reader.element.SmallTitleElement;
import com.gray.reader.page.BasePage;
import com.gray.reader.page.IReaderPage;
import com.gray.reader.util.UIUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * @author wjy on 2018/4/6.
 */
public class ReaderLayout extends FrameLayout {
    //起始点击位置
    private float downX;
    //移动距离X
    private float mMoveX;
    //移动距离Y
    private float mMoveY;

    private final float minMove = 50f;
    //用于存放书籍页面
    private LinkedList<IReaderPage> pages;

    private IReaderPage currentPage;
    private IReaderPage previousPage;
    private IReaderPage nextPage;
    //当前页数
    private volatile int index;
    private volatile int maxIndex;
    private ReaderAdapter adapter;
    private boolean animStatus1 = false;
    private boolean animStatus2 = false;
    private Paint paint;
    private PageData pageData;
    private pageProperty pageProperty;

    public ReaderLayout(@NonNull Context context) {
        this(context, null);
    }

    public ReaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ReaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        pages = new LinkedList<>();
        init();
    }

    public void setAdapter(ReaderAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("index", index + "-");
        if (!animStatus1 && !animStatus2) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = event.getX();
                    mMoveX = 0;
                    Log.e("downX", downX + "_");
                    return true;
                case MotionEvent.ACTION_MOVE:
                    mMoveX = event.getX();
                    mMoveY = event.getY();
                    childMove(mMoveX - downX, mMoveY);
                    return true;
                case MotionEvent.ACTION_UP:
                    float changeX = mMoveX - downX;
                    if (mMoveX != 0 && Math.abs(changeX) > minMove) {
                        if (changeX < 0) {
                            moveToNext(changeX, mMoveY, mMoveX);
                        } else {
                            moveToPrevious(changeX, mMoveY, mMoveX);
                        }
                    } else {
                        int widthPer3 = getDisplayWidth() / 3;
                        if (downX < widthPer3) {
                            Log.e("AA", "上翻页");
                            moveToPrevious(changeX, mMoveY, mMoveX);
                            return true;
                        } else if (downX > 2 * widthPer3) {
                            Log.e("AA", "下翻页");
                            moveToNext(changeX, mMoveY, mMoveX);
                            return true;
                        } else {
                            currentPage.reset();
                            return performClick();
                        }
                    }
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    //翻到前一页
    private synchronized void moveToPrevious(float moveX, float moveY, float mMoveX) {
        Log.e("moveToPrevious", "前一页");
        if (index == 0) {
            return;
        }
        addPreviousToParent(moveX, previousPage);
        ValueAnimator objectAnimator = currentPage.animCurrentToNext(moveX, moveY, mMoveX);
        if (objectAnimator != null) {
            objectAnimator.start();
        }
        ValueAnimator objectAnimator1 = previousPage.animPreviousToCurrent(moveX, moveY, mMoveX);
        if (objectAnimator1 != null) {
            objectAnimator1.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animStatus1 = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    afterToPrevious();
                    animStatus1 = false;

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator1.start();
        }
    }

    //翻到下一页
    private synchronized void moveToNext(float moveX, float moveY, float mMoveX) {
        Log.e("moveToNext", "下一页");
        //是否还有下一页
        if (pages.size() == 1) {
            return;
        }
        addNextToParent(moveX, nextPage);
        ValueAnimator objectAnimator = currentPage.animCurrentToPrevious(moveX, moveY, mMoveX);
        if (objectAnimator != null) {
            objectAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    animStatus2 = true;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    afterToNext();
                    animStatus2 = false;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            objectAnimator.start();
        }
        ValueAnimator objectAnimator1 = nextPage.animNextToCurrent(moveX, moveY, mMoveX);
        if (objectAnimator1 != null) {
            objectAnimator1.start();
        } else {

        }
    }

    private synchronized void childMove(float moveX, float moveY) {
        if (pages.isEmpty()) {
            return;
        }
        if (moveX > 0) {
            if (index == 0) {
                return;
            }
            addPreviousToParent(moveX, previousPage);
            currentPage.currentToNext(moveX, moveY, mMoveX);
            previousPage.previousToCurrent(moveX, moveY, mMoveX);
        } else {
            if (pages.size() == 1) {
                return;
            }
            addNextToParent(moveX, nextPage);
            currentPage.currentToPrevious(moveX, moveY, mMoveX);
            nextPage.nextToCurrent(moveX, moveY, mMoveX);
        }
    }

    //添加页面进入parent
    private void addPreviousToParent(float moveX, final IReaderPage readerPage) {
        if (indexOfChild((View) readerPage) == -1) {
            addView((View) readerPage, (int) (0 - getDisplayWidth() + moveX), 0);
            ((View) readerPage).getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            adapter.loadPrevious((BasePage) readerPage, index - 1);
                            ((View) readerPage).getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);
                        }
                    });
        }
    }

    //添加页面进入parent
    private void addNextToParent(float moveX, final IReaderPage readerPage) {
        if (indexOfChild((View) readerPage) == -1) {
            addView((View) readerPage, (int) (getDisplayWidth() + moveX), 0);
            ((View) readerPage).getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            adapter.loadNext((BasePage) readerPage, index + 1);
                            ((View) readerPage).getViewTreeObserver()
                                    .removeOnGlobalLayoutListener(this);

                        }
                    });
        }
    }

    private synchronized void afterToPrevious() {
        //重新确认页面位置
        pages.add(0, previousPage);
        previousPage = pages.removeLast();
        currentPage = pages.getFirst();
        nextPage = pages.getLast();
        previousPage.reset();
        nextPage.reset();
        currentPage.reset();
        resetView();
        --index;
    }

    private synchronized void afterToNext() {
        //重新确认页面位置
        pages.add(previousPage);
        previousPage = pages.removeFirst();
        currentPage = pages.getFirst();
        nextPage = pages.getLast();
        previousPage.reset();
        nextPage.reset();
        currentPage.reset();
        resetView();
        ++index;
    }

    private void resetView() {
        removeView((View) previousPage);
        removeView((View) nextPage);
        removeView((View) currentPage);
        addView((View) currentPage, 0, 0);
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        pageProperty = new pageProperty();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    //传入文本数据
    public void setData(String word, String title, String author) {
        pageData = new PageData(word, title, author);
        resetPaint();
        maxIndex = pageData.getCount();
    }

    public void setPower(int level) {
        pageProperty.power = level / 100;
        postInvalidate();
    }

    private void resetPaint() {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    //创建页面
    public void setPage(Class clazz) {
        if (!IReaderPage.class.isAssignableFrom(clazz)) {
            throw new IllegalArgumentException("class需要继承IReaderPage");
        }
        try {
            Constructor constructor = clazz.getConstructor(Context.class);
            for (int i = 0; i < 2; ++i) {
                IReaderPage IReaderPage = (IReaderPage) constructor.newInstance(getContext());
                pages.add(0, IReaderPage);
            }
            previousPage = (IReaderPage) constructor.newInstance(getContext());
            ((View) previousPage).setBackgroundColor(Color.TRANSPARENT);
            addView((View) previousPage, 0 - getDisplayWidth(), 0);
            nextPage = pages.getLast();
            ((View) nextPage).setBackgroundColor(Color.TRANSPARENT);
            addView((View) nextPage, getDisplayWidth(), 0);
            currentPage = pages.getFirst();
            ((View) currentPage).setBackgroundColor(Color.TRANSPARENT);
            addView((View) currentPage);
            //加载第一章数据
            ((View) currentPage).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (pageData != null) {
                        ArrayList<Element> page = pageData.getPage(index);

//                    adapter.loadFirst((BasePage) currentPage, index);
                        ((WriteView) currentPage).setIndex(index);
                        ((WriteView) currentPage).setCount(pageData.getCount());
                        ((WriteView) currentPage).setPage(page);
                        ((WriteView) currentPage).setPower(pageProperty.power);
                        ((WriteView) currentPage).notifyData();
                    }
                    ((View) currentPage).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

        } catch (NoSuchMethodException
                | InstantiationException
                | InvocationTargetException
                | IllegalAccessException e) {
            Log.e("Error", Arrays.toString(e.getStackTrace()));
        }
    }

    class PageData {
        private String word;
        private String title;
        private String author;
        private ArrayList<ArrayList<Element>> elementList;
        private boolean calOver;
        private boolean pagOver;
        private String unCal;
        private ArrayList<Integer> pageNum;

        PageData(String word, String title, String author) {
            this.word = word;
            this.title = title;
            this.author = author;
            int i = word.lastIndexOf("\n");
            if (i != word.length() - 1) {
                word += "\n";
            }
            unCal = word;
            elementList = new ArrayList<>();
            pageNum = new ArrayList<>();
            paging();
        }

        //进行分页操作
        void paging() {
            Rect rect = new Rect();
            while (!calOver) {
                ArrayList<Element> elements = new ArrayList<>();
                //已经使用的高度
                int hasUsedHeight = UIUtils.dip2px(getContext(), Element.PADDING_TOP);
                //小标题
                SmallTitleElement smallTitleElement = getSmallTitleElement();
                elements.add(smallTitleElement);
                String smallTitleUsefulString = smallTitleElement.getTitle();
                paint.getTextBounds(smallTitleElement.getTitle(), 0, smallTitleUsefulString.length(),
                        rect);
                smallTitleElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
                smallTitleElement.setY(hasUsedHeight + rect.height());
                hasUsedHeight += rect.height() + UIUtils.dip2px(getContext(), 18);
                //大标题
                if (elementList.isEmpty()) {
                    paint.setTextSize(pageProperty.bigTitleSize);
                    BigTitleElement bigTitleElement = new BigTitleElement(getContext(),
                            getBigTitleUsefulString(title));
                    elements.add(bigTitleElement);
                    String bigTitleUsefulString = getBigTitleUsefulString(title);
                    String[] split = bigTitleUsefulString.split("\n");
                    paint.setTextSize(pageProperty.bigTitleSize);
                    paint.getTextBounds(title, 0, title.length(), rect);
                    bigTitleElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
                    bigTitleElement.setY(UIUtils.dip2px(getContext(), 26)
                            + hasUsedHeight + rect.height());
                    //已占用高度
                    hasUsedHeight += UIUtils.dip2px(getContext(), 26) + rect.height() * split.length
                            + pageProperty.bigTitleLineSpace
                            * (split.length - 1) + UIUtils.dip2px(getContext(), 40);
                }
                //正文内容
                if ((unCal != null && !unCal.isEmpty())
                        || (author != null && !author.isEmpty())) {
                    //正文已占据空间
                    int contentHeight = 0;
                    //正文可用空间
                    int lastHeight = UIUtils.getDisplayHeight(getContext()) - hasUsedHeight
                            - UIUtils.dip2px(getContext(), 38);
                    paint.setTextSize(pageProperty.textSize);
                    String[] split = unCal.split("\n");//分段

                    ArrayList<LineElement> lineElements = new ArrayList<>();//临时空间用于储存行
                    int wordNum = 0;
                    for (String aSplit : split) {
                        String paragraph = aSplit;
                        boolean pageOver = false;
                        while (true) {
                            hasUsedHeight += pageProperty.lineSpace;
                            contentHeight += pageProperty.lineSpace;
                            String lineUsefulString = getLineUsefulString(paragraph);
                            LineElement lineElement = new LineElement(getContext(), lineUsefulString);
                            paint.getTextBounds(lineUsefulString, 0,
                                    lineUsefulString.length(), rect);
                            hasUsedHeight += rect.height();
                            lineElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
                            lineElement.setY(hasUsedHeight);
                            contentHeight += rect.height();
                            if (contentHeight > lastHeight) {
                                pageOver = true;
                                break;
                            } else {
                                lineElements.add(lineElement);
                                wordNum += lineUsefulString.length();
                            }
                            paragraph = paragraph.substring(lineUsefulString.length(), paragraph.length());
                            if (paragraph.isEmpty()) {
                                break;
                            }
                        }
                        //在计算完一页后取数以计算的字数
                        if (pageOver) {
                            break;
                        }
                        wordNum++;//加上换行符
                    }
                    //如果有作者的话应当添加
//                    int authorHeight = UIUtils.getDisplayHeight(getContext()) - marginTop
//                            - UIUtils.dip2px(getContext(), 38) - contentHeight;
//                    if () {
//
//                    }
                    pageNum.add(wordNum);
                    unCal = unCal.substring(wordNum, unCal.length());
                    elements.addAll(lineElements);
                }
                elementList.add(elements);
                if (unCal == null || unCal.isEmpty()) {
                    calOver = true;
                }
            }
            //分页完成后统一添加底部
            int count = elementList.size();

            for (ArrayList<Element> elements : elementList) {
                BottomElement bottomElement = new BottomElement(getContext());
                bottomElement.setIndex(index + "/" + count);
                bottomElement.setPower(pageProperty.power);
                elements.add(bottomElement);
            }
        }

        //获取小标题的Element
        @NonNull
        private SmallTitleElement getSmallTitleElement() {
            paint.setTextSize(pageProperty.smallTitleSize);
            return new SmallTitleElement(getContext(), getSmallTitleUsefulString(title));
        }

        //获取某一页面
        ArrayList<Element> getPage(int index) {
            return elementList.get(index - 1);
        }

        int getCount() {
            return elementList.size();
        }

//        private int getContentMarginTop(String title, boolean isFirst) {
//            paint.setTextSize(pageProperty.smallTitleSize);
//            Rect rect1 = new Rect();
//            paint.getTextBounds(title, 0, title.length(), rect1);
//            int marginTop = UIUtils.dip2px(getContext(), Element.PADDING_TOP)
//                    + rect1.height() + UIUtils.dip2px(getContext(), 18);
//            if (isFirst) {
//                String bigTitleUsefulString = getBigTitleUsefulString(title);
//                String[] split = bigTitleUsefulString.split("\n");
//                paint.setTextSize(pageProperty.bigTitleSize);
//                paint.getTextBounds(title, 0, title.length(), rect1);
//                marginTop += UIUtils.dip2px(getContext(), 26) + rect1.height() * split.length
//                        + pageProperty.bigTitleLineSpace
//                        * (split.length - 1) + UIUtils.dip2px(getContext(), 40);
//            }
//            return marginTop;
//        }

        private String getSmallTitleUsefulString(String string) {
            int usefulWidth = UIUtils.getDisplayWidth(getContext())
                    - UIUtils.dip2px(getContext(), Element.PADDING_START)
                    - UIUtils.dip2px(getContext(), Element.PADDING_END);
            String usefulString;
            int length = string.length();
            if (paint.measureText(string) <= usefulWidth) {
                return string;
            }
            while ((paint.measureText(string.substring(0, length)
                    + paint.measureText("...")) > usefulWidth)) {
                --length;
            }
            usefulString = string.substring(0, length) + "...";
            return usefulString;
        }

        private String getBigTitleUsefulString(String string) {
            //获取屏幕可用宽度
            int usefulWidth = UIUtils.getDisplayWidth(getContext())
                    - UIUtils.dip2px(getContext(), Element.PADDING_START)
                    - UIUtils.dip2px(getContext(), Element.PADDING_END);
            StringBuilder usefulString = new StringBuilder();
            int length;
            if (paint.measureText(string) <= usefulWidth) {
                return string;
            }
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i != string.length(); i = length) {
                length = string.length();
                while ((paint.measureText(string.substring(i, length)) > usefulWidth)) {
                    --length;
                }
                strings.add(string.substring(i, length));
            }
            for (String s : strings) {
                usefulString.append(s).append("\n");
            }
            return usefulString.toString();
        }

        private String getLineUsefulString(String string) {
            int usefulWidth = UIUtils.getDisplayWidth(getContext())
                    - UIUtils.dip2px(getContext(), Element.PADDING_START)
                    - UIUtils.dip2px(getContext(), Element.PADDING_END);
            String usefulString;
            int length = string.length();
            while ((paint.measureText(string.substring(0, length)) > usefulWidth)) {
                --length;
            }
            usefulString = string.substring(0, length);
            return usefulString;
        }

    }

    class pageProperty {
        int textSize = UIUtils.dip2px(getContext(), LineElement.DEF_TEXT_SIZE);
        int lineSpace = UIUtils.dip2px(getContext(), LineElement.DEF_TEXT_SIZE);
        int smallTitleSize = UIUtils.dip2px(getContext(), SmallTitleElement.DEF_TEXT_SIZE);
        int bigTitleSize = UIUtils.dip2px(getContext(), BigTitleElement.DEF_TEXT_SIZE);
        int bigTitleLineSpace = UIUtils.dip2px(getContext(), BigTitleElement.DEF_LINE_SPACE);
        int textColor = LineElement.DEF_TEXT_COLOR;
        int bgColor = Color.WHITE;
        int bottomTextSize = UIUtils.dip2px(getContext(), BottomElement.DEF_TEXT_SIZE);
        float power = 0.5f;
    }

    private int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int getDisplayHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}
