package com.example.expensetracker.view.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.example.expensetracker.R;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.view.login.LoginActivity;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String json = sharedPreferences.getString("user", "");

        // If user data is present in SharedPreferences, navigate to MainActivity
        if (!json.isEmpty()) {
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        Button btn = (Button)findViewById(R.id.btnRegister);
        btn.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}