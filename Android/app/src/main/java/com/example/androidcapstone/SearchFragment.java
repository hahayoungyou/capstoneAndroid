package com.example.androidcapstone;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.androidcapstone.Login.token;

public class SearchFragment extends Fragment {

    private Spinner spinner;
    private String[] schools = new String[]{"all","title"};

    JsonApi jsonApi;
    List<BoardData> dataList2;

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    EditText editSearch;
    EditText textView;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.154.52:8080";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        jsonApi = ServiceGenerator.createService(JsonApi.class, token);

        editSearch = (EditText)view.findViewById(R.id.search);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);

        textView = (EditText)view.findViewById(R.id.search);
        textView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Enter키 눌렀을 때 검색 버튼 눌리는 기능이랑 똑같이.
                    Callback<List<BoardData>> callback = new Callback<List<BoardData>>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(Call<List<BoardData>> call, Response<List<BoardData>> response) {
                            if (response.isSuccessful()) {
                                dataList2 = response.body();
                                Log.d("ExpertFragment", dataList2.toString());

                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                recyclerViewAdapter = new RecyclerViewAdapter(getContext(), dataList2);
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
                    jsonApi.getSearchBoards(textView.getText().toString()).enqueue(callback);
                    return true;
                }
                return false;
            }
        });

        // 검색 버튼 눌렀을 때 editText에 있는 것들이 제목이나 질문에 포함되면 조회
        Button button = (Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback<List<BoardData>> callback = new Callback<List<BoardData>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<List<BoardData>> call, Response<List<BoardData>> response) {
                        if (response.isSuccessful()) {
                            dataList2 = response.body();
                            Log.d("ExpertFragment", dataList2.toString());

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            recyclerViewAdapter = new RecyclerViewAdapter(getContext(), dataList2);
                            recyclerView.setAdapter(recyclerViewAdapter);
                        } else {
                            Log.d("log", "Status Code " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BoardData>> call, Throwable t) {
                        Log.d("log", "Fail");
                    }
                };
                jsonApi.getSearchBoards(textView.getText().toString()).enqueue(callback);
            }
        });
        return view;
    }
}