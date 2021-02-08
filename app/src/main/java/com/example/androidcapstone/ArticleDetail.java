package com.example.androidcapstone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidcapstone.databinding.ActivityArticleDetailBinding;

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleDetail extends AppCompatActivity {
    // 글 목록의 아이템을 누르면 나오는 글 상세 화면

    Retrofit retrofit;
    JsonApi jsonApi;
    List<CommentData> dataList;

    Integer num;
    String id;

    RecyclerView recyclerView2;
    RecyclerViewAdapter2 recyclerViewAdapter2;


    static final String URL = "http://192.168.35.91:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        TextView title = (TextView)findViewById(R.id.title);
        TextView question = (TextView)findViewById(R.id.question);

        Intent intent = getIntent();

        String mTitle = intent.getExtras().getString("title");
        title.setText(mTitle);

        String mQuestion = intent.getExtras().getString("question");
        question.setText(mQuestion);

        num = intent.getExtras().getInt("num");
        id = intent.getExtras().getString("board_id");

        // retrofit 통신 연결 - Spring 웹 서버와 연결
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerView2 = findViewById(R.id.recycler_view2);

        jsonApi = retrofit.create(JsonApi.class);

        Callback<List<CommentData>> callback = new Callback<List<CommentData>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<CommentData>> call, Response<List<CommentData>> response) {
                if(response.isSuccessful()) {

                    dataList = response.body();
                    Log.d("ArticleDetail", dataList.toString());


                    recyclerViewAdapter2 = new RecyclerViewAdapter2(getApplicationContext(), dataList);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView2.setAdapter(recyclerViewAdapter2);

                    Log.d("board_no", String.valueOf(num));

                    //textView = (TextView)mView.findViewById(R.id.text);
                    //textView.setText(response.body().toString());


                } else {
                    Log.d("log", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<CommentData>> call, Throwable t) {
                //Log.d("log", "Fail");
                t.printStackTrace();
            }
        };
        jsonApi.getComment(num).enqueue(callback);


        EditText editTextComment = (EditText)findViewById(R.id.editTextComment);

        Button writeComment = (Button)findViewById(R.id.writeComment);
        writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentData cd = new CommentData();
                cd.answer=editTextComment.getText().toString();
                cd.board_no=num;
                cd.board_id=id;
                cd.comment_id="user2";
                cd.comment_like=0;
                cd.comment_date=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new java.util.Date());
                if(cd.answer.equals("")) return;
                else addComment(cd);
            }
        });

        // 글 수정
        Button update = (Button)findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WritingBoard.class);
                intent.putExtra("mode","edit");
                intent.putExtra("board_no", num);
                intent.putExtra("title", title.getText().toString());
                intent.putExtra("question", question.getText().toString());
                Log.d("ArticleDetail - title", title.getText().toString());
                startActivity(intent);
            }
        });

        Button delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(num);
            }
        });

    }

    // 글 삭제
    private void deletePost(Integer no) {
        Call<Void> calls = jsonApi.deleteUser(no);
        calls.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (!response.isSuccessful()) {

                    Log.i("board delete", "num="+no);

                    //textViewResult.setText("code: " + response.code());boar
                    Intent intent2=new Intent(ArticleDetail.this, ArticleBoard.class);
                    String name=ArticleBoard.name;
                    intent2.putExtra("values",name);
                    startActivity(intent2);
                }

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i("board delete fail", String.valueOf(num));
            }


        });

    }

    // 댓글 추가
    private void addComment(CommentData c) {
        Call<CommentData> call = jsonApi.addComment(c);
        call.enqueue(new Callback<CommentData>() {
            @Override
            public void onResponse(Call<CommentData> call, Response<CommentData> response) {
                Log.i("addComment", String.valueOf(c.board_id));
                Toast.makeText(ArticleDetail.this, "Comment created successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<CommentData> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

}