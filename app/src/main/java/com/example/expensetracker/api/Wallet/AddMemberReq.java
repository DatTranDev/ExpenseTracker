package com.example.expensetracker.api.Wallet;

public class AddMemberReq {
    private String walletId;
    private String userId;
    private String inviteUserMail;

    public AddMemberReq(String walletId, String userId, String inviteUserMail) {
        this.walletId = walletId;
        this.userId = userId;
        this.inviteUserMail = inviteUserMail;
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

    public String getInviteUserMail() {
        return inviteUserMail;
    }

    public void setInviteUserMail(String inviteUserMail) {
        this.inviteUserMail = inviteUserMail;
    }
}
