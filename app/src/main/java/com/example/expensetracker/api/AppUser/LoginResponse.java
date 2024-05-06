package com.example.expensetracker.api.AppUser;

import com.example.expensetracker.model.AppUser;

public class LoginResponse {
    private AppUser data;
    public LoginResponse(AppUser data) {
        this.data = data;
    }
    public AppUser getData() {
        return data;
    }
    public void setData(AppUser data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "data=" + data +
                '}';
    }
}
