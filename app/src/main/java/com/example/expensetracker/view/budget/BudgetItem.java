package com.example.expensetracker.view.budget;

import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;

import java.math.BigDecimal;

public class BudgetItem {
    public String nameCategory;
    public int idIcon;
    public int Progress;
    public String Amount;
    public BigDecimal Enabled;
    public Budget budget;
    public BudgetItem(String nameCategory, int nameIcon, int progress, String amount, BigDecimal enabled, Budget a) {
        this.nameCategory = nameCategory;
        this.idIcon = nameIcon;
        Progress = progress;
        Amount = amount;
        Enabled = enabled;
        this.budget=a;
    }

}
