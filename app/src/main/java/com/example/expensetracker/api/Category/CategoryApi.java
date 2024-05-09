package com.example.expensetracker.api.Category;

import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.UserCategory;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

public interface CategoryApi {
    @POST("/category/add")
    Call<DataResponse<Category>> addCategory(@Body CategoryReq categoryReq);
    @DELETE("/category/delete")
    Call<DataResponse<Category>> deleteCategory(@Body UserCategory category);
}
