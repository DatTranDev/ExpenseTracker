package com.example.expensetracker.model;

import java.math.BigDecimal;

public class Budget {
    private String userId;
    private AppUser user;
    private Category category;
    private String categoryId;
    private BigDecimal amount;
    private String period;

    // Constructor
    public Budget(String userId, String categoryId, BigDecimal amount, String period) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.amount = amount;
        this.period = period;
    }

    // Getters and setters
    public AppUser getUser() {
        return user;
    }
    public void setUser(AppUser user) {
        this.user = user;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}

