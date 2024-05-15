package com.example.expensetracker.api.Budget;

import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.Budget;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BudgetApi {
    @POST("budget/add")
    Call<DataResponse<Budget>> addBudget(@Body Budget budget);
    @PATCH("budget/update/{id}")
    Call<DataResponse<Budget>> updateBudget(@Body Budget budget);
    @DELETE("budget/delete/{id}")
    Call<DataResponse<Budget>> deleteBudget(@Path("id") String id);
}
