package com.example.expensetracker.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Request.RequestRes;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Request;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.RequestRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private LinearLayout notificationsContainer;

    @Override
    protected synchronized void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> finish());
        notificationsContainer = findViewById(R.id.notifications_container);

        fetchNotification();
    }

    private synchronized void fetchNotification() {
        AppUser user = SharedPreferencesManager.getInstance(this).getObject("user", AppUser.class);
        RequestRepository.getInstance().getRequestsByUser(user.getId(), new ApiCallBack<List<Request>>() {
            @Override
            public void onSuccess(List<Request> response) {
                if (response.size() > 0) {
                    displayNotifications(response);
                }
            }

            @Override
            public void onError(String message) {
                // Show error message (implement as needed)
            }
        });
    }

    private void displayNotifications(List<Request> notifications) {
        LayoutInflater inflater = LayoutInflater.from(this);

        for (Request notification : notifications) {
            View notificationView = inflater.inflate(R.layout.notification_item, notificationsContainer, false);
            TextView messageTextView = notificationView.findViewById(R.id.notification_message);
            LinearLayout actionButtons = notificationView.findViewById(R.id.action_buttons);
            Button acceptButton = notificationView.findViewById(R.id.accept_button);
            Button declineButton = notificationView.findViewById(R.id.decline_button);
            TextView dateTextView = notificationView.findViewById(R.id.notification_time);

            dateTextView.setText(notification.getCreateAt().toString());
            String message = notification.getName();
            messageTextView.setText(message);

            if (notification.getWalletId() != null) {
                actionButtons.setVisibility(View.VISIBLE);
                acceptButton.setOnClickListener(v -> handleAccept(notification.getId(), notification.getWallet()));
                declineButton.setOnClickListener(v -> handleDecline(notification.getId()));
            }

            notificationsContainer.addView(notificationView);
        }
    }

    private synchronized void handleAccept(String requestId, Wallet wallet) {
        RequestRes requestRes = new RequestRes();
        requestRes.setRequestId(requestId);
        requestRes.setAccepted(true);
       RequestRepository.getInstance().responseRequest(requestRes, new ApiCallBack<Request>() {
           @Override
           public synchronized void onSuccess(Request response) {
               refreshNotification();
               Type listType = new TypeToken<List<Wallet>>() {}.getType();
               List<Wallet> sharingWallets = SharedPreferencesManager.getInstance(NotificationActivity.this).getList("sharingWallets", listType);
               sharingWallets.add(wallet);
               SharedPreferencesManager.getInstance(NotificationActivity.this).saveList("sharingWallets", sharingWallets);

               Toast.makeText(NotificationActivity.this, "Thành công tham gia quỹ "+wallet.getName(), Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onError(String message) {
               Toast.makeText(NotificationActivity.this, message, Toast.LENGTH_SHORT).show();
               // Show error message (implement as needed)
           }
       });
    }

    private synchronized void handleDecline(String requestId) {
        RequestRes requestRes = new RequestRes();
        requestRes.setRequestId(requestId);
        requestRes.setAccepted(false);
        RequestRepository.getInstance().responseRequest(requestRes, new ApiCallBack<Request>() {
            @Override
            public synchronized void onSuccess(Request response) {
                refreshNotification();
                // Show success message (implement as needed)
                Toast.makeText(NotificationActivity.this, "Từ chối thành công", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                // Show error message
                Toast.makeText(NotificationActivity.this, "Từ chối thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void refreshNotification(){
        notificationsContainer.removeAllViews();
        fetchNotification();
    }
}
