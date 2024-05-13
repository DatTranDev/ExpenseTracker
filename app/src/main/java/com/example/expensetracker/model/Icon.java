package com.example.expensetracker.model;

public class Icon {
    private String id;
    private String linking;

    // Constructor
    public Icon(String id, String linking) {
        this.id = id;
        this.linking = linking;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLinking() {
        return linking;
    }

    public void setLinking(String linking) {
        this.linking = linking;
    }
}
