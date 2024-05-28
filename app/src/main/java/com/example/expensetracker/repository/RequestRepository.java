package com.example.expensetracker.repository;

import com.example.expensetracker.api.Request.RequestApi;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.api.Request.RequestRes;
import com.example.expensetracker.api.RetrofitClient;
import com.example.expensetracker.model.Request;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class RequestRepository {
    private static RequestRepository requestRepository;
    private RequestApi requestApi;

    private RequestRepository() {
        requestApi = RetrofitClient.getClient().create(RequestApi.class);
    }

    public static synchronized RequestRepository getInstance() {
        if (requestRepository == null) {
            requestRepository = new RequestRepository();
        }
        return requestRepository;
    }

    public synchronized void getRequestsByUser(String id, ApiCallBack<List<Request>> callback) {
        requestApi.getRequestsByUser(id).enqueue(new Callback<DataResponse<List<Request>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<Request>>> call, retrofit2.Response<DataResponse<List<Request>>> response) {
                if (response.isSuccessful()) {
                    DataResponse<List<Request>> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to fetch requests by user");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<Request>>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void responseRequest(RequestRes requestRes, ApiCallBack<Request> callback) {
        requestApi.responseRequest(requestRes).enqueue(new Callback<DataResponse<Request>>() {
            @Override
            public synchronized void onResponse(Call<DataResponse<Request>> call, retrofit2.Response<DataResponse<Request>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Request> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    if (response.code() == 400) {
                        try {
                            // Parse the error body if the status code is 400
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to respond to request");
                        }
                    } else {
                        callback.onError("Failed to respond to request");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Request>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }
}
