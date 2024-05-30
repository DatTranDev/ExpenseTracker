package com.example.expensetracker.model;

import java.sql.Time;


public class AppUser {
    private String id;
    private String userName;
    private String password;
    private String email;
    private String alertTime;

    // Constructor
    public AppUser(String id, String userName, String password, String email, String alertTime) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.alertTime = alertTime;
    }
    public AppUser(){}

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getAlertTime() {
        return alertTime;
    }

    public void setAlertTime(String alertTime) {
        this.alertTime = alertTime;
    }

    public void findByEmail(AppUser user){
        if(user.getEmail().equals(this.getEmail())){
            user.setId(this.getId());
            user.setUserName(this.getUserName());
            user.setPassword(this.getPassword());
            user.setAlertTime(this.getAlertTime());
        }
    }
}

