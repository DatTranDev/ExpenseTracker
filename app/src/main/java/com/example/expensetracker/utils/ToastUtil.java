package com.example.expensetracker.utils;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

public class ToastUtil {
    public static void showCustomToast(Context context, String message, int durationInMilliseconds) {
        final Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();

        // Hủy Toast sau durationInMilliseconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, durationInMilliseconds);
    }
}
