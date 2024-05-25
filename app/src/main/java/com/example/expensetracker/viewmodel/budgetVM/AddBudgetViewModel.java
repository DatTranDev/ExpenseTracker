package com.example.expensetracker.viewmodel.budgetVM;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.BudgetRepository;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
    public void showMessage(String msg) {
        _message.setValue(msg);
    }
    public  AddBudgetViewModel(Context context)
    {
        SharedPreferences sharedPreferences= context.getSharedPreferences("user",Context.MODE_PRIVATE);


        String userString= sharedPreferences.getString("user","null");

        if(userString!="null")
        {
            Gson gson = new Gson();
            user= gson.fromJson(userString, AppUser.class);
        }
//        category.observeForever(value->updateButton());
//        moneyBudget.observeForever(value->updateButton());

    }
    public void addBudget(){
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
//            int compare= spend.compareTo(wallet.get().getAmount());
//            if(compare>0)
//            {
//                showMessage("Số tiền đã vượt quá số tiền trong ví");
//                return;
//            }
            String periodStirng="Tuần";
            if(period.get().equals("Theo tháng"))
            {
                periodStirng="Tháng";
            }
            try
            {
                Budget newBudget= new Budget(user.getId(),category.get().getId(),spend,periodStirng);
                BudgetRepository.getInstance().addBudget(newBudget, new ApiCallBack<Budget>() {
                    @Override
                    public void onSuccess(Budget budget) {
                        showMessage("Thêm ngân sách thành công");
                        resetData();

                    }

                    @Override
                    public void onError(String message) {
                        showMessage("Thêm ngân sách thất bại");
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