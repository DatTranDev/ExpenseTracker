package com.example.expensetracker.model;

public class Currency {
    private int id;
    private String currency;

    // Constructor
    public Currency(int id, String currency) {
        this.id = id;
        this.currency = currency;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

