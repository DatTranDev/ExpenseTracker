package com.example.expensetracker.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
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
import com.example.expensetracker.view.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class TransactionDetailsFragment extends Fragment {

    public static final String TAG = TransactionDetailsFragment.class.getName();

    private static final String KEY_TRANSACTION = "transaction_info";
    private String transaction;
    private TextView transactionType;
    private TextView transactionAmount;
    private TextView transactionTime;
    private TextView transactionNote;
    private View view;
    private ImageButton btnBack;

    private TextView btnModify;

    public TransactionDetailsFragment() {
    }

    public static TransactionDetailsFragment newInstance(String param) {
        TransactionDetailsFragment details = new TransactionDetailsFragment();
        Bundle args = new Bundle();
        args.putString(KEY_TRANSACTION, param);
        details.setArguments(args);
        return details;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transaction = getArguments().getString(KEY_TRANSACTION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_transaction_details, container, false);
        transactionType = view.findViewById(R.id.transaction_type);
        transactionAmount = view.findViewById(R.id.transaction_price);
        transactionTime = view.findViewById(R.id.transaction_time);
        transactionNote = view.findViewById(R.id.transaction_note);
        btnBack = view.findViewById(R.id.btnBack);
        btnModify = view.findViewById(R.id.modifyTransaction);

        Bundle receiver = getArguments();

        if (receiver != null) {
            TransactionExp transactionExp = (TransactionExp) receiver.get(KEY_TRANSACTION);
            if (transactionExp != null) {
                transactionType.setText(transactionExp.getCategory().getName());
                transactionNote.setText(transactionExp.getNote());
                transactionAmount.setText(String.valueOf(transactionExp.getSpend()));
                transactionTime.setText(Helper.formatDate(transactionExp.getCreatedAt()));
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransactionExp transactionExp = (TransactionExp) receiver.get(KEY_TRANSACTION);
                modifyTransaction(transactionExp);
            }
        });

        return view;
    }

    private void modifyTransaction(TransactionExp transactionExp) {
        ModifyTransactionFragment modifyTransactionFragment = ModifyTransactionFragment.newInstance(transactionExp);
        modifyTransactionFragment.show(getActivity().getSupportFragmentManager(), modifyTransactionFragment.getTag());
    }
}