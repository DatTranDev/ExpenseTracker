package com.example.expensetracker.view.budget;

import com.example.expensetracker.model.Category;

import java.math.BigDecimal;

public class BudgetItem {
    public String nameCategory;
    public int idIcon;
    public int Progress;
    public String Amount;
    public BigDecimal Enabled;
    public BudgetItem(String nameCategory, int nameIcon, int progress, String amount, BigDecimal enabled) {
        this.nameCategory = nameCategory;
        this.idIcon = nameIcon;
        Progress = progress;
        Amount = amount;
        Enabled = enabled;
    }

}
