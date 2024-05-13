package com.example.expensetracker.model;

public class UserWallet {
    private int walletId;
    private int userId;
    private boolean isDeleted;

    // Constructor
    public UserWallet() {
    }
    public UserWallet(int walletId, int userId, boolean isDeleted) {
        this.walletId = walletId;
        this.userId = userId;
        this.isDeleted = isDeleted;
    }

    // Getters and setters
    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

