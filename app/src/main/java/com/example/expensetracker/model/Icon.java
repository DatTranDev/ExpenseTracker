package com.example.expensetracker.model;

public class Icon {
    private int id;
    private String linking;

    // Constructor
    public Icon(int id, String linking) {
        this.id = id;
        this.linking = linking;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLinking() {
        return linking;
    }

    public void setLinking(String linking) {
        this.linking = linking;
    }
}
