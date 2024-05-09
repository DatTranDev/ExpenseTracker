package com.example.expensetracker.api.Wallet;

public class RemoveMemberReq {
    private String walletId;
    private String userId;
    private String removeUserId;

    public RemoveMemberReq(String walletId, String userId, String removeUserId) {
        this.walletId = walletId;
        this.userId = userId;
        this.removeUserId = removeUserId;
    }

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

    public String getRemoveUserId() {
        return removeUserId;
    }

    public void setRemoveUserId(String removeUserId) {
        this.removeUserId = removeUserId;
    }
}
