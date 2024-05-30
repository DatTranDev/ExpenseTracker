package com.example.expensetracker.utils;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.expensetracker.bottom_sheet.NotifictionAccount;

import com.example.expensetracker.R;

public class NotificationReceiver extends BroadcastReceiver{
    private static final String CHANNEL_ID = "hihi";
    private static final int NOTIFICATION_ID = 1;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            NotifictionAccount.scheduleDailyNotification(context);
        }
            else{
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                CharSequence name = "Expense Tracker Channel";
//                String description = "Kênh cho thông báo của Expense Tracker";
//                int importance = NotificationManager.IMPORTANCE_DEFAULT;
//                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//                channel.setDescription(description);
//
//                notificationManager.createNotificationChannel(channel);
//            }
                Notification notification = new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle("Expense Tracker")
                        .setContentText("Bạn hãy thêm giao dịch mới")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
                notificationManager.notify(NOTIFICATION_ID, notification);
            }
        }
    }
