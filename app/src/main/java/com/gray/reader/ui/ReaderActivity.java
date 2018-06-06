package com.gray.reader.ui;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.gray.reader.R;
import com.gray.reader.broadcast.BatteryReceiver;
import com.gray.reader.page.BookPageView;
import com.gray.reader.page.ReaderLayout;
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
    private PopupTypeface popupTypeface;
    private PopupMenu popupMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_main);

        popupBottom = new PopupBottom();
        popupTop = new PopupTop();
        popupRight = new PopupRight();
        popupTypeface = new PopupTypeface();
        popupMenu = new PopupMenu();
        readerLayout = findViewById(R.id.reader);

        readerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (readerLayout.isShowPop()) {
                    popupTop.closePop();
                    popupBottom.closePop();
                    popupRight.closePop();
                    readerLayout.setShowPop(false);
                    hideStatusBar();
                } else {
                    popupTop.showPop();
                    popupBottom.showPop();
                    popupRight.showPop();
                    readerLayout.setShowPop(true);
                    showStatusBar();
                }
            }
        });
        readerLayout.setData(word, title, author);
        readerLayout.paging();
        readerLayout.setIndex(1);
        readerLayout.setPage(BookPageView.class);
//        readerLayout.AsyncPaging(new ReaderLayout.AsyncPagingListener() {
//            @Override
//            public void onFinishListener() {
//                readerLayout.loadData();
//
//            }
//        });

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

    private void hideStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    private void showStatusBar() {
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
    }

    class PopupBottom implements View.OnClickListener {
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
            initView(view);
        }

        private void initView(View view) {
            view.findViewById(R.id.img_index).setOnClickListener(this);
            view.findViewById(R.id.cb_night_or_day).setOnClickListener(this);
            view.findViewById(R.id.img_typeface).setOnClickListener(this);
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

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_index:
                    break;
                case R.id.cb_night_or_day:
                    if (((CheckBox) v).isChecked()) {
                        setAlpha(0.7f);
                    } else {
                        setAlpha(1);
                    }
                    break;
                case R.id.img_typeface:
                    popupTypeface.showPop();
                    break;
            }
        }
    }

    class PopupTop implements View.OnClickListener {
        PopupWindow popupWindow;

        public PopupTop() {
            View view = LayoutInflater.from(ReaderActivity.this)
                    .inflate(R.layout.pop_top, null);
            popupWindow = new PopupWindow(view,
                    UIUtils.getDisplayWidth(ReaderActivity.this),
                    UIUtils.dip2px(ReaderActivity.this, 44)
                            + UIUtils.getStatusBarHeight(ReaderActivity.this));
            popupWindow.setAnimationStyle(R.style.PopupTopAnim);
            // 设置PopupWindow是否能响应点击事件
            popupWindow.setTouchable(true);
            initView(view);
        }

        private void initView(View view) {
            view.findViewById(R.id.img_more).setOnClickListener(this);
            view.findViewById(R.id.img_back).setOnClickListener(this);
            view.findViewById(R.id.img_recommend).setOnClickListener(this);
            view.findViewById(R.id.img_reward).setOnClickListener(this);
            view.findViewById(R.id.img_ticket).setOnClickListener(this);
            view.findViewById(R.id.img_download).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_more:
                    popupMenu.showPop();
                    if (popupRight.isShow()) {
                        popupRight.closePop();
                    }
                    break;
                case R.id.img_back:
                    ReaderActivity.this.finish();
                    break;
                case R.id.img_recommend:
                    //推荐
                    break;
                case R.id.img_reward:
                    //赏
                    break;
                case R.id.img_ticket:
                    //月票
                    break;
                case R.id.img_download:
                    //下载
                    break;
            }
        }

        void showPop() {
            popupWindow.showAtLocation(readerLayout, Gravity.TOP, 0,
                    0);
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

    class PopupRight implements View.OnClickListener {
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
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            //加入书架
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

    class PopupMenu implements View.OnClickListener {
        PopupWindow popupWindow;

        public PopupMenu() {
            View view = LayoutInflater.from(ReaderActivity.this)
                    .inflate(R.layout.pop_menu, null);
            popupWindow = new PopupWindow(view,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    UIUtils.dip2px(ReaderActivity.this, 273));
            popupWindow.setAnimationStyle(R.style.PopupMenuAnim);
            // 设置PopupWindow是否能响应点击事件
            popupWindow.setTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setFocusable(true);
            initView(view);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    if (true) { //判断是否需要显示右侧pop
                        popupRight.showPop();
                    }
                }
            });

        }

        private void initView(View view) {
            view.findViewById(R.id.cb_auto_buy).setOnClickListener(this);
            view.findViewById(R.id.tv_subscribe).setOnClickListener(this);
            view.findViewById(R.id.tv_book_detail).setOnClickListener(this);
            view.findViewById(R.id.tv_share).setOnClickListener(this);
            view.findViewById(R.id.tv_report).setOnClickListener(this);
            view.findViewById(R.id.tv_refresh).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cb_auto_buy:
                    //自动购买收费章节
                    break;
                case R.id.tv_subscribe:
                    //订阅全本
                    break;
                case R.id.tv_book_detail:
                    //书籍详情
                    break;
                case R.id.tv_share:
                    //分享
                    break;
                case R.id.tv_report:
                    //内容举报
                    break;
                case R.id.tv_refresh:
                    //刷新本章内容
                    break;
            }
        }

        void showPop() {
            popupWindow.showAtLocation(readerLayout, Gravity.END | Gravity.TOP,
                    UIUtils.dip2px(ReaderActivity.this, 5),
                    UIUtils.dip2px(ReaderActivity.this, 49)
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

    class PopupTypeface implements View.OnClickListener {
        PopupWindow popupWindow;
        TextView textSize;
        TextView tvEnlarge;
        TextView tvNarrow;
        RadioButton rbWide;
        RadioButton rbMedium;
        RadioButton rbNormal;

        public PopupTypeface() {
            View view = LayoutInflater.from(ReaderActivity.this)
                    .inflate(R.layout.pop_typeface, null);
            popupWindow = new PopupWindow(view,
                    UIUtils.getDisplayWidth(ReaderActivity.this),
                    UIUtils.dip2px(ReaderActivity.this, 255));
            popupWindow.setAnimationStyle(R.style.PopupBottomAnim);
            // 设置PopupWindow是否能响应点击事件
            popupWindow.setTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            popupWindow.setFocusable(true);
            intiView(view);
        }

        private void intiView(View view) {
            tvEnlarge = view.findViewById(R.id.tv_enlarge);
            tvEnlarge.setOnClickListener(this);
            textSize = view.findViewById(R.id.tv_text_size);
            tvNarrow = view.findViewById(R.id.tv_narrow);
            tvNarrow.setOnClickListener(this);
            view.findViewById(R.id.choose_flip_type).setOnClickListener(this);
            view.findViewById(R.id.rb_normal_bg).setOnClickListener(this);
            view.findViewById(R.id.rb_protect_eye).setOnClickListener(this);
            view.findViewById(R.id.rb_green_bg).setOnClickListener(this);
            view.findViewById(R.id.rb_pink_bg).setOnClickListener(this);
            view.findViewById(R.id.rb_black_bg).setOnClickListener(this);

            rbWide = view.findViewById(R.id.rb_wide);
            rbWide.setOnClickListener(this);
            rbMedium = view.findViewById(R.id.rb_medium);
            rbMedium.setOnClickListener(this);
            rbNormal = view.findViewById(R.id.rb_normal);
            rbNormal.setOnClickListener(this);

            SeekBar sbBrightness = view.findViewById(R.id.sb_brightness);
            sbBrightness.setProgress(getScreenBrightness());
            sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    setWindowBrightness(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_enlarge:
                    //放大字体
                    String size = textSize.getText().toString();
                    int i = Integer.parseInt(size);
                    i++;
                    tvNarrow.setEnabled(true);
                    tvNarrow.setTextColor(ReaderActivity.this
                            .getResources().getColor(R.color.white));
                    if (i == 25) {
                        tvEnlarge.setEnabled(false);
                        tvEnlarge.setTextColor(ReaderActivity.this
                                .getResources().getColor(R.color.non_clickable));
                    }
                    textSize.setText(String.valueOf(i));
                    readerLayout.setTextSize(i);
                    rbNormal.setChecked(true);
                    break;
                case R.id.tv_narrow: //缩小字体
                    tvEnlarge.setEnabled(true);
                    tvEnlarge.setTextColor(ReaderActivity.this
                            .getResources().getColor(R.color.white));
                    String size1 = textSize.getText().toString();
                    int j = Integer.parseInt(size1);
                    j--;
                    if (j == 15) {
                        tvNarrow.setEnabled(false);
                        tvNarrow.setTextColor(ReaderActivity.this
                                .getResources().getColor(R.color.non_clickable));
                    }
                    textSize.setText(String.valueOf(j));
                    readerLayout.setTextSize(j);
                    rbNormal.setChecked(true);
                    break;
                case R.id.choose_flip_type:
                    //翻页样式
                    break;
                case R.id.rb_normal_bg:
                    readerLayout.chooseMode(ReaderLayout.TYPE_NORMAL);

                    break;
                case R.id.rb_protect_eye:
                    readerLayout.chooseMode(ReaderLayout.TYPE_PROTECT_EYE);
                    break;
                case R.id.rb_green_bg:
                    readerLayout.chooseMode(ReaderLayout.TYPE_GREEN);
                    break;
                case R.id.rb_pink_bg:
                    readerLayout.chooseMode(ReaderLayout.TYPE_PINK);
                    break;
                case R.id.rb_black_bg:
                    readerLayout.chooseMode(ReaderLayout.TYPE_BlACK);
                    break;
                case R.id.rb_wide:
                    readerLayout.setLineSpace(ReaderLayout.LINE_SPACE_WIDE);
                    break;
                case R.id.rb_medium:
                    readerLayout.setLineSpace(ReaderLayout.LINE_SPACE_MEDIUM);
                    break;
                case R.id.rb_normal:
                    readerLayout.setLineSpace(ReaderLayout.LINE_SPACE_NORMAL);
                    break;
            }
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

    private int getScreenBrightness() {
        int value = 0;
        ContentResolver cr = this.getContentResolver();
        try {
            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Settings.SettingNotFoundException e) {
            Log.e("error", e.getLocalizedMessage());
        }
        return value;
    }

    /**
     * 设置亮度
     *
     * @param brightness
     */
    private void setWindowBrightness(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255f;
        window.setAttributes(lp);
    }

    /**
     * 设置夜间模式
     */
    public void setAlpha(float alpha) {
        WindowManager.LayoutParams ll = getWindow().getAttributes();
        ll.alpha = alpha;
        getWindow().setAttributes(ll);
    }


    @Override
    public void onBackPressed() {
        if (popupRight.isShow()
                || popupBottom.isShow()
                || popupTop.isShow()) {
            popupRight.closePop();
            popupTop.closePop();
            popupBottom.closePop();
            hideStatusBar();
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
        popupTypeface.destroy();
        unregisterReceiver(receiver);
    }
}
