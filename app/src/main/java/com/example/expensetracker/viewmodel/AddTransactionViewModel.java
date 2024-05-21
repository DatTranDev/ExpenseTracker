package com.example.expensetracker.viewmodel;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.example.expensetracker.BR;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Wallet;

import java.sql.Timestamp;
import java.util.Calendar;

public class AddTransactionViewModel extends BaseObservable {
    public ObservableField<Category> category = new ObservableField<>();
    public ObservableField<Wallet> wallet = new ObservableField<>();
    public ObservableField<String> node= new ObservableField<>();
    public ObservableField<String> borrower= new ObservableField<>();
    private Timestamp timeTransaction;
    @Bindable
    public Timestamp getTimeTransaction(){return  timeTransaction;}
    public void setTimeTransaction(Timestamp a)
    {
        this.timeTransaction=a;
        notifyPropertyChanged(BR.timeTransaction);
    }


    public void addTransaction()
    {
        if(timeTransaction==null)
        {
            final Calendar calendar = Calendar.getInstance();
            timeTransaction= new Timestamp(calendar.getTimeInMillis());
        }

    }

}