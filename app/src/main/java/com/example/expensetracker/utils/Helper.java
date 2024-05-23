package com.example.expensetracker.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Helper {
    public static String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        return dateFormat.format(date);
    }
    public static boolean isValidEmail(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static String formatCurrency(BigDecimal amount) {
        DecimalFormat formatter = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));

        String formattedAmount = formatter.format(amount);

        String currencyCode = "VND";
        return formattedAmount + " " + currencyCode;
    }

    public static boolean isSameDay(Date transactionDate, Date date) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(transactionDate);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }

    public static Date normalizeDate(Date date, boolean startOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if (startOfDay) {
            // Set time to the beginning of the day (00:00:00)
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else {
            // Set time to the end of the day (23:59:59)
            calendar.set(Calendar.HOUR_OF_DAY, 23);
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            calendar.set(Calendar.MILLISECOND, 999);
        }

        return calendar.getTime();
    }
    public static String formatMoney(BigDecimal amount) {
        DecimalFormat formatter = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));

        String formattedAmount = formatter.format(amount);

        return formattedAmount ;
    }

}
