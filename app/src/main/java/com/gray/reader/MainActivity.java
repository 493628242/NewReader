package com.gray.reader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.eschao.android.widget.pageflip.PageFlip;
import com.gray.reader.page.FlipPage;
import com.gray.reader.page.NormalPage;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ReaderLayout readerLayout = findViewById(R.id.reader);
        readerLayout.setPage(FlipPage.class);
        readerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("click", "点击监听");
            }
        });
    }
}
