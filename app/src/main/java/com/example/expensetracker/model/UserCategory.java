package com.example.expensetracker.model;

public class UserCategory {
    private int userId;
    private int categoryId;
    private boolean isDeleted;

    // Constructor
    public UserCategory() {
    }
    public UserCategory(int userId, int categoryId, boolean isDeleted) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.isDeleted = isDeleted;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

