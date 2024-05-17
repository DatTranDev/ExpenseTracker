package com.example.expensetracker.viewmodel;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.example.expensetracker.BR;
import com.example.expensetracker.model.Category;

import java.sql.Timestamp;

public class AddTransactionViewModel extends BaseObservable {
    public ObservableField<Category> category = new ObservableField<>(new Category("5", "Mua nhà", true, "aa","aa", "0", "Chi tiêu"));
    private Timestamp timeTransaction;
    @Bindable
    public Timestamp getTimeTransaction(){return  timeTransaction;}
    public void setTimeTransaction(Timestamp a)
    {
        this.timeTransaction=a;
        notifyPropertyChanged(BR.timeTransaction);
    }

}