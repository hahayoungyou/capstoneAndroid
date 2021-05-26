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
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
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
    List list;

    String data;

    View mView;
    RecyclerView recyclerView;
    HotRecyclerViewAdapter hotRecyclerViewAdapter;

    TextView tag01;
    TextView tag02;
    TextView tag03;
    TextView tag04;
    TextView tag05;

    String token;
    static String str;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.154.52:8080";
    
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        // 홈에서 검색 버튼 누르면 전체 게사글 검색 화면으로 넘어감
        Button button = (Button) mView.findViewById(R.id.buttonsearch);
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

        MainActivity activity = (MainActivity) getActivity();
        token = activity.getMyToken();

        recyclerView = (RecyclerView) mView.findViewById(R.id.hot_recyclerview);

        jsonApi = ServiceGenerator.createService(JsonApi.class, token);

        tag01 = (TextView) mView.findViewById(R.id.tag01);
        tag02 = (TextView) mView.findViewById(R.id.tag02);
        tag03 = (TextView) mView.findViewById(R.id.tag03);
        tag04 = (TextView) mView.findViewById(R.id.tag04);
        tag05 = (TextView) mView.findViewById(R.id.tag05);

        return loadStores();
    }

    public View loadStores() {
        // 인기 태그 web-server와 연결
        Callback<List> callbacks = new Callback<List>() {
            @Override
            public void onResponse(Call<List> call, Response<List> response) {
                if(response.isSuccessful()) {
                    list = response.body();
                    Log.d("tag", list.toString());

                    String str01 = list.get(0).toString();
                    String str1 = str01.substring(1, str01.indexOf(","));
                    String str02 = list.get(1).toString();
                    String str2 = str02.substring(1, str02.indexOf(","));
                    String str03 = list.get(2).toString();
                    String str3 = str03.substring(1, str03.indexOf(","));
                    String str04 = list.get(3).toString();
                    String str4 = str04.substring(1, str04.indexOf(","));
                    String str05 = list.get(4).toString();
                    String str5 = str05.substring(1, str05.indexOf(","));

                    tag01.setText(str1);
                    tag02.setText(str2);
                    tag03.setText(str3);
                    tag04.setText(str4);
                    tag05.setText(str5);
                } else {
                    Log.d("tag", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List> call, Throwable t) {
                Log.d("tag", t.getMessage());
            }
        };
        jsonApi.getPopularTag().enqueue(callbacks);

        // hot 게시물 web-server와 연결
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
                    Log.d("hot", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<List<BoardData>> call, Throwable t) {
                Log.d("hot", t.getMessage());
            }
        };
        jsonApi.getHotBoard().enqueue(callback);

        // token 보내고 username 받아오기
        Callback<Username> call = new Callback<Username>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Username> call, Response<Username> response) {
                if(response.isSuccessful()) {
                    Log.i("HomeFragment - username", response.body().getName());
                    str = response.body().getName();
                    //str[0] = String.valueOf(response.body());
                } else {
                    Log.e("HomeFragment - getUsername", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Username> call, Throwable t) {
                Log.e("HomeFragment - getUsername fail", t.getMessage());
            }
        };
        jsonApi.getUsername().enqueue(call);

        return mView;
    }

}