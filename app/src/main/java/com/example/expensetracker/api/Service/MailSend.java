package com.example.expensetracker.api.Service;

public class MailSend {
    private String message;
    private String userCode;

    public String getMessage() {
        return message;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
