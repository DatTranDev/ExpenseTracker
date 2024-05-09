package com.example.expensetracker.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionExp {
    private String id;
    private String userId;
    private String categoryId;
    private String note;
    private BigDecimal spend;
    private String currency;
    private String partner;
    private String walletId;
    private Timestamp createdAt;
    private String image;

    // Constructor
    public TransactionExp(String id, String userId, String categoryId, String note, BigDecimal spend, String currency, String partner, String walletId, Timestamp createdAt, String image ) {
        this.id = id;
        this.userId = userId;
        this.categoryId = categoryId;
        this.note = note;
        this.spend = spend;
        this.currency = currency;
        this.partner = partner;
        this.walletId = walletId;
        this.createdAt = createdAt;
        this.image = image;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getImage() {
        return partner;
    }

    public void setImage(String partner) {
        this.partner = partner;
    }
}
