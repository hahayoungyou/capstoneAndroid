package com.example.androidcapstone;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.androidcapstone.Login.token;


public class InfoFragment extends Fragment {
    // 회원 정보 화면

    Retrofit retrofit;
    JsonApi jsonApi;

    View mView;
    TextView ID;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.154.52:8080";
    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_info, container, false);

        ID = (TextView) mView.findViewById(R.id.ID);

        jsonApi = ServiceGenerator.createService(JsonApi.class, token);

        return loadStores();
    }

    public View loadStores() {
        // token 보내고 username 받아오기
        Callback<Username> call = new Callback<Username>(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<Username> call, Response<Username> response) {
                if(response.isSuccessful()) {
                    Log.i("InfoFragment - username", response.body().getName());
                    ID.setText(response.body().getName());
                } else {
                    Log.e("InfoFragment - getUsername", "Status Code " + response.code());
                }
            }
            @Override
            public void onFailure(Call<Username> call, Throwable t) {
                Log.e("InfoFragment - getUsername fail", t.getMessage());
            }
        };
        jsonApi.getUsername().enqueue(call);


        // Inflate the layout for this fragment
        return  mView;
    }
}