package com.example.expensetracker.repository;

import android.widget.Toast;

import com.example.expensetracker.api.AppUser.AppUserApi;
import com.example.expensetracker.api.AppUser.LoginRequest;
import com.example.expensetracker.api.AppUser.LoginResponse;
import com.example.expensetracker.api.AppUser.UserCallBack;
import com.example.expensetracker.api.RetrofitClient;
import com.example.expensetracker.model.AppUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class AppUserRepository {
    private static AppUserRepository appUserRepository;
    private AppUserApi appUserApi;
    public AppUserRepository() {
        appUserApi = RetrofitClient.getClient().create(AppUserApi.class);
    }
    public static synchronized AppUserRepository getInstance() {
        if (appUserRepository == null) {
            appUserRepository = new AppUserRepository();
        }
        return appUserRepository;
    }
    public synchronized void login(String email, String password, UserCallBack userCallBack) {
        LoginRequest loginRequest = new LoginRequest(email, password);
        appUserApi.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    AppUser appUser = loginResponse.getData();
                    userCallBack.onSuccess(appUser);
                } else {
                    userCallBack.onError("Invalid email or password");
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                userCallBack.onError("Server error, please try again later: "+t.getMessage());
            }
        });
    }
}
