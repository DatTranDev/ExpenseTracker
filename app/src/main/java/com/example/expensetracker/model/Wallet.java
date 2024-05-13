package com.example.expensetracker.model;

import java.math.BigDecimal;

public class Wallet {
    private String id;
    private String name;
    private BigDecimal amount;
    private String currency;
    private boolean isSharing;

    // Constructor
    public Wallet() {
    }
    public Wallet(String id, String name, BigDecimal amount, String currency, boolean isSharing) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.currency = currency;
        this.isSharing = isSharing;
    }

    // Getters and setters
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
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public String getCurrency() {
        return currency;
    }
    public boolean isSharing() {
        return isSharing;
    }

    public void setSharing(boolean sharing) {
        isSharing = sharing;
    }
}

