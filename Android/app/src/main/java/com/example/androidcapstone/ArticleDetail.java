package com.example.androidcapstone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.androidcapstone.databinding.ActivityArticleDetailBinding;

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.androidcapstone.Login.token;

public class ArticleDetail extends AppCompatActivity {
    // 글 목록의 아이템을 누르면 나오는 글 상세 화면

    Retrofit retrofit;
    JsonApi jsonApi;
    List<CommentData> dataList;

    Integer num;

    RecyclerView recyclerView2;
    RecyclerViewAdapter2 recyclerViewAdapter2;

    MenuItem menuItem;

    TextView category;
    TextView user;
    TextView datetime;
    TextView title;
    TextView question;
    TextView tag1;
    TextView tag2;
    TextView tag3;
    TextView tag4;
    TextView tag5;
    TextView goodcount;
    TextView commentcount;
    ImageView boardImage;

    String username;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.154.52:8080";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        // titlebar 없애기
        ActionBar bar = getSupportActionBar();
        bar.hide();

        category = (TextView)findViewById(R.id.category);
        user = (TextView)findViewById(R.id.user);
        datetime = (TextView)findViewById(R.id.datetime);
        title = (TextView)findViewById(R.id.title);
        question = (TextView)findViewById(R.id.question);
        tag1 = (TextView)findViewById(R.id.tag1);
        tag2 = (TextView)findViewById(R.id.tag2);
        tag3 = (TextView)findViewById(R.id.tag3);
        tag4 = (TextView)findViewById(R.id.tag4);
        tag5 = (TextView)findViewById(R.id.tag5);
        goodcount = (TextView)findViewById(R.id.goodcount);
        commentcount = (TextView)findViewById(R.id.commentcount);
        boardImage = (ImageView)findViewById(R.id.boardImage);

        // ExpertFragment - RecyclerViewAdapter에서 받아옴
        Intent intent = getIntent();

        String c = intent.getExtras().getString("category");
        category.setText(c);

        String id = intent.getExtras().getString("board_id");
        user.setText(" "+id);

        String mTitle = intent.getExtras().getString("title");
        title.setText(mTitle);

        String mQuestion = intent.getExtras().getString("question");
        question.setText(mQuestion);

        String mDatetime = intent.getExtras().getString("datetime");
        datetime.setText(mDatetime);

        String mGoodcount = intent.getExtras().getString("goodcount");
        goodcount.setText(mGoodcount);

        String mCommentcount = intent.getExtras().getString("commentcount");
        commentcount.setText(mCommentcount);

        String mTag1 = intent.getExtras().getString("tag1");
        tag1.setText(mTag1);

        String mTag2 = intent.getExtras().getString("tag2");
        tag2.setText(mTag2);

        String mTag3 = intent.getExtras().getString("tag3");
        tag3.setText(mTag3);

        String mTag4 = intent.getExtras().getString("tag4");
        tag4.setText(mTag4);

        String mTag5 = intent.getExtras().getString("tag5");
        tag5.setText(mTag5);

        // 이미지 조회 (drawble말고 assets사용한 bitmap)
        String mFilepath = intent.getExtras().getString("filepath");
        AssetManager am = getResources().getAssets();
        InputStream is = null;
        try {
            if(mFilepath == null)   is = am.open("noimg.PNG");
            else is = am.open(mFilepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bm = BitmapFactory.decodeStream(is);
        boardImage.setImageBitmap(bm);

        num = intent.getExtras().getInt("num");

        recyclerView2 = findViewById(R.id.recycler_view2);

        jsonApi = ServiceGenerator.createService(JsonApi.class, token);

        username = loadUsername();

        // 좋아요 버튼
        Button like = (Button)findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 좋아요 수만 1 늘리고 나머지값은 그대로 똑같이 받아서 보내기
                BoardData bd = new BoardData();
                bd.title = title.getText().toString();
                bd.question = question.getText().toString();
                bd.board_like = Integer.parseInt(goodcount.getText().toString());
                bd.board_like++;
                bd.tag1 = tag1.getText().toString();
                bd.tag2 = tag2.getText().toString();
                bd.tag3 = tag3.getText().toString();
                bd.tag4 = tag4.getText().toString();
                bd.tag5 = tag5.getText().toString();
                updateLike(bd);
            }
        });

        // 글 수정 버튼
        Button update = (Button)findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인된 username이랑 글 작성자의 id가 같다면 수정 가능
                if(username.equals(id)) {
                    // WritingBoard로 모든 값 그대로 넘겨주기 + "edit"라고 수정 모드라는 걸 넘겨주기
                    Intent intent = new Intent(getApplicationContext(), WritingBoard.class);
                    intent.putExtra("mode", "edit");
                    intent.putExtra("board_no", num);
                    intent.putExtra("title", title.getText().toString());
                    intent.putExtra("question", question.getText().toString());
                    intent.putExtra("likecount", goodcount.getText().toString());
                    intent.putExtra("commentcount", commentcount.getText().toString());
                    intent.putExtra("tag1", tag1.getText().toString());
                    intent.putExtra("tag2", tag2.getText().toString());
                    intent.putExtra("tag3", tag3.getText().toString());
                    intent.putExtra("tag4", tag4.getText().toString());
                    intent.putExtra("tag5", tag5.getText().toString());
                    Log.d("ArticleDetail - title", title.getText().toString());
                    startActivity(intent);
                } else {
                    Log.d("ArticleDetail - username & id", username + " & " + id);
                    Toast.makeText(ArticleDetail.this, "수정할 수 있는 권한이 없습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 글 삭제 버튼
        Button delete = (Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인된 username이랑 글 작성자의 id가 같다면 삭제 가능
                if(username.equals(id)) {
                    deletePost(num);

                    // 글 삭제 시 앞으로 넘어감
                    Intent intent2 = new Intent(ArticleDetail.this, ArticleBoard.class);
                    String name = ArticleBoard.name;
                    intent2.putExtra("values", name);
                    startActivity(intent2);
                } else {
                    Log.d("ArticleDetail - username & id", username + " & " + id);
                    Toast.makeText(ArticleDetail.this, "삭제할 수 있는 권한이 없습니다.", Toast.LENGTH_LONG).show();
                }
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
                cd.comment_id = username;
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
                    Log.d("comment", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<CommentData>> call, Throwable t) {
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

    // token 보내고 username 받아오기
    private String loadUsername(){
        Callback<Username> call = new Callback<Username>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Username> call, Response<Username> response) {
                if(response.isSuccessful()) {
                    Log.i("PromotionDetail - username", response.body().getName());
                    username = response.body().getName();
                } else {
                    Log.e("PromotionDetail - getUsername", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Username> call, Throwable t) {
                Log.e("PromotionDetail - getUsername fail", t.getMessage());
            }
        };
        jsonApi.getUsername().enqueue(call);

        return username;
    }

}