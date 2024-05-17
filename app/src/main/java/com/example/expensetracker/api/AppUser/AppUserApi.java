package com.example.expensetracker.api.AppUser;

import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AppUserApi {
    @POST("user/login")
    Call<DataResponse<AppUser>> login(@Body AppUser loginRequest);
    @POST("user/register")
    Call<DataResponse<AppUser>> register(@Body AppUser appUser);
    @PATCH("user/changepassword")
    Call<DataResponse<AppUser>> changePassword(@Body AppUser appUser);
    @PATCH("user/update/{id}")
    Call<DataResponse<AppUser>> update(@Path ("id") String id, @Body AppUser appUser);
    @GET("user/findbyemail/{email}")
    Call<DataResponse<AppUser>> findByEmail(@Path("email") String email);
    @GET("user/findbyid/{id}")
    Call<DataResponse<AppUser>> findById(@Path ("id") String id);

    //Find feature
    @GET("user/getcategory/{id}")
    Call<DataResponse<List<Category>>> getCategory(@Path ("id") String id);
    @GET("user/getwallet/{id}")
    Call<DataResponse<List<Wallet>>> getWallet(@Path ("id") String id);
    @GET("user/getsharingwallet/{id}")
    Call<DataResponse<List<Wallet>>> getSharingWallet(@Path ("id") String id);
    @GET("user/gettransaction/{id}")
    Call<DataResponse<List<TransactionExp>>> getTransaction(@Path ("id") String id);
    @GET("user/getbudget/{id}")
    Call<DataResponse<List<Budget>>> getBudget(@Path ("id") String id);
}
