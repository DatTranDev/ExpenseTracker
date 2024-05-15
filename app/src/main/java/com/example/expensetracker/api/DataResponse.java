package com.example.expensetracker.api;

public class DataResponse<T> {
     private T data;
    private String message;

    public T getData() {
        return data;
    }
    public String getMessage() {
        return message;
    }
}

