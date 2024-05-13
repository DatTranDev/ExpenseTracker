package com.example.expensetracker.repository;

import com.example.expensetracker.api.RetrofitClient;
import com.example.expensetracker.api.Wallet.WalletApi;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.api.Wallet.WalletReq;
import com.example.expensetracker.api.Wallet.AddMemberReq;
import com.example.expensetracker.api.Wallet.RemoveMemberReq;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class WalletRepository {
    private static WalletRepository walletRepository;
    private WalletApi walletApi;

    private WalletRepository() {
        walletApi = RetrofitClient.getClient().create(WalletApi.class);
    }

    public static synchronized WalletRepository getInstance() {
        if (walletRepository == null) {
            walletRepository = new WalletRepository();
        }
        return walletRepository;
    }

    public synchronized void addWallet(WalletReq walletReq, ApiCallBack<Wallet> callback) {
        walletApi.addWallet(walletReq).enqueue(new Callback<DataResponse<Wallet>>() {
            @Override
            public void onResponse(Call<DataResponse<Wallet>> call, retrofit2.Response<DataResponse<Wallet>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Wallet> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to add wallet");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Wallet>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void updateWallet(String id, Wallet wallet, ApiCallBack<Wallet> callback) {
        walletApi.updateWallet(id, wallet).enqueue(new Callback<DataResponse<Wallet>>() {
            @Override
            public void onResponse(Call<DataResponse<Wallet>> call, retrofit2.Response<DataResponse<Wallet>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Wallet> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to update wallet");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Wallet>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void deleteWallet(String id, ApiCallBack<Wallet> callback) {
        walletApi.deleteWallet(id).enqueue(new Callback<DataResponse<Wallet>>() {
            @Override
            public void onResponse(Call<DataResponse<Wallet>> call, retrofit2.Response<DataResponse<Wallet>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Wallet> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to delete wallet");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Wallet>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void addMember(AddMemberReq addMemberReq, ApiCallBack<Wallet> callback) {
        walletApi.addMember(addMemberReq).enqueue(new Callback<DataResponse<Wallet>>() {
            @Override
            public void onResponse(Call<DataResponse<Wallet>> call, retrofit2.Response<DataResponse<Wallet>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Wallet> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to add member to wallet");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Wallet>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }

    public synchronized void removeMember(RemoveMemberReq removeMemberReq, ApiCallBack<Wallet> callback) {
        walletApi.removeMember(removeMemberReq).enqueue(new Callback<DataResponse<Wallet>>() {
            @Override
            public void onResponse(Call<DataResponse<Wallet>> call, retrofit2.Response<DataResponse<Wallet>> response) {
                if (response.isSuccessful()) {
                    DataResponse<Wallet> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to remove member from wallet");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<Wallet>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }
    public synchronized void getTransaction(String id, ApiCallBack<List<TransactionExp>> callback) {
        walletApi.getTransaction(id).enqueue(new Callback<DataResponse<List<TransactionExp>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<TransactionExp>>> call, retrofit2.Response<DataResponse<List<TransactionExp>>> response) {
                if (response.isSuccessful()) {
                    DataResponse<List<TransactionExp>> responseData = response.body();
                    callback.onSuccess(responseData.getData());
                } else {
                    callback.onError("Failed to get transaction");
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<TransactionExp>>> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }
}
