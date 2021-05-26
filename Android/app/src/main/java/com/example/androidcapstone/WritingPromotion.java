package com.example.androidcapstone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.androidcapstone.Login.token;

public class WritingPromotion extends AppCompatActivity {
    Button imageUploadButton;
    Button button;
    EditText editTextTitle;
    EditText editTextQuestion;
    ImageView promotionImage;

    private final int GET_GALLERY_IMAGE = 200;

    Integer board_no;

    JsonApi jsonApi;
    Retrofit retrofit;

    String username;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.154.52:8080";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_promotion);

        // titlebar 없애기
        ActionBar bar = getSupportActionBar();
        bar.hide();

        Intent intent = getIntent();
        String mode = intent.getExtras().getString("mode");

        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextQuestion = (EditText)findViewById(R.id.editTextMultiLineBoard);
        button = (Button)findViewById(R.id.submit);
        imageUploadButton = (Button)findViewById(R.id.imageUploadButton);
        promotionImage = (ImageView) findViewById(R.id.promotionUploadImage);

        jsonApi = ServiceGenerator.createService(JsonApi.class, token);

        username = loadUsername();
        // imageUploadButton을 누르면 갤러리에 있는 이미지로 선택할 수 있도록.
        imageUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, GET_GALLERY_IMAGE);
            }
        });

        String edit="edit";
        if(mode.equals(edit)){ // 글 수정 시
            button.setText("수정");
            board_no=intent.getExtras().getInt("board_no");
            editTextTitle.setText(intent.getExtras().getString("title"));
            editTextQuestion.setText(intent.getExtras().getString("question"));

        }
        else { // 글 등록 시
            button.setText("등록");
        }

        // 글 등록 또는 수정 완료 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardData bd = new BoardData();
                bd.title = editTextTitle.getText().toString();
                bd.question = editTextQuestion.getText().toString();

                if(mode.equals(edit)) { // 글 수정 시
                    updateBoard(bd);
                }
                else { // 글 등록 시
                    bd.id = username;
                    bd.category = "홍보게시판";
                    bd.board_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new java.util.Date());
                    bd.filepath = "b.PNG";
                    addBoard(bd);
                }

                // 글 수정 또는 등록 시 앞으로 넘어감
                Intent intent2 = new Intent(WritingPromotion.this, PromotionBoard.class);
                String name = PromotionBoard.name;
                intent2.putExtra("values",name);
                startActivity(intent2);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // imageLoad
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_GALLERY_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            promotionImage.setImageURI(selectedImageUri);
        }

    }

    // 글 등록 연결
    private void addBoard(BoardData b) {
        Call<BoardData> call = jsonApi.addPost(b);
        call.enqueue(new Callback<BoardData>() {
            @Override
            public void onResponse(Call<BoardData> call, Response<BoardData> response) {
                Toast.makeText(WritingPromotion.this, "Board created successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<BoardData> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    // 글 수정 연결
    private void updateBoard(BoardData b) {
        Call<Void> call = jsonApi.updatePost(board_no, b);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("Wrinting - Board_no", String.valueOf(board_no));
                Toast.makeText(WritingPromotion.this, "Board updated successfully", Toast.LENGTH_LONG).show();
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