package com.example.expensetracker.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.expensetracker.R;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.Helper;

public class TransactionDetailsFragment extends Fragment {

    public static final String TAG = TransactionDetailsFragment.class.getName();
    private TextView transactionName;
    private TextView transactionAmount;
    private TextView transactionTime;
    private View view;
    private ImageButton btnBack;

    public TransactionDetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction_details, container, false);
        transactionName = view.findViewById(R.id.transaction_name);
        transactionAmount = view.findViewById(R.id.transaction_price);
        transactionTime = view.findViewById(R.id.transaction_time);
        btnBack = view.findViewById(R.id.btnBack);

        Bundle receiver = getArguments();
        if (receiver != null) {
            TransactionExp transactionExp = (TransactionExp) receiver.get("transaction");
            if (transactionExp != null) {
                transactionName.setText(transactionExp.getNote());
                transactionAmount.setText((CharSequence) transactionExp.getSpend());
                transactionTime.setText(Helper.formatDate(transactionExp.getCreatedAt()));
            }
        }

//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getFragmentManager() != null) {
//                    getFragmentManager().popBackStack();
//                }
//            }
//        });

        return view;
    }
}