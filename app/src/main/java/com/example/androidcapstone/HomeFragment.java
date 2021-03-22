package com.example.androidcapstone;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment {
    // 홈 화면

    Retrofit retrofit;
    JsonApi jsonApi;
    List<BoardData> dataList;

    String data;

    View mView;
    RecyclerView recyclerView;
    HotRecyclerViewAdapter hotRecyclerViewAdapter;


    static final String URL = "http://192.168.35.91:8080";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        // 홈에서 검색 버튼 누르면 전체 게사글 검색 화면으로 넘어감
        Button button=(Button)mView.findViewById(R.id.buttonsearch);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                // Create new fragment and transaction
                Fragment newFragment = new SearchFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.main_layout, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });


        recyclerView = (RecyclerView)mView.findViewById(R.id.hot_recyclerview);

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
                    Log.d("Hot-Board", dataList.toString());

                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    hotRecyclerViewAdapter = new HotRecyclerViewAdapter(getActivity(), dataList);
                    recyclerView.setAdapter(hotRecyclerViewAdapter);
                } else {
                    Log.d("log", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<BoardData>> call, Throwable t) {
                Log.d("log", "Fail");
            }
        };
        jsonApi.getBoard().enqueue(callback);



        return mView;
    }
}