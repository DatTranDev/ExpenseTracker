package com.example.expensetracker.api.Transaction;

import android.view.SurfaceControl;

import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.api.ResMessage;
import com.example.expensetracker.model.TransactionExp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TransactionApi {
    @POST("/transaction/add")
    Call<DataResponse<TransactionExp>> addTransaction(@Body TransactionExp transaction);
    @DELETE("/transaction/delete/{id}")
    Call<DataResponse<TransactionExp>> deleteTransaction(@Path("id") String id);
    @PATCH("/transaction/update/{id}")
    Call<DataResponse<TransactionExp>> updateTransaction(@Path("id") String id, @Body TransactionExp transaction);
}
