package com.example.expensetracker.api.AppUser;

import com.example.expensetracker.model.AppUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AppUserApi {
    @POST("user/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
