package com.example.expensetracker.view.register;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.expensetracker.R;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Button btn = (Button)findViewById(R.id.btnRegister);
        btn.setOnClickListener(v -> {
            startActivity(RegisterActivity.newIntent(this));
        });
    }
}