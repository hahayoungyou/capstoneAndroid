package com.example.androidcapstone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    JsonApi jsonApi;
    Retrofit retrofit;

    static final String URL = "http://192.168.35.91:8080";
    //static final String URL = "http://223.194.154.52:8080";

    EditText editTextID;
    EditText editTextPwd;
    Button loginSubmit;

    static String token;

    public static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mContext = this;

        // titlebar 없애기
        ActionBar bar = getSupportActionBar();
        bar.hide();

        editTextID = (EditText)findViewById(R.id.editTextID);
        editTextPwd = (EditText)findViewById(R.id.editTextPwd);
        loginSubmit = (Button)findViewById(R.id.loginSubmit);

        // 이 class에서만 retrofit 연결 방식 약간 다름
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonApi = retrofit.create(JsonApi.class);

        // login 버튼 클릭 시
        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDto u = new UserDto();
                u.setUserName(editTextID.getText().toString());
                u.setPassword(editTextPwd.getText().toString());

                loadAll(u);
            }
        });
    }

    private void loadAll(UserDto userDto) {
        // token 가져오기
        jsonApi.SignIn(userDto).enqueue(new Callback<AuthenticationResponse>() {
            @Override
            public void onResponse(Call<AuthenticationResponse> call, Response<AuthenticationResponse> response) {
                token = response.body().getJwt();

                Log.i("token", token);

                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra("token", token);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i("fail",t.getMessage());
            }


        });

    }


    public static String getTokens() { return token; }

}
