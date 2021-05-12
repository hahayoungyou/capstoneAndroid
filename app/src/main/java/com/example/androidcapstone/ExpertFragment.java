package com.example.androidcapstone;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ExpertFragment extends Fragment implements TextWatcher {
    // 글 목록

    Retrofit retrofit;
    JsonApi jsonApi;
    List<BoardData> dataList;

    String data;

    View mView;
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    Button button;
    EditText editSearch;
    TextView textView;


    //static final String URL = "http://223.194.158.215:8080";
    static String URL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_expert, container, false);

        // 연필 모양 버튼 누르면 글 작성 액티비티로 넘어감
        button = (Button)mView.findViewById(R.id.write);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ExpertFragment", "button");
                // 선택된 category 값을 보내주면서 WritingBoard를 시작
                Intent intent = new Intent(getContext(), WritingBoard.class);

                intent.putExtra("mode", "else");
                startActivity(intent);
            }
        });

        //editSearch = (EditText)mView.findViewById(R.id.search);
        recyclerView = (RecyclerView)mView.findViewById(R.id.recycler_view);


        ArticleBoard activity = (ArticleBoard) getActivity();
        data = activity.getMyData();

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        jsonApi = retrofit.create(JsonApi.class);

        Callback<List<BoardData>> callback = new Callback<List<BoardData>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<BoardData>> call, Response<List<BoardData>> response) {
                if(response.isSuccessful()) {
                    dataList = response.body();
                    Log.d("ExpertFragment", dataList.toString());

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerViewAdapter = new RecyclerViewAdapter(getActivity(), dataList);
                    recyclerView.setAdapter(recyclerViewAdapter);

                } else {
                    Log.d("log", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<BoardData>> call, Throwable t) {
                Log.d("log", "Fail");
                //t.printStackTrace();
            }
        };
        jsonApi.getBoard(data).enqueue(callback);


        //editSearch.addTextChangedListener(this);

        // Inflate the layout for this fragment
        return  mView;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}