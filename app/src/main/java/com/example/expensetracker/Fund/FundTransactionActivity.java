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
    private ImageView btnReturn;
    private Button btnAdd;
    private TransactionAdapter transactionAdapter;
    private List<TransactionExp> allTransactions = new ArrayList<>();
    private TransactionViewModel transactionViewModel;
    private LinearLayout transactionEmpty;
    private AppUser user;

    public FundTransactionActivity(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Gọi lại các hàm cần thiết để làm mới dữ liệu và cập nhật giao diện
        transactionViewModel.loadIsSharingTransactions(user.getId());
        observeTransactionViewModel();
    }

    @NonNull
    @Override
    public synchronized View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fund_transaction, container, false);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(view);
        setupRecycleView(view);

        transactionViewModel.loadIsSharingTransactions(user.getId());
        observeTransactionViewModel();

        btnReturn.setOnClickListener(v -> dismiss());

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), mainAddActivity.class);
            startActivity(intent);
        });

        return view;
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

    public void setupRecycleView(View view){
        LinearLayoutManager transactionLayoutManager = new LinearLayoutManager(requireActivity());
        RecyclerView rvTransaction = view.findViewById(R.id.fund_transaction_list);
        rvTransaction.setLayoutManager(transactionLayoutManager);
        transactionAdapter = new TransactionAdapter(getContext(), allTransactions, this);
        rvTransaction.setAdapter(transactionAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null) {
            View parent = (View) view.getParent();
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            parent.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
            parent.requestLayout();
        }
    }

    @Override
    public void onItemClick(TransactionExp transactionExp) {
        TransactionDetailsFragment transactionDetailsFragment = TransactionDetailsFragment.newInstance(transactionExp);
        transactionDetailsFragment.show(getActivity().getSupportFragmentManager(), transactionDetailsFragment.getTag());
    }
}