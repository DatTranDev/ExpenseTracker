package com.example.expensetracker.viewmodel.budgetVM;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.repository.AppUserRepository;
import com.google.gson.Gson;

import java.util.List;

public class MainBudgetViewModel extends BaseObservable {
   public ObservableField<List<TransactionExp>> listTransaction= new ObservableField<>();
   public ObservableField<List<Budget>> listBudget= new ObservableField<>();
    private final MutableLiveData<String> _message = new MutableLiveData<>();
    public LiveData<String> message = _message;

    private AppUser user;
    public void showMessage(String msg) {
        _message.setValue(msg);
    }

    public MainBudgetViewModel(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");
        user = new Gson().fromJson(userJson, AppUser.class);
        if(user!=null) {
            Log.d("testt", "đã tới 1");
            AppUserRepository.getInstance().getTransaction(user.getId(), new ApiCallBack<List<TransactionExp>>() {
                @Override
                public void onSuccess(List<TransactionExp> transactions) {
                    listTransaction.set(transactions);
                    Log.d("testt", "đã tới 2");
                }

                @Override
                public void onError(String errorMessage) {
                    Log.d("testt", "đã tới 11");
                    showMessage("reset");
                }
            });
            AppUserRepository.getInstance().getBudget(user.getId(), new ApiCallBack<List<Budget>>() {
                @Override
                public void onSuccess(List<Budget> budgets) {
                    listBudget.set(budgets);
                    Log.d("testt", "đã tới 3");
                    showMessage("reset");
                }

                @Override
                public void onError(String message) {

                }
            });
        }

    }
    public void getData(){
//        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
//        String userJson = sharedPreferences.getString("user", "");
//        user = new Gson().fromJson(userJson, AppUser.class);
        if(user!=null) {
            AppUserRepository.getInstance().getTransaction(user.getId(), new ApiCallBack<List<TransactionExp>>() {
                @Override
                public void onSuccess(List<TransactionExp> transactions) {
                    listTransaction.set(transactions);
                }

                @Override
                public void onError(String errorMessage) {
                }
            });
            AppUserRepository.getInstance().getBudget(user.getId(), new ApiCallBack<List<Budget>>() {
                @Override
                public void onSuccess(List<Budget> budgets) {
                    listBudget.set(budgets);
                }

                @Override
                public void onError(String message) {

                }
            });
            showMessage("reset");
        }
    }
}
