package com.example.expensetracker.repository;

import com.example.expensetracker.api.Budget.BudgetApi;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.api.RetrofitClient;
import com.example.expensetracker.model.Budget;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;

public class BudgetRepository {
    private static BudgetRepository budgetRepository;
    private BudgetApi budgetApi;

    private BudgetRepository() {
        budgetApi = RetrofitClient.getClient().create(BudgetApi.class);
    }

    public static synchronized BudgetRepository getInstance() {
        if (budgetRepository == null) {
            budgetRepository = new BudgetRepository();
        }
        return budgetRepository;
    }

    public synchronized void addBudget(Budget budget, ApiCallBack<Budget> callback) {
        budgetApi.addBudget(budget).enqueue(new Callback<DataResponse<Budget>>() {
            @Override
            public void onResponse(Call<DataResponse<Budget>> call, retrofit2.Response<DataResponse<Budget>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Budget> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    if(response.code()==400)
                    {
                        try{
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to add budget");
                        }
                    }
                    else
                    {
                        callback.onError("Failed to add budget");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Budget>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void updateBudget(Budget budget, ApiCallBack<Budget> callback) {
        budgetApi.updateBudget(budget.getId(), budget).enqueue(new Callback<DataResponse<Budget>>() {
            @Override
            public void onResponse(Call<DataResponse<Budget>> call, retrofit2.Response<DataResponse<Budget>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Budget> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    if (response.code() == 400) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to update budget");
                        }
                    } else {
                        callback.onError("Failed to update budget");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Budget>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void deleteBudget(String id, ApiCallBack<Budget> callback) {
        budgetApi.deleteBudget(id).enqueue(new Callback<DataResponse<Budget>>() {
            @Override
            public void onResponse(Call<DataResponse<Budget>> call, retrofit2.Response<DataResponse<Budget>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Budget> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    if(response.code()==400)
                    {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to delete budget");
                        }
                    }
                    else
                    {
                        callback.onError("Failed to delete budget");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Budget>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }
}
