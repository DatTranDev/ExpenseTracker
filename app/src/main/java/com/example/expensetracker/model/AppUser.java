package com.example.expensetracker.model;

import java.sql.Time;

public class AppUser {
    private int id;
    private String userName;
    private String password;
    private String email;
    private Time alertTime;

    // Constructor
    public AppUser(int id, String userName, String password, String email, Time alertTime) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.alertTime = alertTime;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Time getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(Time alertTime) {
        this.alertTime = alertTime;
    }
}

