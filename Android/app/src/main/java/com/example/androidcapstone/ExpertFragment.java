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
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.androidcapstone.Login.token;


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
    TextView category;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.154.52:8080";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_expert, container, false);

        ArticleBoard activity = (ArticleBoard) getActivity();
        data = activity.getMyData();

        category = (TextView) mView.findViewById(R.id.category);
        category.setText(data);

        // 버튼 누르면 글 작성 액티비티로 넘어감
        button = (Button) mView.findViewById(R.id.write);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ExpertFragment", "button");
                // 선택된 mode 값을 보내주면서 WritingBoard를 시작
                Intent intent = new Intent(getContext(), WritingBoard.class);
                intent.putExtra("mode", "else");
                startActivity(intent);
            }
        });

        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view);

        jsonApi = ServiceGenerator.createService(JsonApi.class, token);

        return loadStores();
    }

    public View loadStores() {

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

        // token 보내고 username 받아오기
        Callback<Username> call = new Callback<Username>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Username> call, Response<Username> response) {
                if(response.isSuccessful()) {
                    Log.i("ExpertFragment - username", response.body().getName());

                } else {
                    Log.e("ExpertFragment - getUsername", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Username> call, Throwable t) {
                Log.e("ExpertFragment - getUsername fail", t.getMessage());
            }
        };
        jsonApi.getUsername().enqueue(call);

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