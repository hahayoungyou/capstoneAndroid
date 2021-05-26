package com.example.androidcapstone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.androidcapstone.Login.token;

public class PromotionBoard extends AppCompatActivity {
    // 홍보게시판 글 목록

    Retrofit retrofit;
    JsonApi jsonApi;
    List<BoardData> dataList;

    RecyclerView proRecyclerView;
    PromotionRecyclerViewAdapter promotionRecyclerViewAdapter;
    Button button;

    static String name;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.154.52:8080";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_board);

        // titlebar 없애기
        ActionBar bar = getSupportActionBar();
        bar.hide();

        // ArticleMenuFragment에서 받아오기
        Intent intent = getIntent();
        name = intent.getExtras().getString("values");
        Log.i("PromotionBoard", name);

        // 버튼 누르면 글 작성 액티비티로 넘어감
        button = (Button)findViewById(R.id.writePro);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 선택된 mode 값을 보내주면서 WritingBoard를 시작
                Intent intent1 = new Intent(PromotionBoard.this, WritingPromotion.class);
                intent1.putExtra("mode", "else");
                startActivity(intent1);
            }
        });

        proRecyclerView = findViewById(R.id.recycler_view_pro);

        jsonApi = ServiceGenerator.createService(JsonApi.class, token);

        Callback<List<BoardData>> callback = new Callback<List<BoardData>>() {
            @Override
            public void onResponse(Call<List<BoardData>> call, Response<List<BoardData>> response) {
                if(response.isSuccessful()) {
                    dataList = response.body();
                    Log.i("PromotionBoard", dataList.toString());

                    promotionRecyclerViewAdapter = new PromotionRecyclerViewAdapter(getApplicationContext(), dataList);
                    proRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    proRecyclerView.setAdapter(promotionRecyclerViewAdapter);
                } else {
                    Log.e("PromotionBoard", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<BoardData>> call, Throwable t) {
                Log.e("PromotionBoard", t.getMessage());
            }
        };
        jsonApi.getBoard(name).enqueue(callback);

        // token 보내고 username 받아오기
        Callback<Username> call = new Callback<Username>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Username> call, Response<Username> response) {
                if(response.isSuccessful()) {
                    Log.i("Promotion - username", response.body().getName());

                } else {
                    Log.e("Promotion - getUsername", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Username> call, Throwable t) {
                Log.e("Promotion - getUsername fail", t.getMessage());
            }
        };
        jsonApi.getUsername().enqueue(call);

    }

}