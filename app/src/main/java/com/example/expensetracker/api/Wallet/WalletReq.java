package com.example.expensetracker.api.Wallet;

import java.math.BigDecimal;
import java.util.List;

public class WalletReq {
    private String id;
    private String name;
    private BigDecimal amount;
    private String currency;
    private boolean isSharing;
    private String userId;
    private List<String> inviteUserMail;

    public WalletReq(String id, String name, BigDecimal amount, String currency, boolean isSharing, String userId, List<String> inviteUserMail) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.isSharing = isSharing;
        this.userId = userId;
        this.inviteUserMail = inviteUserMail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public boolean isSharing() {
        return isSharing;
    }

    public void setSharing(boolean sharing) {
        isSharing = sharing;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String> getInviteUserMail() {
        return inviteUserMail;
    }

    public void setInviteUserMail(List<String> inviteUserMail) {
        this.inviteUserMail = inviteUserMail;
    }
}
