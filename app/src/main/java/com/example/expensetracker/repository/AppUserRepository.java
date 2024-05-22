package com.example.expensetracker.repository;

import com.example.expensetracker.api.AppUser.AppUserApi;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.api.RetrofitClient;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class AppUserRepository {
    private static AppUserRepository appUserRepository;
    private AppUserApi appUserApi;
    public AppUserRepository() {
        appUserApi = RetrofitClient.getClient().create(AppUserApi.class);
    }
    public static synchronized AppUserRepository getInstance() {
        if (appUserRepository == null) {
            appUserRepository = new AppUserRepository();
        }
        return appUserRepository;
    }
    public synchronized void login(String email, String password, ApiCallBack<AppUser> userApiCallBack) {
        AppUser loginRequest = new AppUser();
        loginRequest.setEmail(email);
        loginRequest.setPassword(password);
        appUserApi.login(loginRequest).enqueue(new Callback<DataResponse<AppUser>>() {
            @Override
            public void onResponse(Call<DataResponse<AppUser>> call, retrofit2.Response<DataResponse<AppUser>> response) {
                if (response.isSuccessful()) {
                    DataResponse<AppUser> loginResponse = response.body();
                    AppUser appUser = loginResponse.getData();
                    userApiCallBack.onSuccess(appUser);
                } else {
                    userApiCallBack.onError("Invalid email or password");
                }
            }
            @Override
            public void onFailure(Call<DataResponse<AppUser>> call, Throwable t) {
                userApiCallBack.onError("Server error, please try again later: "+t.getMessage());
            }
        });
    }
    public synchronized void register(AppUser appUser, ApiCallBack<AppUser> userApiCallBack) {
        appUserApi.register(appUser).enqueue(new Callback<DataResponse<AppUser>>() {
            @Override
            public void onResponse(Call<DataResponse<AppUser>> call, retrofit2.Response<DataResponse<AppUser>> response) {
                DataResponse<AppUser> loginResponse = response.body();
                if (response.isSuccessful()) {
                    AppUser appUser = loginResponse.getData();
                    userApiCallBack.onSuccess(appUser);
                } else {

                    userApiCallBack.onError("Email already exists or invalid email format");
                }
            }
            @Override
            public void onFailure(Call<DataResponse<AppUser>> call, Throwable t) {
                userApiCallBack.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }
    public synchronized void changePassword(AppUser appUser, ApiCallBack<AppUser> userApiCallBack) {
        appUserApi.changePassword(appUser).enqueue(new Callback<DataResponse<AppUser>>() {
            @Override
            public void onResponse(Call<DataResponse<AppUser>> call, retrofit2.Response<DataResponse<AppUser>> response) {
                if (response.isSuccessful()) {
                    DataResponse<AppUser> responseData = response.body();
                    userApiCallBack.onSuccess(responseData.getData());
                } else {
                    userApiCallBack.onError("Failed to change password");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<AppUser>> call, Throwable t) {
                userApiCallBack.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void update(String id, AppUser appUser, ApiCallBack<AppUser> userApiCallBack) {
        appUserApi.update(id, appUser).enqueue(new Callback<DataResponse<AppUser>>() {
            @Override
            public void onResponse(Call<DataResponse<AppUser>> call, retrofit2.Response<DataResponse<AppUser>> response) {
                if (response.isSuccessful()) {
                    DataResponse<AppUser> responseData = response.body();
                    userApiCallBack.onSuccess(responseData.getData());
                } else {
                    userApiCallBack.onError("Failed to update user");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<AppUser>> call, Throwable t) {
                userApiCallBack.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void findByEmail(String email, ApiCallBack<AppUser> userApiCallBack) {
        appUserApi.findByEmail(email).enqueue(new Callback<DataResponse<AppUser>>() {
            @Override
            public void onResponse(Call<DataResponse<AppUser>> call, retrofit2.Response<DataResponse<AppUser>> response) {
                if (response.isSuccessful()) {
                    DataResponse<AppUser> responseData = response.body();
                    userApiCallBack.onSuccess(responseData.getData());
                } else {
                    userApiCallBack.onError("Failed to find user by email");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<AppUser>> call, Throwable t) {
                userApiCallBack.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void findById(String id, ApiCallBack<AppUser> userApiCallBack) {
        appUserApi.findById(id).enqueue(new Callback<DataResponse<AppUser>>() {
            @Override
            public void onResponse(Call<DataResponse<AppUser>> call, retrofit2.Response<DataResponse<AppUser>> response) {
                if (response.isSuccessful()) {
                    DataResponse<AppUser> responseData = response.body();
                    userApiCallBack.onSuccess(responseData.getData());
                } else {
                    userApiCallBack.onError("Failed to find user by ID");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<AppUser>> call, Throwable t) {
                userApiCallBack.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    // Find feature methods

    public synchronized void getCategory(String id, ApiCallBack<List<Category>> callback) {
        appUserApi.getCategory(id).enqueue(new Callback<DataResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<Category>>> call, retrofit2.Response<DataResponse<List<Category>>> response) {
                if (response.isSuccessful()) {
                    DataResponse<List<Category>> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to get categories");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<Category>>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void getWallet(String id, ApiCallBack<List<Wallet>> callback) {
        appUserApi.getWallet(id).enqueue(new Callback<DataResponse<List<Wallet>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<Wallet>>> call, retrofit2.Response<DataResponse<List<Wallet>>> response) {
                if (response.isSuccessful()) {
                    DataResponse<List<Wallet>> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to get wallets");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<Wallet>>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void getSharingWallet(String id, ApiCallBack<List<Wallet>> callback) {
        appUserApi.getSharingWallet(id).enqueue(new Callback<DataResponse<List<Wallet>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<Wallet>>> call, retrofit2.Response<DataResponse<List<Wallet>>> response) {
                if (response.isSuccessful()) {
                    DataResponse<List<Wallet>> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to get sharing wallets");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<Wallet>>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void getTransaction(String id, ApiCallBack<List<TransactionExp>> callback) {
        appUserApi.getTransaction(id).enqueue(new Callback<DataResponse<List<TransactionExp>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<TransactionExp>>> call, retrofit2.Response<DataResponse<List<TransactionExp>>> response) {
                if (response.isSuccessful()) {
                    DataResponse<List<TransactionExp>> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to get transactions");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<TransactionExp>>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void getBudget(String id, ApiCallBack<List<Budget>> callback) {
        appUserApi.getBudget(id).enqueue(new Callback<DataResponse<List<Budget>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<Budget>>> call, retrofit2.Response<DataResponse<List<Budget>>> response) {
                if (response.isSuccessful()) {
                    DataResponse<List<Budget>> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to get budgets");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<Budget>>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }
}
