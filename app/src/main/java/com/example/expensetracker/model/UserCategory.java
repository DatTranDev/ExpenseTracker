package com.example.expensetracker.model;

public class UserCategory {
    private String userId;
    private String categoryId;
    private boolean isDeleted;

    // Constructor
    public UserCategory() {
    }
    public UserCategory(String userId, String categoryId, boolean isDeleted) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.isDeleted = isDeleted;
    }

    // Getters and setters
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

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

