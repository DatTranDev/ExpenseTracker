package com.example.expensetracker.view.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

        EditText editText = findViewById(R.id.editTextTextEmailAddress);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editText.setText("");
                    editText.setTextColor(getResources().getColor(R.color.black));
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