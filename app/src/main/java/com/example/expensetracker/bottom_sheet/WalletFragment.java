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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.WalletAdapter;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class WalletFragment extends BottomSheetDialogFragment {
    private static final String KEY_WALLET_LIST = "wallet_list";
    private TextView btnCancel;
    private RecyclerView recyclerView;
    private WalletAdapter walletAdapter;
    private List<Wallet> wallets;

    public static WalletFragment newInstance(List<Wallet> walletList) {
        WalletFragment walletFragment = new WalletFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_WALLET_LIST, new ArrayList<>(walletList));
        walletFragment.setArguments(bundle);
        return walletFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wallets = getArguments().getParcelableArrayList(KEY_WALLET_LIST);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_wallet, null);
        bottomSheetDialog.setContentView(viewDialog);
        initView(viewDialog);

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

                    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
                        bottomSheet.setLayoutParams(layoutParams);
                    }
                }
            }
        });
        return bottomSheetDialog;
    }

    private void initView(View view) {
        btnCancel = view.findViewById(R.id.close_wallet);
        recyclerView = view.findViewById(R.id.wallet_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        walletAdapter = new WalletAdapter(wallets);
        recyclerView.setAdapter(walletAdapter);
    }

    private void setTransactionData() {
//        if (transactionExp == null) {
//            return;
//        }
//
//        if (transactionExp.getCurrency().equals("VND")) {
//            transactionCurrency.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_vnd));
//        } else {
//            transactionCurrency.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_dollar));
//        }
//        transactionNote.setText(transactionExp.getNote());
//        transactionAmount.setText(String.valueOf(transactionExp.getSpend()));
//        transactionTime.setText(Helper.formatDate(transactionExp.getCreatedAt()));
//        transactionCategory.setText(String.valueOf(transactionExp.getCategory().getType()));
//        transactionType.setText(String.valueOf(transactionExp.getCategory().getName()));
    }
}
