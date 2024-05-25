package com.example.expensetracker.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Wallet.WalletReq;
import com.example.expensetracker.model.UserWallet;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WalletViewModel extends ViewModel {
    private MutableLiveData<List<Wallet>> walletsLiveData;
    private List<Wallet> walletList;
    private MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private AppUserRepository appUserRepository;

    public WalletViewModel() {
        walletList = new ArrayList<>();
        walletsLiveData = new MutableLiveData<>();
        walletsLiveData.setValue(walletList);
        appUserRepository = AppUserRepository.getInstance();
    }

    public LiveData<List<Wallet>> getWalletsLiveData() {
        return walletsLiveData;
    }

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    public void loadWallets(String userId) {
        appUserRepository.getWallet(userId, new ApiCallBack<List<Wallet>>() {
            @Override
            public void onSuccess(List<Wallet> wallets) {
                walletList = wallets;
                walletsLiveData.setValue(walletList);
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }

    public void addWallet(WalletReq walletReq, Context context) {
        WalletRepository.getInstance().addWallet(walletReq, new ApiCallBack<Wallet>() {
            @Override
            public void onSuccess(Wallet addedWallet) {
                walletList.add(addedWallet);
                walletsLiveData.setValue(new ArrayList<>(walletList));
                Toast.makeText(context, "Tạo ví thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }

    public void updateWallet(Wallet wallet, Context context) {
        WalletRepository.getInstance().updateWallet(wallet.getId(), wallet, new ApiCallBack<Wallet>() {
            @Override
            public void onSuccess(Wallet updatedWallet) {
                List<Wallet> currentWallets = walletsLiveData.getValue();
                if (currentWallets != null) {
                    walletsLiveData.setValue(currentWallets);
                    Toast.makeText(context, "Sửa ví thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }

    public void deleteWallet(UserWallet userWallet, Context context, int index) {
        WalletRepository.getInstance().deleteWallet(userWallet, new ApiCallBack<Wallet>() {
            @Override
            public void onSuccess(Wallet wallet) {
                List<Wallet> currentWallets = walletsLiveData.getValue();
                if (currentWallets != null) {
                    currentWallets.remove(index);
                    walletsLiveData.setValue(currentWallets);
                    Toast.makeText(context, "Xóa ví thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }

    public BigDecimal getTotalBalance() {
        BigDecimal result = new BigDecimal(0);

        for (Wallet wallet : walletList) {
            result = result.add(wallet.getAmount());
        }

        return result;
    }
}
