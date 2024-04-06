package com.example.expensetracker.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.expensetracker.R;
import com.example.expensetracker.view.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}