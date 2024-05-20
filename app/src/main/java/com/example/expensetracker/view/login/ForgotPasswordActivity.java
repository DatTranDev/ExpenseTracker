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
import com.example.expensetracker.api.Service.MailSend;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.repository.ServiceRepository;

public class ForgotPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        Button sendButton = findViewById(R.id.btnSendMail);
        EditText emailEditText = findViewById(R.id.getCodeEmail);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emailEditText.getText().toString().isEmpty()){
                    Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    AppUser appUser = new AppUser();
                    appUser.setEmail(emailEditText.getText().toString());
                    ServiceRepository.getInstance().sendEmail(appUser, new ApiCallBack<MailSend>() {
                        @Override
                        public void onSuccess(MailSend mailSend) {
                            Toast.makeText(ForgotPasswordActivity.this, "Mã xác nhận đã được gửi đến email của bạn", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ForgotPasswordActivity.this, VerifyCodeActivity.class);
                            intent.putExtra("userCode", mailSend.getUserCode());
                            intent.putExtra("email", emailEditText.getText().toString());
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(ForgotPasswordActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }
}
