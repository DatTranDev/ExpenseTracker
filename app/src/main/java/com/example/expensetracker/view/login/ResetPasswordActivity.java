package com.example.expensetracker.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.repository.AppUserRepository;

public class ResetPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resetpassword);
        EditText passwordEditText = findViewById(R.id.newPassword);
        EditText confirmPasswordEditText = findViewById(R.id.confirmNewPassword);
        Button resetButton = findViewById(R.id.confirmChangePassword);
        resetButton.setOnClickListener(new View.OnClickListener(
        ) {
            @Override
            public void onClick(View v) {
                if (passwordEditText.getText().toString().isEmpty() || confirmPasswordEditText.getText().toString().isEmpty()) {
                    Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập mật khẩu mới", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    if (!passwordEditText.getText().toString().equals(confirmPasswordEditText.getText().toString())) {
                        Toast.makeText(ResetPasswordActivity.this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        // Call API to reset password
                        Intent intent = getIntent();
                        AppUser appUser = new AppUser();
                        appUser.setPassword(passwordEditText.getText().toString());
                        appUser.setEmail(intent.getStringExtra("email"));

                        AppUserRepository.getInstance().changePassword(appUser, new ApiCallBack<AppUser>() {
                            @Override
                            public void onSuccess(AppUser data) {
                                Toast.makeText(ResetPasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                startActivity(intent1);
                                finish();
                            }
                            @Override
                            public void onError(String error) {
                                Toast.makeText(ResetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });

    }
}
