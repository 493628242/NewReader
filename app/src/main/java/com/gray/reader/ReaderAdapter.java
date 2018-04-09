package com.gray.reader;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.gray.reader.page.BasePage;

import java.util.ArrayList;

/**
 * @author wjy on 2018/4/9.
 */
public class ReaderAdapter {
    private String word;
    private String title;
    private String author;
    private ArrayList<Integer> pageSize;
    private String unRead;
    private String hasRead;

    public ReaderAdapter(String word, String title, String author) {
        this.word = word;
        this.title = title;
        pageSize = new ArrayList<>();
        this.author = author;
        unRead = word;
    }

    public void loadFirst(BasePage basePage, int index) {
        loadData(basePage, index, word);
    }


    public void loadNext(BasePage basePage, int index) {
        if (unRead == null || unRead.isEmpty()) {
            return;
        }
        loadData(basePage, index, unRead);
    }

    public void loadPrevious(BasePage basePage, int index) {
        Integer integer = pageSize.get(index);
        basePage.smallTitle.setText(title);
        if (index == 0) {
            basePage.bigTitle.setText(title);
            basePage.bigTitle.setVisibility(View.VISIBLE);
        } else {
            basePage.bigTitle.setVisibility(View.GONE);
        }
        basePage.content.setText(hasRead.substring(hasRead.length() - integer));
        unRead = hasRead.substring(0, hasRead.length() - integer);
        hasRead += basePage.content.getText().toString();
        int resize = basePage.content.resize();
        Log.e("resize", resize + "#");
        pageSize.remove(index);
        Log.e("size", pageSize.size() + "%");
    }

    private void loadData(BasePage basePage, int index, String content) {
        basePage.smallTitle.setText(title);
        basePage.content.setText(content);
        if (index == 0) {
            basePage.bigTitle.setText(title);
            basePage.bigTitle.setVisibility(View.VISIBLE);
        } else {
            basePage.bigTitle.setVisibility(View.GONE);
        }
        int resize = basePage.content.resize();
        Log.e("resize", resize + "#");
        int length = basePage.content.getText().length();
        pageSize.add(length);
        unRead = content.substring(length);
        hasRead += basePage.content.getText().toString();
        Log.e("size", pageSize.size() + "%");

    }


}
