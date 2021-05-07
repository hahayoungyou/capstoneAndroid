package com.example.androidcapstone;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Login extends AppCompatActivity {
    JsonApi jsonApi;
    Retrofit retrofit;

    static final String URL = "http://192.168.35.91:8080";

    EditText editTextID;
    EditText editTextPwd;
    Button loginSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // titlebar 없애기
        ActionBar bar = getSupportActionBar();
        bar.hide();

        editTextID = (EditText)findViewById(R.id.editTextID);
        editTextPwd = (EditText)findViewById(R.id.editTextPwd);
        loginSubmit = (Button)findViewById(R.id.loginSubmit);

        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        loginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDto u = new UserDto();
                //u.userName =
                u.setUserName(editTextID.getText().toString());
                u.setPassword(editTextPwd.getText().toString());

                /*
                String TOKEN = getToken(); // access token 가져오는 함수 직접 만들기
                jsonApi = ServiceGenerator.createService(JsonApi.class, TOKEN);

                 */
                loadAll(u);
            }
        });


    }

    private void loadAll(UserDto userDto) {
        jsonApi.getUser(userDto).enqueue(new Callback<UserDto>() {
            @Override
            public void onResponse(Call<UserDto> call, Response<UserDto> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(Login.this, "Login success", Toast.LENGTH_LONG).show();
                } else {
                    Log.d("REST FAILED MESSAGE", response.message());
                }
            }

            @Override
            public void onFailure(Call<UserDto> call, Throwable t) {
                Log.d("RESTM ERROR!", t.getMessage());
            }
        });
    }


}
