package com.gray.reader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.gray.reader.element.BigTitleElement;
import com.gray.reader.element.BottomElement;
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
    //    private PageData pageData;
//    private pageProperty pageProperty;
    public static final int TYPE_PROTECT_EYE = 0;
    public static final int TYPE_GREEN = 1;
    public static final int TYPE_PINK = 2;
    public static final int TYPE_BlACK = 3;
    private ArrayList<Element> page;
    private int count;
    private int index;
    private float power;

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
        resetPaint();
//        pageProperty = new pageProperty();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setPage(ArrayList<Element> page) {
        this.page = page;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPower(float power) {
        this.power = power;
    }


    public void notifyData() {
        invalidate();
    }

    //
//    public void setData(String word, String title, String author) {
//        pageData = new PageData(word, title, author);
//        resetPaint();
//        pageData.paging();
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        resetPaint();
        canvas.drawColor(Color.WHITE);
        if (page == null) {
            return;
        }
        for (Element element : page) {
            if (element instanceof BottomElement) {
                BottomElement element1 = (BottomElement) element;
                element1.setIndex(index + "/" + count);
                element1.setPower(power);
            }
            element.draw(canvas, paint);
        }
    }

    private void resetPaint() {
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

//    public void setPower(int level) {
//        pageProperty.power = level / 100;
//        postInvalidate();
//    }

//    class PageData {
//        private String word;
//        private String title;
//        private String author;
//        private ArrayList<ArrayList<Element>> elementList;
//        private boolean calOver;
//        private boolean pagOver;
//        private String unCal;
//        private ArrayList<Integer> pageNum;
//
//        PageData(String word, String title, String author) {
//            this.word = word;
//            this.title = title;
//            this.author = author;
//            int i = word.lastIndexOf("\n");
//            if (i != word.length() - 1) {
//                word += "\n";
//            }
//            unCal = word;
//            elementList = new ArrayList<>();
//            pageNum = new ArrayList<>();
//        }
//
//        //进行分页操作
//        void paging() {
//            Rect rect = new Rect();
//            while (!calOver) {
//                ArrayList<Element> elements = new ArrayList<>();
//                //已经使用的高度
//                int hasUsedHeight = UIUtils.dip2px(getContext(), Element.PADDING_TOP);
//                //小标题
//                SmallTitleElement smallTitleElement = getSmallTitleElement();
//                elements.add(smallTitleElement);
//                String smallTitleUsefulString = smallTitleElement.getTitle();
//                paint.getTextBounds(smallTitleElement.getTitle(), 0, smallTitleUsefulString.length(),
//                        rect);
//                smallTitleElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
//                smallTitleElement.setY(hasUsedHeight + rect.height());
//                hasUsedHeight += rect.height() + UIUtils.dip2px(getContext(), 18);
//                //大标题
//                if (elementList.isEmpty()) {
//                    paint.setTextSize(pageProperty.bigTitleSize);
//                    BigTitleElement bigTitleElement = new BigTitleElement(getContext(),
//                            getBigTitleUsefulString(title));
//                    elements.add(bigTitleElement);
//                    String bigTitleUsefulString = getBigTitleUsefulString(title);
//                    String[] split = bigTitleUsefulString.split("\n");
//                    paint.setTextSize(pageProperty.bigTitleSize);
//                    paint.getTextBounds(title, 0, title.length(), rect);
//                    bigTitleElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
//                    bigTitleElement.setY(UIUtils.dip2px(getContext(), 26)
//                            + hasUsedHeight + rect.height());
//                    //已占用高度
//                    hasUsedHeight += UIUtils.dip2px(getContext(), 26) + rect.height() * split.length
//                            + pageProperty.bigTitleLineSpace
//                            * (split.length - 1) + UIUtils.dip2px(getContext(), 40);
//                }
//                //正文内容
//                if ((unCal != null && !unCal.isEmpty())
//                        || (author != null && !author.isEmpty())) {
//                    //正文已占据空间
//                    int contentHeight = 0;
//                    //正文可用空间
//                    int lastHeight = UIUtils.getDisplayHeight(getContext()) - hasUsedHeight
//                            - UIUtils.dip2px(getContext(), 38);
//                    paint.setTextSize(pageProperty.textSize);
//                    String[] split = unCal.split("\n");//分段
//
//                    ArrayList<LineElement> lineElements = new ArrayList<>();//临时空间用于储存行
//                    int wordNum = 0;
//                    for (String aSplit : split) {
//                        String paragraph = aSplit;
//                        boolean pageOver = false;
//                        while (true) {
//                            hasUsedHeight += pageProperty.lineSpace;
//                            contentHeight += pageProperty.lineSpace;
//                            String lineUsefulString = getLineUsefulString(paragraph);
//                            LineElement lineElement = new LineElement(getContext(), lineUsefulString);
//                            paint.getTextBounds(lineUsefulString, 0,
//                                    lineUsefulString.length(), rect);
//                            hasUsedHeight += rect.height();
//                            lineElement.setX(UIUtils.dip2px(getContext(), Element.PADDING_START));
//                            lineElement.setY(hasUsedHeight);
//                            contentHeight += rect.height();
//                            if (contentHeight > lastHeight) {
//                                pageOver = true;
//                                break;
//                            } else {
//                                lineElements.add(lineElement);
//                                wordNum += lineUsefulString.length();
//                            }
//                            paragraph = paragraph.substring(lineUsefulString.length(), paragraph.length());
//                            if (paragraph.isEmpty()) {
//                                break;
//                            }
//                        }
//                        //在计算完一页后取数以计算的字数
//                        if (pageOver) {
//                            break;
//                        }
//                        wordNum++;//加上换行符
//                    }
//                    //如果有作者的话应当添加
////                    int authorHeight = UIUtils.getDisplayHeight(getContext()) - marginTop
////                            - UIUtils.dip2px(getContext(), 38) - contentHeight;
////                    if () {
////
////                    }
//                    pageNum.add(wordNum);
//                    unCal = unCal.substring(wordNum, unCal.length());
//                    elements.addAll(lineElements);
//                }
//                elementList.add(elements);
//                if (unCal == null || unCal.isEmpty()) {
//                    calOver = true;
//                }
//            }
//            //分页完成后统一添加底部
//            int count = elementList.size();
//
//            for (ArrayList<Element> elements : elementList) {
//                BottomElement bottomElement = new BottomElement(getContext());
//                bottomElement.setIndex(index + "/" + count);
//                bottomElement.setPower(pageProperty.power);
//                elements.add(bottomElement);
//            }
//        }
//
//        //获取小标题的Element
//        @NonNull
//        private SmallTitleElement getSmallTitleElement() {
//            paint.setTextSize(pageProperty.smallTitleSize);
//            return new SmallTitleElement(getContext(), getSmallTitleUsefulString(title));
//        }
//
//        //获取某一页面
//        ArrayList<Element> getPage(int index) {
//            return elementList.get(index - 1);
//        }
//
//        int getCount() {
//            return elementList.size();
//        }
//
////        private int getContentMarginTop(String title, boolean isFirst) {
////            paint.setTextSize(pageProperty.smallTitleSize);
////            Rect rect1 = new Rect();
////            paint.getTextBounds(title, 0, title.length(), rect1);
////            int marginTop = UIUtils.dip2px(getContext(), Element.PADDING_TOP)
////                    + rect1.height() + UIUtils.dip2px(getContext(), 18);
////            if (isFirst) {
////                String bigTitleUsefulString = getBigTitleUsefulString(title);
////                String[] split = bigTitleUsefulString.split("\n");
////                paint.setTextSize(pageProperty.bigTitleSize);
////                paint.getTextBounds(title, 0, title.length(), rect1);
////                marginTop += UIUtils.dip2px(getContext(), 26) + rect1.height() * split.length
////                        + pageProperty.bigTitleLineSpace
////                        * (split.length - 1) + UIUtils.dip2px(getContext(), 40);
////            }
////            return marginTop;
////        }
//
//        private String getSmallTitleUsefulString(String string) {
//            int usefulWidth = UIUtils.getDisplayWidth(getContext())
//                    - UIUtils.dip2px(getContext(), Element.PADDING_START)
//                    - UIUtils.dip2px(getContext(), Element.PADDING_END);
//            String usefulString;
//            int length = string.length();
//            if (paint.measureText(string) <= usefulWidth) {
//                return string;
//            }
//            while ((paint.measureText(string.substring(0, length)
//                    + paint.measureText("...")) > usefulWidth)) {
//                --length;
//            }
//            usefulString = string.substring(0, length) + "...";
//            return usefulString;
//        }
//
//        private String getBigTitleUsefulString(String string) {
//            //获取屏幕可用宽度
//            int usefulWidth = UIUtils.getDisplayWidth(getContext())
//                    - UIUtils.dip2px(getContext(), Element.PADDING_START)
//                    - UIUtils.dip2px(getContext(), Element.PADDING_END);
//            StringBuilder usefulString = new StringBuilder();
//            int length;
//            if (paint.measureText(string) <= usefulWidth) {
//                return string;
//            }
//            ArrayList<String> strings = new ArrayList<>();
//            for (int i = 0; i != string.length(); i = length) {
//                length = string.length();
//                while ((paint.measureText(string.substring(i, length)) > usefulWidth)) {
//                    --length;
//                }
//                strings.add(string.substring(i, length));
//            }
//            for (String s : strings) {
//                usefulString.append(s).append("\n");
//            }
//            return usefulString.toString();
//        }
//
//        private String getLineUsefulString(String string) {
//            int usefulWidth = UIUtils.getDisplayWidth(getContext())
//                    - UIUtils.dip2px(getContext(), Element.PADDING_START)
//                    - UIUtils.dip2px(getContext(), Element.PADDING_END);
//            String usefulString;
//            int length = string.length();
//            while ((paint.measureText(string.substring(0, length)) > usefulWidth)) {
//                --length;
//            }
//            usefulString = string.substring(0, length);
//            return usefulString;
//        }
//
//    }
//
//    class pageProperty {
//        int textSize = UIUtils.dip2px(getContext(), LineElement.DEF_TEXT_SIZE);
//        int lineSpace = UIUtils.dip2px(getContext(), LineElement.DEF_TEXT_SIZE);
//        int smallTitleSize = UIUtils.dip2px(getContext(), SmallTitleElement.DEF_TEXT_SIZE);
//        int bigTitleSize = UIUtils.dip2px(getContext(), BigTitleElement.DEF_TEXT_SIZE);
//        int bigTitleLineSpace = UIUtils.dip2px(getContext(), BigTitleElement.DEF_LINE_SPACE);
//        int textColor = LineElement.DEF_TEXT_COLOR;
//        int bgColor = Color.WHITE;
//        int bottomTextSize = UIUtils.dip2px(getContext(), BottomElement.DEF_TEXT_SIZE);
//        float power=0.5f;
//    }
}
