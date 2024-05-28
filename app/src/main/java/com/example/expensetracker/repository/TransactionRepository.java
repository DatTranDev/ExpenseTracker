package com.example.expensetracker.repository;

import com.example.expensetracker.api.RetrofitClient;
import com.example.expensetracker.api.Transaction.TransactionApi;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.TransactionExp;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class TransactionRepository {
    private static TransactionRepository transactionRepository;
    private TransactionApi transactionApi;

    private TransactionRepository() {
        transactionApi = RetrofitClient.getClient().create(TransactionApi.class);
    }

    public static synchronized TransactionRepository getInstance() {
        if (transactionRepository == null) {
            transactionRepository = new TransactionRepository();
        }
        return transactionRepository;
    }

    public synchronized void addTransaction(TransactionExp transaction, ApiCallBack<TransactionExp> callback) {
        transactionApi.addTransaction(transaction).enqueue(new Callback<DataResponse<TransactionExp>>() {
            @Override
            public void onResponse(Call<DataResponse<TransactionExp>> call, retrofit2.Response<DataResponse<TransactionExp>> response) {
                if (response.isSuccessful()) {
                    DataResponse<TransactionExp> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    if (response.code() == 400) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());

                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to add transaction");
                        }
                    } else {
                        callback.onError("Failed to add transaction");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse<TransactionExp>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void deleteTransaction(String id, ApiCallBack<TransactionExp> callback) {
        transactionApi.deleteTransaction(id).enqueue(new Callback<DataResponse<TransactionExp>>() {
            @Override
            public void onResponse(Call<DataResponse<TransactionExp>> call, retrofit2.Response<DataResponse<TransactionExp>> response) {
                if (response.isSuccessful()) {
                    DataResponse<TransactionExp> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    if (response.code() == 400) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to delete transaction");
                        }
                    } else {
                        callback.onError("Failed to delete transaction");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse<TransactionExp>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void updateTransaction(String id, TransactionExp transaction, ApiCallBack<TransactionExp> callback) {
        transactionApi.updateTransaction(id, transaction).enqueue(new Callback<DataResponse<TransactionExp>>() {
            @Override
            public void onResponse(Call<DataResponse<TransactionExp>> call, retrofit2.Response<DataResponse<TransactionExp>> response) {
                if (response.isSuccessful()) {
                    DataResponse<TransactionExp> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    if (response.code() == 400) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to update transaction");
                        }
                    } else {
                        callback.onError("Failed to update transaction");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse<TransactionExp>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void getNeedToReceive(String id, ApiCallBack<List<TransactionExp>> callback) {
        transactionApi.getNeedToReceive(id).enqueue(new Callback<DataResponse<List<TransactionExp>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<TransactionExp>>> call, retrofit2.Response<DataResponse<List<TransactionExp>>> response) {
                if (response.isSuccessful()) {
                    DataResponse<List<TransactionExp>> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    if (response.code() == 400) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to get need to receive");
                        }
                    } else {
                        callback.onError("Failed to get need to receive");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<TransactionExp>>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void getNeedToPay(String id, ApiCallBack<List<TransactionExp>> callback) {
        transactionApi.getNeedToPay(id).enqueue(new Callback<DataResponse<List<TransactionExp>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<TransactionExp>>> call, retrofit2.Response<DataResponse<List<TransactionExp>>> response) {
                if (response.isSuccessful()) {
                    DataResponse<List<TransactionExp>> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    if (response.code() == 400) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to get need to pay");
                        }
                    } else {
                        callback.onError("Failed to get need to pay");
                    }
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<TransactionExp>>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

}
