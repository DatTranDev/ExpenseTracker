package com.example.expensetracker.enums;

public enum Type {
    KHOAN_THU("Khoản thu"),
    KHOAN_CHI("Khoản chi"),
    CHO_VAY("Cho vay"),
    DI_VAY("Đi vay"),
    THU_NO("Thu nợ"),
    TRA_NO("Trả nợ");

    private final String displayName;

    Type(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}