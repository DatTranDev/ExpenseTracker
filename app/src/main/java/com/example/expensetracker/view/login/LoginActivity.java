package com.example.expensetracker.view.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityLoginBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.view.SplashScreenActivity;
import com.example.expensetracker.view.register.RegisterActivity;
import com.example.expensetracker.viewmodel.LoginViewModel;
import com.google.gson.Gson;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        LoginViewModel loginViewModel = new LoginViewModel();
        binding.setLoginViewModel(loginViewModel);
        AppCompatButton loginButton = findViewById(R.id.btnFacebookLogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Tính năng đang được phát triển...", Toast.LENGTH_SHORT).show();
            }
        });

        TextView textView = findViewById(R.id.textViewRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        AppCompatButton loginButton2 = findViewById(R.id.btnGoogleLogin);
        loginButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Tính năng đang được phát triển...", Toast.LENGTH_SHORT).show();
            }
        });

        ProgressBar progressBar = findViewById(R.id.progressBar);
        loginViewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoading) {
                if(isLoading){
                    progressBar.setVisibility(View.VISIBLE);
                }else{
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        loginViewModel.getAppUserLiveData().observe(this, new Observer<AppUser>() {
            @Override
            public void onChanged(@Nullable AppUser appUser) {
                if(appUser != null){
                    SharedPreferencesManager.getInstance(LoginActivity.this).saveObject("user", appUser);
                }
            }
        });
        loginViewModel.getToastMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        loginViewModel.getIsLoggedIn().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isLoggedIn) {
                if(isLoggedIn){
                    Intent intent = new Intent(LoginActivity.this, SplashScreenActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        TextView forgotPassword = findViewById(R.id.textViewForgotPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }
}