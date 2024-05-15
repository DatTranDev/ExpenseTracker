package com.example.expensetracker.view.addTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.expensetracker.R;

public class mainAddActivity extends AppCompatActivity {

    private ImageButton btnSpend, btnRevenue, btnLoan;
    private  RelativeLayout layCategory, layTime, layNote, layWallet, layBorrower;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        ImageButton btnChoose;
        btnSpend = findViewById(R.id.btn_spend);
        btnRevenue = findViewById(R.id.btn_revenue);
        btnLoan = findViewById(R.id.btn_loan);
        layBorrower= findViewById(R.id.borrower);
//        btnChoose=btnSpend;
//        btnChoose.setBackgroundResource(R.drawable.choose_type_button);
        btnSpend.setOnClickListener(v -> {
            btnSpend.setBackgroundResource(R.drawable.choose_type_button);
            btnRevenue.setBackgroundResource(R.drawable.default_type_button);
            btnLoan.setBackgroundResource(R.drawable.default_type_button);
            layBorrower.setVisibility(View.GONE);
        });
        btnLoan.setOnClickListener(v -> {
            btnLoan.setBackgroundResource(R.drawable.choose_type_button);
            btnRevenue.setBackgroundResource(R.drawable.default_type_button);
            btnSpend.setBackgroundResource(R.drawable.default_type_button);
            layBorrower.setVisibility(View.VISIBLE);
        });
        btnRevenue.setOnClickListener(v -> {
            btnRevenue.setBackgroundResource(R.drawable.choose_type_button);
            btnLoan.setBackgroundResource(R.drawable.default_type_button);
            btnSpend.setBackgroundResource(R.drawable.default_type_button);
            layBorrower.setVisibility(View.GONE);
        });
        layTime.setOnClickListener(v -> {

        });




    }
}
