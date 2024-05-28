package com.example.expensetracker.viewmodel.budgetVM;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import com.example.expensetracker.model.Budget;
import com.example.expensetracker.view.budget.BudgetItem;

public class DetailBudgetViewModel extends BaseObservable {
    public ObservableField<Budget> budget= new ObservableField<>();
    public DetailBudgetViewModel(Context context)
    {

    }
}
