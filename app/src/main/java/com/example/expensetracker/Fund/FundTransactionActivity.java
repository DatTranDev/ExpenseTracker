package com.example.expensetracker.Fund;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.bottom_sheet.TransactionDetailsFragment;

import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.view.addTransaction.mainAddActivity;
import com.example.expensetracker.viewmodel.TransactionViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FundTransactionActivity extends BottomSheetDialogFragment implements TransactionAdapter.OnItemClickListener{
    private static final String KEY_WALLET_LIST = "wallet_list";
    private ImageView btnReturn;
    private Button btnAdd;
    private TransactionAdapter transactionAdapter;
    private List<TransactionExp> sharingTransactionExps = new ArrayList<>();
    private List<TransactionExp> allTransactions = new ArrayList<>();
    private TransactionViewModel transactionViewModel;
    private LinearLayout transactionEmpty;
    private View view;

    public FundTransactionActivity(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            allTransactions = getArguments().getParcelableArrayList(KEY_WALLET_LIST);
        }
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.activity_fund_transaction, null);
        bottomSheetDialog.setContentView(viewDialog);


        AppUser user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(viewDialog);
        transactionViewModel.loadIsSharingTransactions(user.getId());
        observeTransactionViewModel();

        btnReturn.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                int maxHeight = getResources().getDisplayMetrics().heightPixels;

                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.height = maxHeight;
                    bottomSheet.setLayoutParams(layoutParams);
                }
            }
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), mainAddActivity.class);
            startActivity(intent);
        });

        MainActivity mainActivity = (MainActivity)getActivity();
        RecyclerView rvTransaction = viewDialog.findViewById(R.id.fund_transaction_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        rvTransaction.setLayoutManager(linearLayoutManager);
        transactionAdapter = new TransactionAdapter(getContext(), allTransactions, this);
        rvTransaction.setAdapter(transactionAdapter);
        return bottomSheetDialog;
    }

    private void initView(View view) {
        btnAdd = view.findViewById(R.id.add_transaction);
        btnReturn = view.findViewById(R.id.imageQuayLai);
        transactionEmpty = view.findViewById(R.id.transaction_empty);
    }

    private void observeTransactionViewModel() {
        transactionViewModel.getTransactionsLiveData().observe(getViewLifecycleOwner(), transactions -> {
            List<TransactionExp> transactionExpsisSharing = new ArrayList<>();
            for (TransactionExp exp : transactions) {
                // Assume each transaction contains a reference to its wallet
                Wallet wallet = exp.getWallet();
                if (wallet != null && wallet.isSharing()) {
                    transactionExpsisSharing.add(exp);
                }
            }

            if (transactionExpsisSharing.isEmpty()) {
                transactionEmpty.setVisibility(View.VISIBLE);
            } else {
                transactionEmpty.setVisibility(View.GONE);
            }

            transactionAdapter.updateTransaction(transactionExpsisSharing);
            transactionAdapter.notifyDataSetChanged();
        });

        transactionViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), errorMessage ->
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show());
    }


    @Override
    public void onItemClick(TransactionExp transactionExp) {
        TransactionDetailsFragment transactionDetailsFragment = TransactionDetailsFragment.newInstance(transactionExp);
        transactionDetailsFragment.show(getActivity().getSupportFragmentManager(), transactionDetailsFragment.getTag());
    }
}