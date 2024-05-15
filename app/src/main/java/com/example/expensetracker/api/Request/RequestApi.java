package com.example.expensetracker.api.Request;

import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.Request;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestApi {
    @GET("request/getbyuser/{id}")
    Call<DataResponse<List<Request>>> getRequestsByUser(@Path("id") String id);
    @POST("request/response")
    Call<DataResponse<Request>> responseRequest(@Body RequestRes requestRes);
}
