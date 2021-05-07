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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    private Spinner spinner;
    private String[] schools = new String[]{"all","title"};

    Retrofit retrofit;
    JsonApi jsonApi;
    List<BoardData> dataList2;



    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    EditText editSearch;
    EditText textView;

    //static final String URL = "http://223.194.158.215:8080";
    static final String URL = "http://192.168.35.91:8080";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);

        /*
        // 스피너
        final String[] spi = new String[1];
        spinner= (Spinner)view.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,schools);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //Toast.makeText(getActivity(),Integer.toString(position),Toast.LENGTH_SHORT); //본인이 원하는 작업.
                spi[0] =schools[position];
                Log.i("what is that", spi[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

         */


        // retrofit 통신 연결 - Spring 웹 서버와 연결
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        recyclerView = view.findViewById(R.id.recycler_view);

        jsonApi = retrofit.create(JsonApi.class);


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
                                // dataList.notifyAll();
                                dataList2 = response.body();
                                Log.d("ExpertFragment", dataList2.toString());

                                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                            dataList2.notifyAll();
                                recyclerViewAdapter = new RecyclerViewAdapter(getContext(), dataList2);
                                recyclerView.setAdapter(recyclerViewAdapter);

                                //textView = (TextView)mView.findViewById(R.id.text);
                                //textView.setText(response.body().toString());
                            } else {
                                Log.d("log", "Status Code " + response.code());
                                //Log.d("log", textView.getText().toString());
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

        Button button = (Button)view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Callback<List<BoardData>> callback = new Callback<List<BoardData>>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(Call<List<BoardData>> call, Response<List<BoardData>> response) {
                        if (response.isSuccessful()) {
                            // dataList.notifyAll();
                            dataList2 = response.body();
                            Log.d("ExpertFragment", dataList2.toString());

                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                            dataList2.notifyAll();
                            recyclerViewAdapter = new RecyclerViewAdapter(getContext(), dataList2);
                            recyclerView.setAdapter(recyclerViewAdapter);

                            //textView = (TextView)mView.findViewById(R.id.text);
                            //textView.setText(response.body().toString());
                        } else {
                            Log.d("log", "Status Code " + response.code());
                            //Log.d("log", textView.getText().toString());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<BoardData>> call, Throwable t) {
                        Log.d("log", "Fail");
                        //t.printStackTrace();
                    }
                };
                jsonApi.getSearchBoards(textView.getText().toString()).enqueue(callback);
            }
        });
        return view;
    }
}