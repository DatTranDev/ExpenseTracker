package com.example.expensetracker.view.budget;

import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;

import java.math.BigDecimal;
import java.util.Date;

public class BudgetItem {
    public String nameCategory;
    public int idIcon;
    public int Progress;
    public String Amount;
    public BigDecimal Enabled;
    public Budget budget;
    public Date startDate;
    public Date endDate;
    public BudgetItem(String nameCategory, int nameIcon, int progress, String amount, BigDecimal enabled, Budget a,Date start, Date end) {
        this.nameCategory = nameCategory;
        this.idIcon = nameIcon;
        Progress = progress;
        Amount = amount;
        Enabled = enabled;
        this.budget=a;
        startDate=start;
        endDate=end;
    }

}
