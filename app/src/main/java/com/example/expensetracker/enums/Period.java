package com.example.expensetracker.enums;

public enum Period {
    WEEK("Tuần"),
    MONTH("Tháng"),
    YEAR("Năm");

    private final String displayName;

    Period(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
