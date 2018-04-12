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

import com.gray.reader.element.AuthorHeadElement;
import com.gray.reader.element.BigTitleElement;
import com.gray.reader.element.BottomElement;
import com.gray.reader.element.Element;
import com.gray.reader.element.LineElement;
import com.gray.reader.element.SmallTitleElement;
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
    private volatile int index = 1;
    private volatile int maxIndex;
    private ReaderAdapter adapter;
    private boolean animStatus1 = false;
    private boolean animStatus2 = false;
    private Paint paint;
    private PageData pageData;
    private pageProperty pageProperty;
    private PageAnimController animController;

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_PROTECT_EYE = 1;
    public static final int TYPE_GREEN = 2;
    public static final int TYPE_PINK = 3;
    public static final int TYPE_BlACK = 4;

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

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        pageProperty = new pageProperty();
        animController = new PageAnimController();
    }

//    public void setAdapter(ReaderAdapter adapter) {
//        this.adapter = adapter;
//    }

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
                    animController.childMove(mMoveX - downX, mMoveY, mMoveX);
                    return true;
                case MotionEvent.ACTION_UP:
                    float changeX = mMoveX - downX;
                    if (mMoveX != 0 && Math.abs(changeX) > minMove) {
                        if (changeX < 0) {
                            animController.moveToNext(changeX, mMoveY, mMoveX);
                        } else {
                            animController.moveToPrevious(changeX, mMoveY, mMoveX);
                        }
                    } else {
                        int widthPer3 = getDisplayWidth() / 3;
                        if (downX < widthPer3) {
                            Log.e("AA", "上翻页");
                            animController.moveToPrevious(changeX, mMoveY, mMoveX);
                            return true;
                        } else if (downX > 2 * widthPer3) {
                            Log.e("AA", "下翻页");
                            animController.moveToNext(changeX, mMoveY, mMoveX);
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
        pageProperty.power = ((float) level / 100);
        ((WriteView) currentPage).setPower(pageProperty.power);
        ((WriteView) currentPage).notifyData();
        ((WriteView) previousPage).setPower(pageProperty.power);
        ((WriteView) previousPage).notifyData();
        ((WriteView) nextPage).setPower(pageProperty.power);
        ((WriteView) nextPage).notifyData();
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
            nextPage = pages.getLast();
            ((View) nextPage).setBackgroundColor(Color.TRANSPARENT);
            currentPage = pages.getFirst();
            ((View) currentPage).setBackgroundColor(Color.TRANSPARENT);
            ((View) previousPage).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (pageData != null && index > 1) {
                        ArrayList<Element> page = pageData.getPage(index - 1);
                        ((WriteView) previousPage).setIndex(index - 1);
                        ((WriteView) previousPage).setCount(pageData.getCount());
                        ((WriteView) previousPage).setPage(page);
                        ((WriteView) previousPage).setPower(pageProperty.power);
                        ((WriteView) previousPage).notifyData();
                    }
                    ((View) previousPage).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
            //加载第一章数据
            ((View) currentPage).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (pageData != null) {
                        ArrayList<Element> page = pageData.getPage(index);

                        ((WriteView) currentPage).setIndex(index);
                        ((WriteView) currentPage).setCount(pageData.getCount());
                        ((WriteView) currentPage).setPage(page);
                        ((WriteView) currentPage).setPower(pageProperty.power);
                        ((WriteView) currentPage).notifyData();
                    }
                    ((View) currentPage).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
            ((View) nextPage).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (pageData != null && pageData.getCount() > index) {
                        ArrayList<Element> page = pageData.getPage(index + 1);
                        ((WriteView) nextPage).setIndex(index + 1);
                        ((WriteView) nextPage).setCount(pageData.getCount());
                        ((WriteView) nextPage).setPage(page);
                        ((WriteView) nextPage).setPower(pageProperty.power);
                        ((WriteView) nextPage).notifyData();
                    }
                    ((View) nextPage).getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
            addView((View) previousPage);
            addView((View) nextPage);
            addView((View) currentPage);
        } catch (NoSuchMethodException
                | InstantiationException
                | InvocationTargetException
                | IllegalAccessException e) {
            Log.e("Error", Arrays.toString(e.getStackTrace()));
        }
    }

    public void chooseMode(int mode) {
        switch (mode) {
            case TYPE_NORMAL:
                pageProperty.setNormal();
                break;
            case TYPE_PROTECT_EYE:
                pageProperty.setProtectEye();
                break;
            case TYPE_GREEN:
                pageProperty.setGreen();
                break;
            case TYPE_PINK:
                pageProperty.setPink();
                break;
            case TYPE_BlACK:
                pageProperty.setBlack();
                break;
        }
        pageData.rePaging();
        ((View) currentPage).postInvalidate();
        ((View) nextPage).postInvalidate();
        ((View) nextPage).postInvalidate();
    }

    class PageData {
        private String word;
        private String title;
        private String author;
        private ArrayList<ArrayList<Element>> elementList;
        private boolean calOver;
        private String unCal;
        private String unCalAuthor;
        boolean authorOver = false;
        private ArrayList<Integer> pageNum;

        PageData(String word, String title, String author) {
            this.word = word;
            this.title = title;
            this.author = author;
            Log.e("aaa", author);
            Log.e("aaaa", word);
            int i = word.lastIndexOf("\n");
            if (i != word.length() - 1) {
                word += "\n";
            }
            int j = author.lastIndexOf("\n");
            if (j != author.length() - 1) {
                author += "\n";
            }
            unCal = word;
            unCalAuthor = author;
            elementList = new ArrayList<>();
            pageNum = new ArrayList<>();
            paging();
        }

        //进行分页操作
        void paging() {
            Rect rect = new Rect();
            boolean hasDrawAuthorHead = false;
            while (!calOver
                    || !authorOver
                    ) {
                ArrayList<Element> elements = new ArrayList<>();
                //已经使用的高度
                int hasUsedHeight = UIUtils.dip2px(getContext(), Element.PADDING_TOP);
                //小标题
                SmallTitleElement smallTitleElement = getSmallTitleElement();
                SmallTitleElement.setTextColor(pageProperty.otherTextColor);
                SmallTitleElement.setTextSize(pageProperty.otherTextSize);
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
                    BigTitleElement.setTextColor(pageProperty.textColor);
                    BigTitleElement.setTextSize(pageProperty.bigTitleSize);
                    elements.add(bigTitleElement);
                    String bigTitleUsefulString = getBigTitleUsefulString(title);
                    String[] split = bigTitleUsefulString.split("\n");
                    paint.getTextBounds(title, 0, title.length(), rect);
                    bigTitleElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
                    bigTitleElement.setY(UIUtils.dip2px(getContext(), 26)
                            + hasUsedHeight + rect.height());
                    //已占用高度
                    hasUsedHeight += UIUtils.dip2px(getContext(), 26) + rect.height() * split.length
                            + pageProperty.bigTitleLineSpace
                            * (split.length - 1) + UIUtils.dip2px(getContext(), 40);
                }
                hasUsedHeight = writeContent(rect, elements, hasUsedHeight);

                //如果有作者的话应当添加
                if (unCalAuthor == null || unCalAuthor.isEmpty()) {
                    authorOver = true;
                } else {
                    int authorCanUseHeight = UIUtils.getDisplayHeight(getContext())
                            - hasUsedHeight - pageProperty.contentMarginBottom;

                    //判断是否已经在前一页已有标题，或者剩下的高度不够画出标题时跳过
                    if (!hasDrawAuthorHead && authorCanUseHeight > pageProperty.authorHeadHeight) {
                        AuthorHeadElement authorHeadElement =
                                new AuthorHeadElement(getContext());
                        authorHeadElement.setTextColor(pageProperty.textColor);
                        authorHeadElement.setY(hasUsedHeight + pageProperty.authorContentSpace);
                        elements.add(authorHeadElement);
                        hasDrawAuthorHead = true;
                        hasUsedHeight += pageProperty.authorHeadHeight;
                    }
                    writeAuthor(rect, elements, hasUsedHeight);
                    //加载作者的话的内容 逻辑应和加载正文逻辑一致
                }
            }
            //分页完成后统一添加底部
            int count = elementList.size();

            for (ArrayList<Element> elements : elementList) {
                BottomElement bottomElement = new BottomElement(getContext());
                BottomElement.setTextColor(pageProperty.otherTextColor);
                BottomElement.setTextSize(pageProperty.otherTextSize);
                bottomElement.setIndex(index + "/" + count);
                bottomElement.setPower(pageProperty.power);
                elements.add(bottomElement);
            }
        }

        void rePaging() {
            unCal = word;
            unCalAuthor = author;
            elementList.clear();
        }

        private int writeContent(Rect rect, ArrayList<Element> elements, int hasUsedHeight) {
            //正文内容
            if (unCal != null && !unCal.isEmpty()) {
                //在正文逻辑判断是会先加上行间距在计算文字位置，所以第一次先减去一个行间距
                hasUsedHeight -= pageProperty.lineSpace;
                //正文已占据空间
                int contentHeight = 0;
                //正文可用空间
                int surplusHeight = UIUtils.getDisplayHeight(getContext()) - hasUsedHeight
                        - pageProperty.contentMarginBottom;
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
                        lineElement.setTextColor(pageProperty.textColor);
                        lineElement.setTextSize(pageProperty.textSize);
                        lineElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
                        lineElement.setY(hasUsedHeight);
                        contentHeight += rect.height();
                        if (contentHeight > surplusHeight) {
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

                pageNum.add(wordNum);
                unCal = unCal.substring(wordNum, unCal.length());
                elements.addAll(lineElements);
            }
            elementList.add(elements);
            if (unCal == null || unCal.isEmpty()) {
                calOver = true;
            }
            return hasUsedHeight;
        }

        private int writeAuthor(Rect rect, ArrayList<Element> elements, int hasUsedHeight) {
            //同正文
            if (hasUsedHeight < pageProperty.authorHeadHeight) {
                hasUsedHeight = hasUsedHeight - pageProperty.authorLineSpace;
            } else {
                hasUsedHeight = hasUsedHeight + pageProperty.authorHeadContentSpace
                        - pageProperty.authorLineSpace;
            }
            //作者已占据空间
            int contentHeight = 0;
            //作者可用空间
            int surplusHeight = UIUtils.getDisplayHeight(getContext()) - hasUsedHeight
                    - pageProperty.contentMarginBottom;
            paint.setTextSize(pageProperty.authorTextSize);
            String[] split = unCalAuthor.split("\n");//分段
            ArrayList<LineElement> lineElements = new ArrayList<>();//临时空间用于储存行
            int wordNum = 0;
            for (String aSplit : split) {
                String paragraph = aSplit;
                boolean pageOver = false;
                while (true) {
                    hasUsedHeight += pageProperty.authorLineSpace;
                    contentHeight += pageProperty.authorLineSpace;
                    String lineUsefulString = getLineUsefulString(paragraph);
                    LineElement lineElement = new LineElement(getContext(), lineUsefulString);
                    paint.getTextBounds(lineUsefulString, 0,
                            lineUsefulString.length(), rect);
                    hasUsedHeight += rect.height();
                    lineElement.setTextColor(pageProperty.textColor);
                    lineElement.setTextSize(pageProperty.authorTextSize);
                    lineElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
                    lineElement.setY(hasUsedHeight);
                    contentHeight += rect.height();
                    if (contentHeight > surplusHeight) {
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
            pageNum.add(wordNum);
            unCalAuthor = unCalAuthor.substring(wordNum, unCalAuthor.length());
            elements.addAll(lineElements);
            if (unCalAuthor == null || unCalAuthor.isEmpty()) {
                authorOver = true;
            }
            return hasUsedHeight;
        }

        //获取小标题的Element
        @NonNull
        private SmallTitleElement getSmallTitleElement() {
            paint.setTextSize(pageProperty.otherTextSize);
            return new SmallTitleElement(getContext(), getSmallTitleUsefulString(title));
        }

        //获取某一页面
        ArrayList<Element> getPage(int index) {
            return elementList.get(index - 1);
        }

        int getCount() {
            return elementList.size();
        }


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

    class PageAnimController {

        //翻到前一页
        synchronized void moveToPrevious(float changeX, float moveY, float moveX) {
            Log.e("moveToPrevious", "前一页");
            if (index == 1) {
                return;
            }
            ValueAnimator objectAnimator = currentPage.animCurrentToNext(changeX, moveY, moveX);
            if (objectAnimator != null) {
                objectAnimator.start();
            }
            ValueAnimator objectAnimator1 = previousPage.animPreviousToCurrent(changeX, moveY, moveX);
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
        synchronized void moveToNext(float changeX, float moveY, float moveX) {
            Log.e("moveToNext", "下一页");
            //是否还有下一页
            if (pageData.getCount() <= index) {
                return;
            }
            ValueAnimator objectAnimator = currentPage.animCurrentToPrevious(changeX, moveY, moveX);
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
            ValueAnimator objectAnimator1 = nextPage.animNextToCurrent(changeX, moveY, moveX);
            if (objectAnimator1 != null) {
                objectAnimator1.start();
            }
        }

        synchronized void childMove(float changeX, float moveY, float moveX) {
            if (pages.isEmpty()) {
                return;
            }
            if (changeX > 0) {
                if (index == 1) {
                    return;
                }
                currentPage.currentToNext(changeX, moveY, moveX);
                previousPage.previousToCurrent(changeX, moveY, moveX);
            } else {
                if (pageData.getCount() <= index) {
                    return;
                }
                currentPage.currentToPrevious(changeX, moveY, moveX);
                nextPage.nextToCurrent(changeX, moveY, moveX);
            }
        }


        synchronized void afterToPrevious() {
            //重新确认页面位置
            pages.add(0, previousPage);
            previousPage = pages.removeLast();
            currentPage = pages.getFirst();
            nextPage = pages.getLast();
            nextPage = pages.getLast();
            --index;
            if (pageData != null && 1 != index) {
                ArrayList<Element> page = pageData.getPage(index - 1);
                ((WriteView) previousPage).setIndex(index - 1);
                ((WriteView) previousPage).setCount(pageData.getCount());
                ((WriteView) previousPage).setPage(page);
                ((WriteView) previousPage).setPower(pageProperty.power);
                ((WriteView) previousPage).notifyData();
            }
            currentPage.reset();
            ((View) currentPage).bringToFront();
        }

        synchronized void afterToNext() {
            //重新确认页面位置
            pages.add(previousPage);
            previousPage = pages.removeFirst();
            currentPage = pages.getFirst();
            nextPage = pages.getLast();
            ++index;
            if (pageData != null && pageData.getCount() > index) {
                ArrayList<Element> page = pageData.getPage(index + 1);
                ((WriteView) nextPage).setIndex(index + 1);
                ((WriteView) nextPage).setCount(pageData.getCount());
                ((WriteView) nextPage).setPage(page);
                ((WriteView) nextPage).setPower(pageProperty.power);
                ((WriteView) nextPage).notifyData();
            }

            currentPage.reset();
            ((View) currentPage).bringToFront();

        }

    }

    class pageProperty {
        int textSize = UIUtils.dip2px(getContext(), LineElement.DEF_TEXT_SIZE);
        int lineSpace = UIUtils.dip2px(getContext(), LineElement.DEF_TEXT_SIZE);
        int otherTextSize = UIUtils.dip2px(getContext(), SmallTitleElement.DEF_TEXT_SIZE);
        int otherTextColor = 0xFF999999;
        int bigTitleSize = UIUtils.dip2px(getContext(), BigTitleElement.DEF_TEXT_SIZE);
        int bigTitleLineSpace = UIUtils.dip2px(getContext(), BigTitleElement.DEF_LINE_SPACE);
        int textColor = LineElement.DEF_TEXT_COLOR;
        int bgColor = Color.WHITE;
        float power = 0.5f;
        int contentMarginBottom = UIUtils.dip2px(getContext(), 38);
        int authorHeadHeight = UIUtils.dip2px(getContext(), AuthorHeadElement.DEF_HEAD_HEIGHT);
        int authorContentSpace = UIUtils.dip2px(getContext(), 40);
        int authorHeadContentSpace = UIUtils.dip2px(getContext(), 30);
        int authorLineSpace = UIUtils.dip2px(getContext(), 5);
        int authorTextSize = UIUtils.dip2px(getContext(), 15);

        public void setNormal() {
            pageProperty.bgColor = Color.WHITE;
            pageProperty.textColor = LineElement.DEF_TEXT_COLOR;
            pageProperty.otherTextColor = 0xFF999999;
        }

        public void setProtectEye() {
            pageProperty.bgColor = 0xFFFBF0D9;
            pageProperty.textColor = 0xFFFBF0D9;
            pageProperty.otherTextColor = 0xFFBDAE96;
        }

        public void setGreen() {
            pageProperty.bgColor = 0xFFD3E4D3;
            pageProperty.textColor = 0xFF455146;
            pageProperty.otherTextColor = 0xFF919F91;
        }

        public void setPink() {
            pageProperty.bgColor = 0xFFFDDFE7;
            pageProperty.textColor = 0xFF5D1A2E;
            pageProperty.otherTextColor = 0xFFAD7C8A;
        }

        public void setBlack() {
            pageProperty.bgColor = 0xFF2C2C2C;
            pageProperty.textColor = 0xFF2C2C2C;
            pageProperty.otherTextColor = 0xFF999999;
        }
    }

    private int getDisplayWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    private int getDisplayHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }
}
