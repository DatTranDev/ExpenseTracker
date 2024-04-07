package com.example.expensetracker.view.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.CompoundButtonCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import com.example.expensetracker.R;
import com.example.expensetracker.view.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, RegisterActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText editTextEmail = findViewById(R.id.editTextTextEmailAddress);
        editTextEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus&& editTextEmail.getText().toString().equals("Email") ){
                    editTextEmail.setText("");
                    editTextEmail.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        EditText editTextName = findViewById(R.id.editTextTextPersonName);
        editTextName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editTextName.getText().toString().equals("Họ và tên")) {
                    editTextName.setText("");
                    editTextName.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
        EditText editTextPassword = findViewById(R.id.editTextTextPassword);
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && editTextPassword.getText().toString().equals("Mật khẩu")){
                    editTextPassword.setText("");
                    editTextPassword.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });

        TextView textView = findViewById(R.id.textViewLogin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

}