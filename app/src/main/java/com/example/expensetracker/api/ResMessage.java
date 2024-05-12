package com.example.expensetracker.api;

public class ResMessage {
    private String message;

    public ResMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResMessage{" +
                "message='" + message + '\'' +
                '}';
    }
}
