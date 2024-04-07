package com.example.expensetracker.view.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expensetracker.R;
import com.example.expensetracker.view.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editTextEmail = findViewById(R.id.editTextTextEmailAddressLogin);
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editTextEmail.getText().toString().equals("Email")) {
                    editTextEmail.setText("");
                    editTextEmail.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        EditText editTextPassword = findViewById(R.id.editTextTextPasswordLogin);
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editTextPassword.getText().toString().equals("Mật khẩu")) {
                    editTextPassword.setText("");
                    editTextPassword.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        TextView textView = findViewById(R.id.textViewRegister);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}