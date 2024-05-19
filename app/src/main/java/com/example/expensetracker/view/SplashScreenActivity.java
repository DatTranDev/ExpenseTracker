package com.example.expensetracker.view;

import android.content.SharedPreferences;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.Icon;
import com.example.expensetracker.repository.IconRepository;
import com.example.expensetracker.view.register.WelcomeActivity;
import com.google.gson.Gson;

import java.util.List;

public class SplashScreenActivity extends AppCompatActivity{
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Button retry = findViewById(R.id.reloadButton);
        retry.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                IconRepository.getInstance().getAllIcons(new ApiCallBack<List<Icon>>() {
                    @Override
                    public void onSuccess(List<Icon> response) {
                        new android.os.Handler().postDelayed(new Runnable() {
                            public void run() {
                                android.content.Intent intent = new android.content.Intent(SplashScreenActivity.this, WelcomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 500);
                    }

                    @Override
                    public void onError(String message) {
                        android.widget.Toast.makeText(SplashScreenActivity.this, message, android.widget.Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        IconRepository.getInstance().getAllIcons(new ApiCallBack<List<Icon>>() {
            @Override
            public void onSuccess(List<Icon> response) {
                SharedPreferences sharedPreferences = getSharedPreferences("icons", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("icons", new Gson().toJson(response));
                editor.apply();
                new android.os.Handler().postDelayed(new Runnable() {
                    public void run() {
                        android.content.Intent intent = new android.content.Intent(SplashScreenActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 500);
            }
            @Override
            public void onError(String message) {
                android.widget.Toast.makeText(SplashScreenActivity.this, message, android.widget.Toast.LENGTH_SHORT).show();
                retry.setVisibility(android.view.View.VISIBLE);
            }
        });
    }
}
