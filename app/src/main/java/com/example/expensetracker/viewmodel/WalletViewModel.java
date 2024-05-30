package com.example.expensetracker.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Wallet.AddMemberReq;
import com.example.expensetracker.api.Wallet.RemoveMemberReq;
import com.example.expensetracker.api.Wallet.WalletReq;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.UserWallet;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.repository.WalletRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WalletViewModel extends ViewModel {
    private MutableLiveData<List<Wallet>> walletsLiveData;
    private MutableLiveData<Boolean> isLoading;
    private List<Wallet> walletList;
    private List<AppUser> userList;
    private MutableLiveData<List<AppUser>> usersLiveData;
    private MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private AppUserRepository appUserRepository;

    public WalletViewModel() {
        walletList = new ArrayList<>();
        walletsLiveData = new MutableLiveData<>();
        walletsLiveData.setValue(walletList);
        userList = new ArrayList<>();
        usersLiveData = new MutableLiveData<>();
        usersLiveData.setValue(userList);
        isLoading = new MutableLiveData<>();
        appUserRepository = AppUserRepository.getInstance();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<List<Wallet>> getWalletsLiveData() {
        return walletsLiveData;
    }

    public LiveData<List<AppUser>> getUsersLiveData(){return usersLiveData;}

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    public void loadWallets(String userId, Context context) {
        isLoading.setValue(true);
        appUserRepository.getWallet(userId, new ApiCallBack<List<Wallet>>() {
            @Override
            public void onSuccess(List<Wallet> wallets) {
                walletList = wallets;
                walletsLiveData.setValue(walletList);
                isLoading.setValue(false);
                SharedPreferencesManager.getInstance(context).saveList("wallets", walletList);
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
                isLoading.setValue(false);
            }
        });
    }

    public synchronized void loadFunds(String userId) {
        isLoading.setValue(true);
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(null);
        Type type = new TypeToken<List<Wallet>>(){}.getType();
        List<Wallet> sharingWallets = sharedPreferencesManager.getList("sharingWallets", type);
        walletList = sharingWallets;
        walletsLiveData.setValue(walletList);
        isLoading.setValue(false);

//        AppUserRepository.getInstance().getSharingWallet(userId, new ApiCallBack<List<Wallet>>() {
//            @Override
//            public synchronized void onSuccess(List<Wallet> wallets) {
//                walletList = wallets;
//                walletsLiveData.setValue(walletList);
//                isLoading.setValue(false);
//            }
//
//            @Override
//            public void onError(String message) {
//                errorMessageLiveData.setValue(message);
//                isLoading.setValue(false);
//            }
//        });
    }

    public void loadMembers(String userId, Wallet fund){
        isLoading.setValue(true);
        userList = fund.getMembers();
        usersLiveData.setValue(userList);
        isLoading.setValue(false);
    }

    public void addWallet(WalletReq walletReq, Context context) {
        WalletRepository.getInstance().addWallet(walletReq, new ApiCallBack<Wallet>() {
            @Override
            public void onSuccess(Wallet addedWallet) {
                walletList.add(addedWallet);
                walletsLiveData.setValue(new ArrayList<>(walletList));
                if(addedWallet.isSharing()){
                    Toast.makeText(context, "Tạo quỹ thành công", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(context, "Tạo ví thành công", Toast.LENGTH_SHORT).show();
                SharedPreferencesManager.getInstance(context).saveList("wallets", walletList);
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
                    Toast.makeText(context, "Sửa thành công", Toast.LENGTH_SHORT).show();
                    SharedPreferencesManager.getInstance(context).saveList("wallets", walletList);
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
                currentWallets.remove(index);
                walletsLiveData.setValue(currentWallets);
                Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                SharedPreferencesManager.getInstance(context).saveList("wallets", walletList);
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }

    public void addMember(AddMemberReq addMemberReq, Wallet fund, Context context) {
        // Check if the member already exists in the fund
        boolean memberExists = false;
        for (AppUser member : fund.getMembers()) {
            if (member.getEmail().equals(addMemberReq.getInviteUserMail())) {
                memberExists = true;
                break;
            }
        }

        // If member exists, show a message and return
        if (memberExists) {
            Toast.makeText(context, "Thành viên đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        // If member does not exist, proceed to add the member
        appUserRepository.getInstance().findByEmail(addMemberReq.getInviteUserMail(), new ApiCallBack<AppUser>() {
            @Override
            public void onSuccess(AppUser user) {
                AppUser appUser = new AppUser();
                appUser.setEmail(addMemberReq.getInviteUserMail());
                user.findById(appUser);
                fund.getMembers().add(appUser); // Add the new member to the original fund
                Toast.makeText(context, "Thêm thành viên thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }

    public void removeMember(RemoveMemberReq removeMemberReq, Wallet fund, Context context) {
        // Find the member to remove
        AppUser memberToRemove = null;
        for (AppUser member : fund.getMembers()) {
            if (member.getId().equals(removeMemberReq.getUserId())) {
                memberToRemove = member;
                break;
            }
        }

        // If member not found, show a message and return
        if (memberToRemove == null) {
            Toast.makeText(context, "Thành viên không tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        // If member found, proceed to remove the member
        WalletRepository.getInstance().removeMember(removeMemberReq, new ApiCallBack<Wallet>() {
            @Override
            public void onSuccess(Wallet removeMemberWallet) {
                for (AppUser user : fund.getMembers()) {
                    if (user.getId().equals(removeMemberReq.getUserId())) {
                        fund.getMembers().remove(user);
                        break;
                    }
                }
                // Update walletsLiveData after removing member
                walletsLiveData.setValue(walletList);
                Toast.makeText(context, "Xóa thành viên thành công", Toast.LENGTH_SHORT).show();
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