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
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainBudgetViewModel extends BaseObservable {
   public ObservableField<List<TransactionExp>> listTransaction= new ObservableField<>(new ArrayList<>());
   public ObservableField<List<Budget>> listBudget= new ObservableField<>(new ArrayList<>());
    private final MutableLiveData<String> _message = new MutableLiveData<>();
    public LiveData<String> message = _message;

    private AppUser user;
    public void showMessage(String msg) {
        _message.setValue(msg);
    }

    public MainBudgetViewModel(Context context) {
        getData(context);


    }
    public synchronized void  getData(Context context){
//        SharedPreferences sharedPreferences = context.getSharedPreferences("budgets", Context.MODE_PRIVATE);
        try
        {
            String budgetsJson = SharedPreferencesManager.getInstance(context).getString("budgets","null");
            if(!budgetsJson.equals("null")) {

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<List<Budget>>() {
                }.getType();
                List<Budget> list = gson.fromJson(budgetsJson, type);
                if (list != null) {
                    listBudget.set(list);
                }
            }
            String transactionsJson = SharedPreferencesManager.getInstance(context).getString("transactions","null");
            if(!transactionsJson.equals("null")) {

                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<List<TransactionExp>>() {
                }.getType();
                List<TransactionExp> list = gson.fromJson(transactionsJson, type);
                if (list != null) {
                    listTransaction.set(list);
                }
            }
        }
        catch (Exception ex)
        {
            showMessage("Không tải được dữ liệu");
        }

    }
}
