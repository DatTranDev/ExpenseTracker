package com.example.expensetracker.api.IconInApp;

import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.Icon;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IconApi {
    @GET("icon/all")
    Call<DataResponse<List<Icon>>> getAllIcons();
    @GET("icon/{id}")
    Call<DataResponse<Icon>> getIconById(@Path("id") String id);
}
