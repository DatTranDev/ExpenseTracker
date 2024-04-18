package com.example.expensetracker.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Helper {
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a, d/M/yyyy");
        return dateFormat.format(date);
    }
}
