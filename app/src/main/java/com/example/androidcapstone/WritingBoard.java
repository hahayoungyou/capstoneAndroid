package com.example.androidcapstone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.androidcapstone.ExpertFragment.URL;
import static com.example.androidcapstone.RecyclerViewAdapter2.num;

public class WritingBoard extends AppCompatActivity {
    Button imageUploadButton;
    Button button;
    EditText editTextTitle;
    EditText editTextMultiLineBoard;
    ImageView newImage;

    private final int GET_GALLERY_IMAGE = 200;

    EditText tag1;
    EditText tag2;
    EditText tag3;
    EditText tag4;
    EditText tag5;

    Integer board_no;
    Integer likecount;

    JsonApi jsonApi;
    Retrofit retrofit;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.158.215:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_board);

        // titlebar 없애기
        ActionBar bar = getSupportActionBar();
        bar.hide();

        Intent intent = getIntent();
        String mode = intent.getExtras().getString("mode");

        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextMultiLineBoard = (EditText)findViewById(R.id.editTextMultiLineBoard);
        button = (Button)findViewById(R.id.submit);
        imageUploadButton = (Button)findViewById(R.id.imageUploadButton);
        newImage = (ImageView)findViewById(R.id.newImage);

        tag1 = (EditText)findViewById(R.id.tag1);
        //if(tag1.length() == 0)  tag1 = null;
        tag2 = (EditText)findViewById(R.id.tag2);
        tag3 = (EditText)findViewById(R.id.tag3);
        tag4 = (EditText)findViewById(R.id.tag4);
        tag5 = (EditText)findViewById(R.id.tag5);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApi = retrofit.create(JsonApi.class);

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
            editTextMultiLineBoard.setText(intent.getExtras().getString("question"));
            likecount = Integer.parseInt(intent.getExtras().getString("likecount"));
            tag1.setText(intent.getExtras().getString("tag1"));
            tag2.setText(intent.getExtras().getString("tag2"));
            tag3.setText(intent.getExtras().getString("tag3"));
            tag4.setText(intent.getExtras().getString("tag4"));
            tag5.setText(intent.getExtras().getString("tag5"));

        }
        else { // 글 등록 시
            button.setText("등록");
        }

        // 글 등록 또는 수정 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardData bd = new BoardData();
                bd.title = editTextTitle.getText().toString();
                bd.question = editTextMultiLineBoard.getText().toString();
                bd.board_like = likecount;
                /*
                if(tag1 == null) {
                    tag1.setText(" ");
                }

                 */
                bd.tag1 = tag1.getText().toString();
                bd.tag2 = tag2.getText().toString();
                bd.tag3 = tag3.getText().toString();
                bd.tag4 = tag4.getText().toString();
                bd.tag5 = tag5.getText().toString();

                if(mode.equals(edit)) { // 글 수정 시
                    updateBoard(bd);
                }
                else { // 글 등록 시
                    bd.id="user3";
                    bd.board_like=0;
                    bd.category=ArticleBoard.name;
                    bd.board_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new java.util.Date());
                    bd.filepath = "b.PNG";
                    addBoard(bd);
                }

                // 글 수정 또는 등록 시 앞으로 넘어감
                Intent intent2=new Intent(WritingBoard.this, ArticleBoard.class);
                String name=ArticleBoard.name;
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
            newImage.setImageURI(selectedImageUri);
        }

    }

    // 글 등록 연결
    private void addBoard(BoardData b) {
        Call<BoardData> call = jsonApi.addPost(b);
        call.enqueue(new Callback<BoardData>() {
            @Override
            public void onResponse(Call<BoardData> call, Response<BoardData> response) {
                Toast.makeText(WritingBoard.this, "Board created successfully", Toast.LENGTH_LONG).show();
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
                Toast.makeText(WritingBoard.this, "Board updated successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

}