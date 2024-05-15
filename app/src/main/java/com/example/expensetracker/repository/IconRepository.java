package com.example.expensetracker.repository;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.api.IconInApp.IconApi;
import com.example.expensetracker.api.RetrofitClient;
import com.example.expensetracker.model.Icon;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class IconRepository {
    private static IconRepository iconRepository;
    private IconApi iconApi;

    private IconRepository() {
        iconApi = RetrofitClient.getClient().create(IconApi.class);
    }

    public static synchronized IconRepository getInstance() {
        if (iconRepository == null) {
            iconRepository = new IconRepository();
        }
        return iconRepository;
    }

    public synchronized void getAllIcons(ApiCallBack<List<Icon>> callback) {
        iconApi.getAllIcons().enqueue(new Callback<DataResponse<List<Icon>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<Icon>>> call, retrofit2.Response<DataResponse<List<Icon>>> response) {
                if (response.isSuccessful()) {
                    DataResponse<List<Icon>> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to fetch icons");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<Icon>>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void getIconById(String id, ApiCallBack<Icon> callback) {
        iconApi.getIconById(id).enqueue(new Callback<DataResponse<Icon>>() {
            @Override
            public void onResponse(Call<DataResponse<Icon>> call, retrofit2.Response<DataResponse<Icon>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Icon> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to fetch icon");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Icon>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }
}
