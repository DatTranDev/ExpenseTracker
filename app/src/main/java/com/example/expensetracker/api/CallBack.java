package com.example.expensetracker.api;

import com.example.expensetracker.model.AppUser;

public interface CallBack <T>{
    void onSuccess(T t);
    void onError(String message);
}
