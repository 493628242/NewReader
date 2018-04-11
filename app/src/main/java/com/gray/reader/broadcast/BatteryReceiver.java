package com.gray.reader.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * @author wjy on 2018/4/11.
 */
public class BatteryReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取当前电量
        int level = intent.getIntExtra("level", 0);
        //电量的总刻度
        listener.onListener(level);
        Log.e("level",level+"%");
    }

    //设置监听回调，用于把电量的信息传递给activity
    public BatteryListener listener;

    public interface BatteryListener {
        void onListener(int level);
    }

    public void setBatteryListener(BatteryListener myListener) {
        this.listener = myListener;
    }
}
