package com.example.expensetracker.api.Category;

import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.UserCategory;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.HTTP;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoryApi {
    @POST("category/add")
    Call<DataResponse<Category>> addCategory(@Body CategoryReq categoryReq);
    @HTTP(method = "DELETE", path = "category/delete", hasBody = true)
    Call<DataResponse<Category>> deleteCategory(@Body UserCategory category);
    @PATCH("category/update/{id}")
    Call<DataResponse<Category>> updateCategory(@Path("id") String id, @Body CategoryReq categoryReq);
}
