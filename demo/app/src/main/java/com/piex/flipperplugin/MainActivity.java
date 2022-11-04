package com.piex.flipperplugin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.piex.flipperplugin.databinding.ActivityMainBinding;

import java.io.IOException;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    @Override
    @Nullable
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        startNetworkCall();
    }

    private void startNetworkCall() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Retrofit retrofit = new Retrofit.Builder()
                        .addConverterFactory(GsonConverterFactory.create())
                        .baseUrl(RetrofitService.BASE_URL)
                        .build();
                RetrofitService service = retrofit.create(RetrofitService.class);
                try {
                    service.getUsers().execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

