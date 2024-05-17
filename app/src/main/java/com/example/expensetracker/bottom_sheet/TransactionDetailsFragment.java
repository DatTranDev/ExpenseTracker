//package com.example.expensetracker.fragment;
//
//import android.app.Dialog;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.expensetracker.R;
//import com.example.expensetracker.bottom_sheet.ModifyTransactionFragment;
//import com.example.expensetracker.model.TransactionExp;
//import com.example.expensetracker.utils.Helper;
//import com.example.expensetracker.view.MainActivity;
//import com.google.android.material.bottomsheet.BottomSheetDialog;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//
//public class TransactionDetailsFragment extends BottomSheetDialogFragment {
//
//    public static final String TAG = TransactionDetailsFragment.class.getName();
//
//    private static final String KEY_TRANSACTION = "transaction_info";
//    private String transaction;
//    private TextView transactionType;
//    private TextView transactionAmount;
//    private TextView transactionTime;
//    private TextView transactionNote;
//    private View view;
//    private ImageButton btnBack;
//
//    private TextView btnModify;
//
//    public TransactionDetailsFragment() {
//    }
//
//    public static TransactionDetailsFragment newInstance(String param) {
//        TransactionDetailsFragment details = new TransactionDetailsFragment();
//        Bundle args = new Bundle();
//        args.putString(KEY_TRANSACTION, param);
//        details.setArguments(args);
//        return details;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            transaction = getArguments().getString(KEY_TRANSACTION);
//        }
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_transaction_details, container, false);
//        transactionType = view.findViewById(R.id.transaction_type);
//        transactionAmount = view.findViewById(R.id.transaction_price);
//        transactionTime = view.findViewById(R.id.transaction_time);
//        transactionNote = view.findViewById(R.id.transaction_note);
//        btnBack = view.findViewById(R.id.btnBack);
//        btnModify = view.findViewById(R.id.modifyTransaction);
//
//        Bundle receiver = getArguments();
//
//        if (receiver != null) {
//            TransactionExp transactionExp = (TransactionExp) receiver.get(KEY_TRANSACTION);
//            if (transactionExp != null) {
//                transactionType.setText(transactionExp.getCategory().getName());
//                transactionNote.setText(transactionExp.getNote());
//                transactionAmount.setText(String.valueOf(transactionExp.getSpend()));
//                transactionTime.setText(Helper.formatDate(transactionExp.getCreatedAt()));
//            }
//        }
//
//        btnBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity mainActivity = (MainActivity) getActivity();
//                if (mainActivity != null) {
//                    mainActivity.navigateToTransactions();
//                }
//            }
//        });
//
//        btnModify.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TransactionExp transactionExp = (TransactionExp) receiver.get(KEY_TRANSACTION);
//                modifyTransaction(transactionExp);
//            }
//        });
//
//        return view;
//    }
//
//    private void modifyTransaction(TransactionExp transactionExp) {
//        ModifyTransactionFragment modifyTransactionFragment = ModifyTransactionFragment.newInstance(transactionExp);
//        modifyTransactionFragment.show(getActivity().getSupportFragmentManager(), modifyTransactionFragment.getTag());
//    }
//}
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

public class TransactionDetailsFragment extends BottomSheetDialogFragment {
    private static final String KEY_TRANSACTION = "transaction_info";
    private TransactionExp transactionExp;
    private TextView transactionType, transactionCategory, transactionTime, transactionNote, transactionAmount;
    private ImageView transactionCurrency;
    private TextView btnCancel;
    private TextView btnModify;

    public static TransactionDetailsFragment newInstance(TransactionExp transactionExp) {
        TransactionDetailsFragment transactionDetailsFragment = new TransactionDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_TRANSACTION, transactionExp);
        transactionDetailsFragment.setArguments(bundle);
        return transactionDetailsFragment;
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
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_transaction_details, null);
        bottomSheetDialog.setContentView(viewDialog);
        initView(viewDialog);
        setTransactionData();

//        btnCancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                bottomSheetDialog.dismiss();
//            }
//        });

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

        btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyTransaction(transactionExp);
            }
        });

        return bottomSheetDialog;
    }

    private void modifyTransaction(TransactionExp transactionExp) {
        ModifyTransactionFragment modifyTransactionFragment = ModifyTransactionFragment.newInstance(transactionExp);
        modifyTransactionFragment.show(getActivity().getSupportFragmentManager(), modifyTransactionFragment.getTag());
    }

    private void initView(View view) {
        transactionType = view.findViewById(R.id.transaction_type);
        transactionCurrency = view.findViewById(R.id.transaction_currency);
        transactionCategory = view.findViewById(R.id.transaction_category);
        transactionNote = view.findViewById(R.id.transaction_note);
        transactionTime = view.findViewById(R.id.transaction_time);
        transactionAmount = view.findViewById(R.id.transaction_price);
        btnModify = view.findViewById(R.id.modify_transaction);
//        btnCancel = view.findViewById(R.id.cancelModify);
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
        transactionAmount.setText(String.format("%s %s", Helper.formatCurrency(transactionExp.getSpend()), transactionExp.getCurrency()));
        transactionTime.setText(Helper.formatDate(transactionExp.getCreatedAt()));
        transactionCategory.setText(String.valueOf(transactionExp.getCategory().getType()));
        transactionType.setText(String.valueOf(transactionExp.getCategory().getName()));
    }
}
