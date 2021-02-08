package com.example.androidcapstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
    Button button;
    EditText editTextTitle;
    EditText editTextMultiLineBoard;

    String category;
    Integer board_no;

    JsonApi jsonApi;
    Retrofit retrofit;

    static final String URL = "http://192.168.35.91:8080";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writing_board);

        Intent intent = getIntent();
        String mode = intent.getExtras().getString("mode");

        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextMultiLineBoard = (EditText)findViewById(R.id.editTextMultiLineBoard);
        button = (Button)findViewById(R.id.submit);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApi = retrofit.create(JsonApi.class);

        String s1="edit";
        if(mode.equals(s1)){
            button.setText("수정");
            board_no=intent.getExtras().getInt("board_no");
            editTextTitle.setText(intent.getExtras().getString("title"));
            editTextMultiLineBoard.setText(intent.getExtras().getString("question"));
        }
        else {

            button.setText("등록");
        }


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BoardData bd = new BoardData();
                bd.title=editTextTitle.getText().toString();
                bd.question=editTextMultiLineBoard.getText().toString();
                if(mode.equals(s1)) {
                    updateBoard(bd);
                }
                else {
                    bd.id="user3";
                    bd.board_like=0;
                    bd.category=ArticleBoard.name;
                    bd.board_date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new java.util.Date());

                    addBoard(bd);
                }
                Intent intent2=new Intent(WritingBoard.this, ArticleBoard.class);
                String name=ArticleBoard.name;
                intent2.putExtra("values",name);
                startActivity(intent2);
            }
        });


    }

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