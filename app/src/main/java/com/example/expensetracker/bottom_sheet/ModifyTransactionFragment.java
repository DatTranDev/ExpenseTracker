package com.example.expensetracker.bottom_sheet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.expensetracker.R;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModifyTransactionFragment extends BottomSheetDialogFragment {
    private static final String KEY_TRANSACTION = "transaction_info";
    private TransactionExp transactionExp;
    private TextView transactionType, transactionCategory, transactionTime, transactionNote, transactionAmount;
    private ImageView transactionCurrency;
    private TextView btnCancel;

    public static ModifyTransactionFragment newInstance(TransactionExp transactionExp) {
        ModifyTransactionFragment modifyTransactionFragment = new ModifyTransactionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_TRANSACTION, transactionExp);
        modifyTransactionFragment.setArguments(bundle);
        return modifyTransactionFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceiver = getArguments();
        if (bundleReceiver != null) {
            transactionExp = (TransactionExp) bundleReceiver.getParcelable(KEY_TRANSACTION);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_modify_transaction, null);
        bottomSheetDialog.setContentView(viewDialog);
        initView(viewDialog);
        setTransactionData();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    int maxHeight = getResources().getDisplayMetrics().heightPixels;
                    maxHeight = maxHeight - maxHeight / 4;

                    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.height = maxHeight;
                        bottomSheet.setLayoutParams(layoutParams);
                    }
                }
            }
        });

        return bottomSheetDialog;
    }

    private void initView(View view) {
        transactionType = view.findViewById(R.id.transaction_type);
        transactionCurrency = view.findViewById(R.id.transaction_currency);
        transactionCategory = view.findViewById(R.id.transaction_category);
        transactionNote = view.findViewById(R.id.transaction_note);
        transactionTime = view.findViewById(R.id.transaction_time);
        transactionAmount = view.findViewById(R.id.transaction_price);
        btnCancel = view.findViewById(R.id.cancelModify);
    }

    private void setTransactionData() {
        if (transactionExp == null) {
            return;
        }

        if (transactionExp.getCurrency().equals("VND")) {
            transactionCurrency.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_vnd));
        } else {
            transactionCurrency.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dollar));
        }
        transactionNote.setText(transactionExp.getNote());
        transactionAmount.setText(String.valueOf(transactionExp.getSpend()));
        transactionTime.setText(Helper.formatDate(transactionExp.getCreatedAt()));
        transactionCategory.setText(String.valueOf(transactionExp.getCategory().getType()));
        transactionType.setText(String.valueOf(transactionExp.getCategory().getName()));
    }
}
