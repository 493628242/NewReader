package com.gray.reader;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.gray.reader.broadcast.BatteryReceiver;
import com.gray.reader.page.NormalPage;
import com.gray.reader.util.UIUtils;

public class ReaderActivity extends AppCompatActivity {
    String word = "　一夜，天还没亮，屋里漆黑一片，只有落地花罩外一盏小小的宫灯正发着微弱的黄光。\n" +
            "\n" +
            "　　\n" +
            "\n" +
            "　　萧源躺在柔软馨香的被褥里，隔着层层幔帐，隐约可见花罩外几名丫鬟仆妇正在给她熏衣，准备盥漱用具，" +
            "房里安静的连窗外的落雪声都能隐约听见。屋里火墙烧的正暖，萧源翻了一个身，掀了掀被窝散了些热气，“骨碌”一声，熏被的银香球被她无意间踢了下，滚到了地上。\n" +
            "\n" +
            "　　架子床外值夜的丫鬟灵偃正在望着窗户发呆，听到银香球落地的声音，打了一个激灵，" +
            "轻手轻脚的掀起一角床帘查看，见萧源已经睁开了眼睛，就轻声说：“夫人说这几天天气太冷，" +
            "姑娘们身子又弱，就不用这么早起身请安了，让你们多睡一会，姑架子床外值夜的丫鬟灵偃正在望着窗户发呆，" +
            "听到银香球落地的声音，打了一个激灵，轻手轻脚的掀起一角床帘查看，见萧源已经睁开了一夜，天还没亮，屋里漆黑一片，只有落地花罩外一盏小小的宫灯正发着微弱的黄光。\n" +
            "　　萧源躺在柔软馨香的被褥里，隔着层层幔帐，隐约可见花罩外几名丫鬟仆妇正在给她熏衣，准备盥漱用具，房里安静的连窗外的落雪声都能隐约听见。屋里火墙烧的正暖，萧源翻了一个身，掀了掀被窝散了些热气，“骨碌”一声，熏被的银香球被她无意间踢了下，滚到了地上。\n" +
            "\n" +
            "　　\n" +
            "\n" +
            "　　架子床外值夜的丫鬟灵偃正在望着窗户发呆，听到银香球落地的声音，打了一个激灵，轻手轻脚的掀起一角床帘查看，见萧源已经睁开了眼睛，就轻声说：“夫人说这几天天气太冷，姑娘们身子又弱，就不用这么早起身请安了，让你们多睡一会，姑架子床外值夜的丫鬟灵偃正在望着窗户发呆，听到银香球落地的声音，打了一个激灵，轻手轻脚的掀起一角床帘查看，见萧源已经睁开了眼睛，就轻声说：“夫人说这几天天气太冷，姑娘们身子又弱，就不用这么早起身请安了，让你们多睡一会，姑　　\n" +
            "\n" +
            "　　\n" +
            "\n" +
            "　　架子床外值夜的丫鬟灵偃正在望着窗户发呆，听到银香球落地的声音，打了一个激\n";
    String title = "第3章 最长30字章节名称最长30字章节名称最长30字章节名称";
    String author = "记得按时发几首歌罚款就是个示范岗后翻赶快来啦gals尬死了噶临时发发生\n"
            + "　　萧源躺在柔软馨香的被褥里，隔着层层幔帐，隐约可见花罩外几名丫鬟仆妇正在给她熏衣，准备盥漱用具，" +
            "房里安静的连窗外的落雪声都能隐约听见。屋里火墙烧的正暖，萧源翻了一个身，掀了掀被窝散了些热气，“骨碌”一声" +
            "，熏被的银香球被她无意间踢了下，滚到了地上。\n" + "　　萧源躺在柔软馨香的被褥里，隔着层层幔帐，隐约可见花罩外几名丫鬟仆妇正在给她熏衣，准备盥漱用具，" +
            "房里安静的连窗外的落雪声都能隐约听见。屋里火墙烧的正暖，萧源翻了一个身，掀了掀被窝散了些热气，“骨碌”一声" +
            "，熏被的银香球被她无意间踢了下，滚到了地上。\n" + "　　萧源躺在柔软馨香的被褥里，隔着层层幔帐，隐约可见花罩外几名丫鬟仆妇正在给她熏衣，准备盥漱用具，" +
            "房里安静的连窗外的落雪声都能隐约听见。屋里火墙烧的正暖，萧源翻了一个身，掀了掀被窝散了些热气，“骨碌”一声" +
            "，熏被的银香球被她无意间踢了下，滚到了地上。\n" + "　　萧源躺在柔软馨香的被褥里，隔着层层幔帐，隐约可见花罩外几名丫鬟仆妇正在给她熏衣，准备盥漱用具，" +
            "房里安静的连窗外的落雪声都能隐约听见。屋里火墙烧的正暖，萧源翻了一个身，掀了掀被窝散了些热气，“骨碌”一声" +
            "，熏被的银香球被她无意间踢了下，滚到了地上。\n";
    //            +"\n a";
    private BatteryReceiver receiver;
    int i = 0;
    private ReaderLayout readerLayout;
    private PopupBottom popupBottom;
    private PopupTop popupTop;
    private PopupRight popupRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        popupBottom = new PopupBottom();
        popupTop = new PopupTop();
        popupRight = new PopupRight();
        readerLayout = findViewById(R.id.reader);
        readerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "点击监听");
////                readerLayout.setTextSize(25);
//                readerLayout.setLineSpace(++i%3);
////                readerLayout.chooseMode(++i % 5);
                if (popupTop.isShow()
                        && popupBottom.isShow()
                        && popupRight.isShow()) {
                    popupTop.closePop();
                    popupBottom.closePop();
                    popupRight.closePop();
                    setFullScreen(false);
                } else {
                    popupTop.showPop();
                    popupBottom.showPop();
                    popupRight.showPop();
                    setFullScreen(true);
                }
            }
        });
        readerLayout.setData(word, title, author);
        readerLayout.setPage(NormalPage.class);
        readerLayout.setIndex(1);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        receiver = new BatteryReceiver();
        registerReceiver(receiver, intentFilter);
        receiver.setBatteryListener(new BatteryReceiver.BatteryListener() {
            @Override
            public void onListener(int level) {
                readerLayout.setPower(level);
            }
        });
    }

    private void setFullScreen(boolean isFullScreen) {
        if (isFullScreen) {//设置为非全屏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(lp);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {//设置为全屏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    class PopupBottom {
        PopupWindow popupWindow;

        public PopupBottom() {
            View view = LayoutInflater.from(ReaderActivity.this)
                    .inflate(R.layout.pop_bottom, null);
            popupWindow = new PopupWindow(view,
                    UIUtils.getDisplayWidth(ReaderActivity.this),
                    UIUtils.dip2px(ReaderActivity.this, 48));
            popupWindow.setAnimationStyle(R.style.PopupBottomAnim);
            // 设置PopupWindow是否能响应点击事件
            popupWindow.setTouchable(true);
        }

        void showPop() {
            popupWindow.showAtLocation(readerLayout, Gravity.BOTTOM, 0, 0);
        }

        boolean isShow() {
            return popupWindow.isShowing();
        }

        void closePop() {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        }

        void destroy() {
            popupWindow = null;
        }
    }

    class PopupTop {
        PopupWindow popupWindow;

        public PopupTop() {
            View view = LayoutInflater.from(ReaderActivity.this)
                    .inflate(R.layout.pop_top, null);
            popupWindow = new PopupWindow(view,
                    UIUtils.getDisplayWidth(ReaderActivity.this),
                    UIUtils.dip2px(ReaderActivity.this, 44)
            );
            popupWindow.setAnimationStyle(R.style.PopupTopAnim);
            // 设置PopupWindow是否能响应点击事件
            popupWindow.setTouchable(true);
        }

        void showPop() {
            popupWindow.showAtLocation(readerLayout, Gravity.TOP, 0,
                    UIUtils.getStatusBarHeight(ReaderActivity.this));
        }

        boolean isShow() {
            return popupWindow.isShowing();
        }

        void closePop() {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        }

        void destroy() {
            popupWindow = null;
        }
    }

    class PopupRight {
        PopupWindow popupWindow;

        public PopupRight() {
            View view = LayoutInflater.from(ReaderActivity.this)
                    .inflate(R.layout.pop_right, null);
            popupWindow = new PopupWindow(view,
                    UIUtils.dip2px(ReaderActivity.this, 85),
                    UIUtils.dip2px(ReaderActivity.this, 35));
            popupWindow.setAnimationStyle(R.style.PopupRightAnim);
            // 设置PopupWindow是否能响应点击事件
            popupWindow.setTouchable(true);
        }

        void showPop() {
            popupWindow.showAtLocation(readerLayout, Gravity.END | Gravity.TOP, 0,
                    UIUtils.dip2px(ReaderActivity.this, 65)
                            + UIUtils.getStatusBarHeight(ReaderActivity.this));
        }

        boolean isShow() {
            return popupWindow.isShowing();
        }

        void closePop() {
            if (popupWindow != null) {
                popupWindow.dismiss();
            }
        }

        void destroy() {
            popupWindow = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (popupRight.isShow()
                || popupBottom.isShow()
                || popupTop.isShow()) {
            popupRight.closePop();
            popupTop.closePop();
            popupBottom.closePop();
            setFullScreen(false);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        popupTop.destroy();
        popupBottom.destroy();
        popupRight.destroy();
        unregisterReceiver(receiver);
    }
}
