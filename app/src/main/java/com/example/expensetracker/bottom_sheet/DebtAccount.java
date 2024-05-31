package com.example.expensetracker.bottom_sheet;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.lang.reflect.Type;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.TransactionAdapter;
//import com.example.expensetracker.enums.Type;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.view.addTransaction.mainAddActivity;
import com.example.expensetracker.viewmodel.accountVM.SharedViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DebtAccount extends BottomSheetDialogFragment implements TransactionAdapter.OnItemClickListener {
    private static final String KEY_WALLET_LIST = "wallet_list";
    private ImageButton btnCancel;
    private FloatingActionButton btnAdd;
    private TransactionAdapter transactionAdapter;
    private List<TransactionExp> needtoreceive = new ArrayList<>();

    private List<TransactionExp> needtopay = new ArrayList<>();
    private List<TransactionExp> allTransactions = new ArrayList<>();
    private TabLayout tabLayoutFilter;

    private LinearLayout transactionEmpty;


    public DebtAccount(){}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            allTransactions = getArguments().getParcelableArrayList(KEY_WALLET_LIST);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.account_debt, null);
        bottomSheetDialog.setContentView(viewDialog);


        AppUser user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(viewDialog);

        btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
        Log.e("debt","hih");
        tabLayoutFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterTransactions(user.getId());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                filterTransactions(user.getId());
            }
        });

        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                int maxHeight = getResources().getDisplayMetrics().heightPixels;

                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.height = maxHeight;;
                    bottomSheet.setLayoutParams(layoutParams);
                }
            }
        });

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), mainAddActivity.class);
            startActivity(intent);
        });

        MainActivity mainActivity = (MainActivity)getActivity();
        RecyclerView rvTransaction = viewDialog.findViewById(R.id.account_transaction_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        rvTransaction.setLayoutManager(linearLayoutManager);
        transactionAdapter = new TransactionAdapter(getContext(), allTransactions, this);
        getTransactionsForUser(1);
        rvTransaction.setAdapter(transactionAdapter);


        return bottomSheetDialog;

    }


    private void filterTransactions(String userId) {
        int check = 0;
        switch (getFilter()) {
            case "Cần trả":
                check = 1;
                break;
            case "Cần thu":
                check = 2;
                break;
        }

        getTransactionsForUser(check);
    }

    private String getFilter() {
        TabLayout.Tab tab = tabLayoutFilter.getTabAt(tabLayoutFilter.getSelectedTabPosition());
        String period = tab.getText().toString();
        return period;
    }


    private void initView(View view) {
        btnAdd = view.findViewById(R.id.add_debt);
        btnCancel = view.findViewById(R.id.debt_back);
        transactionEmpty = view.findViewById(R.id.debt_empty);
        tabLayoutFilter = view.findViewById(R.id.debt_filter);

    }

    private void getTransactionsForUser( int check) {
        List<TransactionExp> filteredTransactions = new ArrayList<>();
        Type type = new TypeToken<List<TransactionExp>>() {}.getType();
        List<TransactionExp> current = SharedPreferencesManager.getInstance(getContext()).getList("transactions",type);
        if (current != null) {
                allTransactions = current;
                for (TransactionExp transaction : allTransactions) {
                    if (check == 1) {
                        List<String> income = Arrays.asList("Trả nợ");
                        if (income.contains(transaction.getCategory().getType())) {
                            filteredTransactions.add(transaction);
                        }
                    } else if (check == 2) {
                        List<String> income1 = Arrays.asList("Cho vay");
                        if (income1.contains(transaction.getCategory().getType()))
                            filteredTransactions.add(transaction);
                    }
                transactionAdapter.updateTransaction(filteredTransactions);
                transactionAdapter.notifyDataSetChanged();

                if (!filteredTransactions.isEmpty()) {
                    transactionEmpty.setVisibility(View.GONE);
                } else {
                    transactionEmpty.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (getFilter()) {
            case "Cần trả":
                getTransactionsForUser(1);
                break;
            case "Cần thu":
                getTransactionsForUser(2);
                break;
        }
    }

    @Override
    public void onItemClick(TransactionExp transactionExp) {
        TransactionDetailsFragment transactionDetailsFragment = TransactionDetailsFragment.newInstance(transactionExp);

        transactionDetailsFragment.show(getActivity().getSupportFragmentManager(), transactionDetailsFragment.getTag());

    }


}
