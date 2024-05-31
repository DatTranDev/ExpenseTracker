package com.example.expensetracker.viewmodel.budgetVM;

import android.content.Context;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.fragment.BudgetFragment;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.BudgetRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.view.budget.AddBudgetActivity;
import com.example.expensetracker.view.budget.EditBudgetActivity;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddBudgetViewModel extends BaseObservable {
    Context context;
    public ObservableField<Category> category= new ObservableField<>();
    public  ObservableField<Wallet> wallet = new ObservableField<>();
    public ObservableField<String> period=  new ObservableField<>("Theo tuần");
    public ObservableField<String> moneyBudget=new ObservableField<>();
    public MutableLiveData<Boolean> btnEnabled = new MutableLiveData<>(false);
    public AppUser user;
    private final MutableLiveData<String> _message = new MutableLiveData<>();
    public LiveData<String> message = _message;
    public ObservableField<List<Budget>> listBudget= new ObservableField<>(new ArrayList<>());
    public void showMessage(String msg) {
        _message.setValue(msg);
    }
    public  AddBudgetViewModel(Context context)
    {
        this.context=context;
        user=SharedPreferencesManager.getInstance(context).getObject("user", AppUser.class);
        Type type = new TypeToken<List<Budget>>() {}.getType();
        listBudget.set(SharedPreferencesManager.getInstance(context).getList("budgets",type));
    }
    public synchronized void addBudget(){
        if(category.get()==null ||  moneyBudget.get()==null || moneyBudget.get().equals("") || user==null)
        {
            if(moneyBudget.get()==null){
                showMessage("Vui lòng nhập số tiền của giao dịch");
            }
            if(category.get()==null)
            {
                showMessage("Vui lòng chọn danh mục");
            }
        }
        else {
            BigDecimal spend ;
            String cleanString = moneyBudget.get().toString().replaceAll("[,]", "");
            spend=new BigDecimal(cleanString);
            String periodStirng="Tuần";
            if(period.get().equals("Theo tháng"))
            {
                periodStirng="Tháng";
            }
            if(period.get().equals("Theo năm"))
            {
                periodStirng="Năm";
            }
            try
            {
                Budget newBudget= new Budget(user.getId(),category.get().getId(),spend,periodStirng);
                newBudget.setCategory(category.get());
                BudgetRepository.getInstance().addBudget(newBudget, new ApiCallBack<Budget>() {
                    @Override
                    public void onSuccess(Budget budget) {
                        newBudget.setId(budget.getId());
                        listBudget.get().add(newBudget);
                        List<Budget> list= listBudget.get();
                        SharedPreferencesManager.getInstance(context).saveList("budgets",list);
                        showMessage("Thêm ngân sách thành công");
                        resetData();

                    }

                    @Override
                    public void onError(String message) {
                        showMessage(message);
                    }
                });
            }
            catch (Exception ex){
                return;
            }

        }
    }
    public void resetData(){
        period.set(null);
        category.set(null);
        moneyBudget.set(null);
    }
    public void updateButton(){
        if(category.get()==null ||  moneyBudget.get()==null || moneyBudget.get().equals(""))
        {
            btnEnabled.setValue(false);
        }
        else {
            btnEnabled.setValue(true);
        }
    }

}
