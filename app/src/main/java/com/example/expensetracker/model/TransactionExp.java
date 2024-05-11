package com.example.expensetracker.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionExp implements Serializable {
    private int id;
    private int userId;
    private int categoryId;
    private String note;
    private BigDecimal spend;
    private int currencyId;
    private String partner;
    private int walletId;
    private Timestamp createdAt;

    // Constructor
    public TransactionExp(int id, int userId, int categoryId, String note, BigDecimal spend, int currencyId, String partner, int walletId, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.note = note;
        this.spend = spend;
        this.currencyId = currencyId;
        this.partner = partner;
        this.walletId = walletId;
        this.createdAt = createdAt;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getSpend() {
        return spend;
    }

    public void setSpend(BigDecimal spend) {
        this.spend = spend;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public int getWalletId() {
        return walletId;
    }

    public void setWalletId(int walletId) {
        this.walletId = walletId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
