package com.gray.reader.ui;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.gray.reader.R;
import com.gray.reader.flip.HorizontalFlip;
import com.gray.reader.page.BookView;

/**
 * @author wjy on 2019/3/21.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitynew);
        BookView book = findViewById(R.id.book);
        book.setFlipModel(new HorizontalFlip());
        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "menu", Toast.LENGTH_LONG).show();
            }
        });
    }
}
