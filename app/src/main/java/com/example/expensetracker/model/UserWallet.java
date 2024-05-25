package com.example.expensetracker.model;

public class UserWallet {
    private String walletId;
    private String userId;
    private boolean isDeleted;

    // Constructor
    public UserWallet() {
    }
    public UserWallet(String walletId, String userId, boolean isDeleted) {
        this.walletId = walletId;
        this.userId = userId;
        this.isDeleted = isDeleted;
    }

    // Getters and setters
    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

