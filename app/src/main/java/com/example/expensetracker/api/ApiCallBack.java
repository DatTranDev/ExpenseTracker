package com.example.expensetracker.api;

public interface ApiCallBack<T>{
    void onSuccess(T t);
    void onError(String message);
}
