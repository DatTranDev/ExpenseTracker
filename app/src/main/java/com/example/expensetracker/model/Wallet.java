package com.example.expensetracker.model;

import java.math.BigDecimal;

public class Wallet {
    private int id;
    private String name;
    private BigDecimal amount;
    private boolean isSharing;

    // Constructor
    public Wallet(int id, String name, BigDecimal amount, boolean isSharing) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.isSharing = isSharing;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isSharing() {
        return isSharing;
    }

    public void setSharing(boolean sharing) {
        isSharing = sharing;
    }
}

