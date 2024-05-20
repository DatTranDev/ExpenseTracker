package com.example.expensetracker.view.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Service.MailSend;
import com.example.expensetracker.generated.callback.OnClickListener;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.repository.ServiceRepository;

public class VerifyCodeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verifycode);
        Button verifyButton = findViewById(R.id.confirmCode);
        EditText codeEditText = findViewById(R.id.code);
        TextView resendCode = findViewById(R.id.resendCode);
        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                AppUser appUser = new AppUser();
                appUser.setEmail(intent.getStringExtra("email"));
                // Call API to resend code
                ServiceRepository.getInstance().sendEmail(appUser, new ApiCallBack<MailSend>() {
                    @Override
                    public void onSuccess(MailSend data) {
                        Toast.makeText(VerifyCodeActivity.this, "Mã xác nhận đã được gửi lại", Toast.LENGTH_SHORT).show();
                        intent.putExtra("userCode", data.getUserCode());
                    }
                    @Override
                    public void onError(String error) {
                        Toast.makeText(VerifyCodeActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        verifyButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(codeEditText.getText().toString().isEmpty()){
                    Toast.makeText(VerifyCodeActivity.this, "Vui lòng nhập mã xác nhận", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                    Intent intent = getIntent();
                    String userCode = intent.getStringExtra("userCode");
                    if(codeEditText.getText().toString().equals(userCode)){
                        Intent intent1 = new Intent(VerifyCodeActivity.this, ResetPasswordActivity.class);
                        intent1.putExtra("email", intent.getStringExtra("email"));
                        startActivity(intent1);
                        finish();
                    }
                    else{
                        Toast.makeText(VerifyCodeActivity.this, "Mã xác nhận không đúng", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        }
        );
    }
}
