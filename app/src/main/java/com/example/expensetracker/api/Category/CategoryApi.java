package com.example.expensetracker.api.Category;

import com.example.expensetracker.model.Category;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CategoryApi {
    @POST("/category/add")
    Call<Category> addCategory(@Body Category category);
}
