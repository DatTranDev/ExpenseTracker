package com.example.expensetracker.bottom_sheet;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.example.expensetracker.R;
import com.example.expensetracker.utils.MyApplication;
import com.example.expensetracker.utils.NotificationReceiver;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class NotifictionAccount extends BottomSheetDialogFragment {

    private static final int NOTIFICATION_ID = 1;
    private static final int REQUEST_POST_NOTIFICATIONS = 1;
    private ImageView btnCancel;
    public SwitchMaterial btn;

    private static PendingIntent pendingIntent;
    private static AlarmManager alarmManager;
    private static final String PREFS_NAME = "my_prefs";

    private static final String TOGGLE_STATE_KEY = "toggle_state";


    public NotifictionAccount() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.account_notification, null);
        bottomSheetDialog.setContentView(viewDialog);

        initView(viewDialog);

        btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                int maxHeight = getResources().getDisplayMetrics().heightPixels;

                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.height = maxHeight;
                    ;
                    bottomSheet.setLayoutParams(layoutParams);
                }
            }
        });


        boolean isChecked = SharedPreferencesManager.getInstance(getContext()).getObject(TOGGLE_STATE_KEY, Boolean.class);
        btn.setChecked(isChecked);

//        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean(TOGGLE_STATE_KEY, isChecked);
//                editor.apply();
//                if (isChecked) {
//                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
//                            != PackageManager.PERMISSION_GRANTED) {
//                        ActivityCompat.requestPermissions(requireActivity(),
//                                new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_POST_NOTIFICATIONS);
//                    } else {
//                        // Lập lịch thông báo hàng ngày nếu quyền đã được cấp
//                        scheduleDailyNotification(requireContext());
//                    }
//                }
//            }
//        });
       // scheduleDailyNotification(requireContext());
        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferencesManager.getInstance(getContext()).saveObject(TOGGLE_STATE_KEY,isChecked);
                if (btn.isChecked())
                {
                    scheduleDailyNotification(requireContext());
                }
            }
        });

//        if (btn.isChecked())
//        {
//            scheduleDailyNotification(requireContext());
//        }
        return bottomSheetDialog;
    }


    private void initView(View view) {
        btn = view.findViewById(R.id.material_switch);
        btnCancel = view.findViewById(R.id.notification_back);

    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == REQUEST_POST_NOTIFICATIONS) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(getContext(), "Permission allowed", Toast.LENGTH_SHORT).show();
//                scheduleDailyNotification(getContext());
//            } else {
//                // Xử lý khi quyền bị từ chối
//                Toast.makeText(getContext(), "Permission denied to post notifications", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    public static void scheduleDailyNotification(Context context) {
        Intent intent = new Intent(context, NotificationReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 5);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
//                     AlarmManager.INTERVAL_DAY,pendingIntent);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                   pendingIntent);

        }
        Toast.makeText(context, "allow", Toast.LENGTH_SHORT).show();
//        Notification notification = new NotificationCompat.Builder(context, MyApplication.CHANNEL_ID)
//                .setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle("Expense Tracker")
//                .setContentText("Bạn hãy thêm giao dịch mới")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT).build();
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(NOTIFICATION_ID, notification);
    }

}
