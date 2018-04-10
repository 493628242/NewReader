package com.gray.reader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.gray.reader.element.BigTitleElement;
import com.gray.reader.element.Element;
import com.gray.reader.element.LineElement;
import com.gray.reader.element.SmallTitleElement;
import com.gray.reader.util.UIUtils;

import java.util.ArrayList;

/**
 * @author wjy on 2018/4/10.
 */
public class WriteView extends View {
    private Paint paint;
    private PageData pageData;
    private pageProperty pageProperty;
    public static final int TYPE_PROTECT_EYE = 0;
    public static final int TYPE_GREEN = 1;
    public static final int TYPE_PINK = 2;
    public static final int TYPE_BlACK = 3;


    private int index;

    public WriteView(Context context) {
        this(context, null);
    }

    public WriteView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public WriteView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);

    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setData(String word, String title, String author) {
        pageData = new PageData(word, title, author);
        pageProperty = new pageProperty();
        pageData.paging();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        pageData.getPage(index);
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
            unCal = word;
            elementList = new ArrayList<>();
            pageNum = new ArrayList<>();
        }

        //进行分页操作
        void paging() {
            while (!calOver) {
                ArrayList<Element> elements = new ArrayList<>();
                int hasUsedLength = UIUtils.dip2px(getContext(), Element.PADDING_TOP);
                //小标题
                paint.setTextSize(pageProperty.smallTitleSize);
                String smallTitleUsefulString = getSmallTitleUsefulString(title);
                SmallTitleElement smallTitleElement =
                        new SmallTitleElement(getContext(), smallTitleUsefulString);
                smallTitleElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
                smallTitleElement.setY(hasUsedLength);
                elements.add(smallTitleElement);
                Rect rect = new Rect();
                paint.getTextBounds(smallTitleUsefulString, 0, smallTitleUsefulString.length(),
                        rect);
                hasUsedLength += rect.height() + UIUtils.dip2px(getContext(), 18);
                //大标题
                if (elementList.isEmpty()) {
                    paint.setTextSize(pageProperty.bigTitleSize);
                    BigTitleElement bigTitleElement = new BigTitleElement(getContext(),
                            getBigTitleUsefulString(title));
                    bigTitleElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
                    bigTitleElement.setY(UIUtils.dip2px(getContext(), 26
                            + pageProperty.smallTitleSize + Element.PADDING_TOP));
                    elements.add(smallTitleElement);
                    String bigTitleUsefulString = getBigTitleUsefulString(title);
                    String[] split = bigTitleUsefulString.split("\n");
                    paint.setTextSize(pageProperty.bigTitleSize);
                    paint.getTextBounds(title, 0, title.length(), rect);
                    //已占用高度
                    hasUsedLength += UIUtils.dip2px(getContext(), 26) + rect.height() * split.length
                            + pageProperty.bigTitleLineSpace
                            * (split.length - 1) + UIUtils.dip2px(getContext(), 40);
                }
                if (unCal != null && !unCal.isEmpty()) {
                    int contentHeight = 0;
                    int marginTop = getContentMarginTop(title, elementList.isEmpty());
                    int lastHeight = UIUtils.getDisplayHeight(getContext()) - hasUsedLength
                            - UIUtils.dip2px(getContext(), 38);

                    paint.setTextSize(pageProperty.textSize);
                    String[] split = unCal.split("\n");//分段
                    ArrayList<LineElement> lineElements = new ArrayList<>();//临时空间用于储存行
                    int wordNum = 0;
                    for (int i = 0; i < split.length; ++i) {
                        String paragraph = split[i];
                        boolean pageOver = false;
                        while (true) {
                            if (i != 0) {
                                hasUsedLength += pageProperty.lineSpace;
                                contentHeight += pageProperty.lineSpace;
                            }
                            if (paragraph.isEmpty()) {
                                break;
                            }
                            String lineUsefulString = getLineUsefulString(paragraph);
                            Log.e("lineUsefulString", lineUsefulString);

                            LineElement lineElement = new LineElement(getContext(), lineUsefulString);
                            paint.getTextBounds(lineUsefulString, 0,
                                    lineUsefulString.length(), rect);
                            lineElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
                            lineElement.setY(hasUsedLength);
                            contentHeight += rect.height();
                            if (contentHeight > lastHeight) {
                                pageOver = true;
                                break;
                            } else {
                                lineElements.add(lineElement);
                                wordNum += lineUsefulString.length();
                            }
                            paragraph = paragraph.substring(0, lineUsefulString.length());
                            if (paragraph.isEmpty()) {
                                break;
                            }
                        }
                        if (pageOver) {
                            unCal = word.substring(0, wordNum);
                            break;
                        }
                        wordNum++;//加上换行符
                    }

//                    int authorHeight = UIUtils.getDisplayHeight(getContext()) - marginTop
//                            - UIUtils.dip2px(getContext(), 38) - contentHeight;
//                    if () {
//
//                    }
                    pageNum.add(wordNum);
                    unCal = word.substring(0, wordNum);
                    elements.addAll(lineElements);
                }
                elementList.add(elements);
                if (unCal == null || unCal.isEmpty()) {
                    calOver = true;
                }
            }
        }

        ArrayList<Element> getPage(int index) {
            return elementList.get(index - 1);
        }

        private int getContentMarginTop(String title, boolean isFirst) {
            paint.setTextSize(pageProperty.smallTitleSize);
            Rect rect1 = new Rect();
            paint.getTextBounds(title, 0, title.length(), rect1);
            int marginTop = UIUtils.dip2px(getContext(), Element.PADDING_TOP)
                    + rect1.height() + UIUtils.dip2px(getContext(), 18);
            if (isFirst) {
                String bigTitleUsefulString = getBigTitleUsefulString(title);
                String[] split = bigTitleUsefulString.split("\n");
                paint.setTextSize(pageProperty.bigTitleSize);
                paint.getTextBounds(title, 0, title.length(), rect1);
                marginTop += UIUtils.dip2px(getContext(), 26) + rect1.height() * split.length
                        + pageProperty.bigTitleLineSpace
                        * (split.length - 1) + UIUtils.dip2px(getContext(), 40);
            }
            return marginTop;
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
                    + paint.measureText("...")) <= usefulWidth)) {
                --length;
            }
            usefulString = string.substring(0, length) + "...";
            return usefulString;
        }

        private String getBigTitleUsefulString(String string) {
            int usefulWidth = UIUtils.getDisplayWidth(getContext())
                    - UIUtils.dip2px(getContext(), Element.PADDING_START)
                    - UIUtils.dip2px(getContext(), Element.PADDING_END);
            StringBuilder usefulString = new StringBuilder();
            int length;
            if (paint.measureText(string) <= usefulWidth) {
                return string;
            }
            ArrayList<String> strings = new ArrayList<>();
            for (int i = 0; i == string.length(); i = length) {
                length = string.length();
                while ((paint.measureText(string.substring(i, length)) <= usefulWidth)) {
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
            int length = 0;
            do {
                ++length;
            } while ((paint.measureText(string.substring(0, length)) > usefulWidth));
            usefulString = string.substring(0, length - 1);
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

    }
}
