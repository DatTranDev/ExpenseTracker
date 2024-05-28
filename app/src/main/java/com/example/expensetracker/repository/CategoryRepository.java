package com.example.expensetracker.repository;

import com.example.expensetracker.api.Category.CategoryApi;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Category.CategoryReq;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.api.RetrofitClient;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.UserCategory;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class CategoryRepository {
    private static CategoryRepository categoryRepository;
    private CategoryApi categoryApi;

    private CategoryRepository() {
        categoryApi = RetrofitClient.getClient().create(CategoryApi.class);
    }

    public static synchronized CategoryRepository getInstance() {
        if (categoryRepository == null) {
            categoryRepository = new CategoryRepository();
        }
        return categoryRepository;
    }

    public synchronized void addCategory(CategoryReq categoryReq, ApiCallBack<Category> callback) {
        categoryApi.addCategory(categoryReq).enqueue(new Callback<DataResponse<Category>>() {
            @Override
            public void onResponse(Call<DataResponse<Category>> call, retrofit2.Response<DataResponse<Category>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Category> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    if(response.code()==400)
                    {
                        try {
                            // Parse the error body if the status code is 400
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to add category");
                        }
                    }
                    else
                    {
                        callback.onError("Failed to add category");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Category>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void deleteCategory(UserCategory category, ApiCallBack<Category> callback) {
        categoryApi.deleteCategory(category).enqueue(new Callback<DataResponse<Category>>() {
            @Override
            public void onResponse(Call<DataResponse<Category>> call, retrofit2.Response<DataResponse<Category>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Category> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to delete category");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Category>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }
}
