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

    RecyclerView recyclerView2;
    RecyclerViewAdapter2 recyclerViewAdapter2;

    static final String URL = "http://192.168.35.91:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        TextView user = (TextView)findViewById(R.id.user);
        TextView datetime = (TextView)findViewById(R.id.datetime);
        TextView title = (TextView)findViewById(R.id.title);
        TextView question = (TextView)findViewById(R.id.question);

        TextView tag1 = (TextView)findViewById(R.id.tag1);
        TextView tag2 = (TextView)findViewById(R.id.tag2);
        TextView tag3 = (TextView)findViewById(R.id.tag3);
        TextView tag4 = (TextView)findViewById(R.id.tag4);
        TextView tag5 = (TextView)findViewById(R.id.tag5);

        TextView goodcount = (TextView)findViewById(R.id.goodcount);
        TextView commentcount = (TextView)findViewById(R.id.commentcount);

        Intent intent = getIntent();

        String id = intent.getExtras().getString("board_id");
        user.setText("작성자 : " +id);

        String mTitle = intent.getExtras().getString("title");
        title.setText(mTitle);

        String mQuestion = intent.getExtras().getString("question");
        question.setText(mQuestion);

        String mDatetime = intent.getExtras().getString("datetime");
        datetime.setText(mDatetime);

        String mGoodcount = intent.getExtras().getString("goodcount");
        goodcount.setText(mGoodcount);

        //String mCommentcount = intent.getExtras().getString("commentcount");
        //commentcount.setText(mCommentcount);

        // 태그 조회
        String mTag1 = intent.getExtras().getString("tag1");
        tag1.setText(mTag1);
        String mTag2 = intent.getExtras().getString("tag2");
        tag1.setText(mTag2);
        String mTag3 = intent.getExtras().getString("tag3");
        tag1.setText(mTag3);
        String mTag4 = intent.getExtras().getString("tag4");
        tag1.setText(mTag4);
        String mTag5 = intent.getExtras().getString("tag5");
        tag1.setText(mTag5);


        num = intent.getExtras().getInt("num");

        // retrofit 통신 연결 - Spring 웹 서버와 연결
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerView2 = findViewById(R.id.recycler_view2);

        jsonApi = retrofit.create(JsonApi.class);

        // 좋아요 버튼
        Button like = (Button)findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardData bd = new BoardData();
                bd.title = title.getText().toString();
                bd.question = question.getText().toString();
                bd.board_like = Integer.parseInt(goodcount.getText().toString());
                bd.board_like++;
                updateLike(bd);
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
                intent.putExtra("likecount", goodcount.getText().toString());
                intent.putExtra("tag1", tag1.getText().toString());
                intent.putExtra("tag2", tag2.getText().toString());
                intent.putExtra("tag3", tag3.getText().toString());
                intent.putExtra("tag4", tag4.getText().toString());
                intent.putExtra("tag5", tag5.getText().toString());
                Log.d("ArticleDetail - title", title.getText().toString());
                startActivity(intent);
            }
        });

        // 글 삭제 버튼
        Button delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePost(num);

                // 글 삭제 시 앞으로 넘어감
                Intent intent2=new Intent(ArticleDetail.this, ArticleBoard.class);
                String name=ArticleBoard.name;
                intent2.putExtra("values",name);
                startActivity(intent2);
            }
        });

        EditText editTextComment = (EditText)findViewById(R.id.editTextComment);

        // 댓글 등록 버튼
        Button writeComment = (Button)findViewById(R.id.writeComment);
        writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentData cd = new CommentData();
                cd.answer = editTextComment.getText().toString();
                cd.board_no = num;
                cd.board_id = id;
                cd.comment_id = "user2";
                cd.comment_like = 0;
                cd.comment_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new java.util.Date());
                if(cd.answer.equals("")) return;
                else addComment(cd);
            }
        });

        // 댓글 조회 연결
        Callback<List<CommentData>> callback = new Callback<List<CommentData>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<CommentData>> call, Response<List<CommentData>> response) {
                if(response.isSuccessful()) {
                    dataList = response.body();
                    Log.d("comment", dataList.toString());

                    recyclerViewAdapter2 = new RecyclerViewAdapter2(getApplicationContext(), dataList);
                    recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView2.setAdapter(recyclerViewAdapter2);

                    Log.d("board_no", String.valueOf(num));

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


    }

    // 글 삭제 연결
    private void deletePost(Integer no) {
        Call<Void> calls = jsonApi.deletePost(no);
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

    // 댓글 등록 연결
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

    // 글 좋아요 추가
    private void updateLike(BoardData b) {
        Call<Void> call = jsonApi.updatePost(num, b);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("Like - Board_no", String.valueOf(num));
                Toast.makeText(ArticleDetail.this, "Like updated successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

}