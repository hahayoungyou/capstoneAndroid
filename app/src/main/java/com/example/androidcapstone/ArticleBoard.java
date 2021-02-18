package com.example.androidcapstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ArticleBoard extends AppCompatActivity {
    // 버튼 선택하면 글 목록과 지도 나오도록.


   static String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_board);

        Button button = findViewById(R.id.button); // 글 목록 나오도록.
        Button button2 = findViewById(R.id.button2); // 지도 나오도록.

        Intent intent = getIntent();
        name = intent.getExtras().getString("values");
        // ArticleMenuFragment에서 values값 받았다는 로그.
        Log.i("ArticleBoard", name);

        //button.setBackgroundColor(ContextCompat.getColor(this, R.id.));


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ExpertFragment()).commit();

            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new MapFragment()).commit();
            }
        });
    }

    public String getMyData(){
        return name;
    }
}