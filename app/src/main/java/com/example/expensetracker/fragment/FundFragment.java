package com.example.expensetracker.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetracker.Fund.FundFragmentActivity;
import com.example.expensetracker.R;
import com.example.expensetracker.adapter.FundAdapter;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.bottom_sheet.TransactionDetailsFragment;
import com.example.expensetracker.bottom_sheet.WalletFragment;
import com.example.expensetracker.bottom_sheet.WalletUpdateListener;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.viewmodel.TransactionViewModel;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FundFragment extends Fragment implements TransactionAdapter.OnItemClickListener, WalletUpdateListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int MAX_DISPLAY_ITEMS = 3;

    private String mParam1;
    private String mParam2;

    private FundAdapter fundAdapter;
    private TransactionAdapter transactionAdapter;
    private List<TransactionExp> transactionList;
    private List<Wallet> fundList;
    private List<Wallet> subFunds;
    private AppUser user;
    private EditText showFund;
    private EditText showTransaction;
    private TextView emptyWallet;
    private TextView emptyTransaction;
    private WalletViewModel walletViewModel;
    private TransactionViewModel transactionViewModel;

    public FundFragment() {
        // Required empty public constructor
    }

    public static FundFragment newInstance(String param1, String param2) {
        FundFragment fragment = new FundFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fund, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");
        user = new Gson().fromJson(userJson, AppUser.class);

        initView(view);

        setupRecyclerViews(view);
        setupClickListeners();

        walletViewModel.loadFunds(user.getId());
        observeWalletViewModel();

        transactionViewModel.loadTransactions(user.getId());
        observeTransactionViewModel();

        return view;
    }

    private void observeTransactionViewModel() {
        transactionViewModel.getTransactionsLiveData().observe(getViewLifecycleOwner(), new Observer<List<TransactionExp>>() {
            @Override
            public void onChanged(List<TransactionExp> transactions) {
                transactionList = sortTransactionsByDate(transactions);
                if (transactions.isEmpty()) {
                    emptyTransaction.setVisibility(View.VISIBLE);
                } else {
                    emptyTransaction.setVisibility(View.GONE);
                }
                if (transactions.size() > 3) {
                    transactions = transactions.subList(0, 3);
                }
                transactionAdapter.updateTransaction(transactions);
                transactionAdapter.notifyDataSetChanged();
            }
        });

        transactionViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeWalletViewModel() {
        walletViewModel.getWalletsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Wallet>>() {
            @Override
            public void onChanged(List<Wallet> wallets) {
                fundList = wallets;
                if (fundList.isEmpty()) {
                    emptyWallet.setVisibility(View.VISIBLE);
                } else {
                    emptyWallet.setVisibility(View.GONE);
                }
                List<Wallet> sharingWallets = new ArrayList<>();
                for (Wallet wallet : wallets) {
                    if (wallet.isSharing()) {
                        sharingWallets.add(wallet);
                    }
                }
                if (sharingWallets.size() > 3) {
                    subFunds = sharingWallets.subList(0, 3);
                } else {
                    subFunds = sharingWallets;
                }

                fundAdapter.updateWallet(subFunds);
                fundAdapter.notifyDataSetChanged();
            }
        });

        walletViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        emptyWallet = view.findViewById(R.id.wallet_empty);
        emptyTransaction = view.findViewById(R.id.transaction_empty);
        showFund = view.findViewById(R.id.show_fund);
        showTransaction = view.findViewById(R.id.show_transaction);
    }

    private List<TransactionExp> sortTransactionsByDate(List<TransactionExp> transactions) {
        Collections.sort(transactions, (t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));
        return transactions;
    }

    private void setupRecyclerViews(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        LinearLayoutManager walletLayoutManager = new LinearLayoutManager(mainActivity);
        RecyclerView rvWallet = view.findViewById(R.id.fund_list);
        rvWallet.setLayoutManager(walletLayoutManager);
        fundAdapter = new FundAdapter(subFunds);
        rvWallet.setAdapter(fundAdapter);

        LinearLayoutManager transactionLayoutManager = new LinearLayoutManager(mainActivity);
        RecyclerView rvTransaction = view.findViewById(R.id.transaction_list_recent);
        rvTransaction.setLayoutManager(transactionLayoutManager);
        transactionAdapter = new TransactionAdapter(getContext(), transactionList, this);
        rvTransaction.setAdapter(transactionAdapter);
    }

    private void setupClickListeners() {
        showFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllFund(fundList);
            }
        });

        showTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.navigateToTransactions();
                }
            }
        });
    }

    private void showAllFund(List<Wallet> walletList) {
        FundFragmentActivity fundFragmentActivity = FundFragmentActivity.newInstance();
        fundFragmentActivity.show(getActivity().getSupportFragmentManager(), fundFragmentActivity.getTag());
    }

    @Override
    public void onItemClick(TransactionExp transactionExp) {
        TransactionDetailsFragment transactionDetailsFragment = TransactionDetailsFragment.newInstance(transactionExp);
        transactionDetailsFragment.show(getActivity().getSupportFragmentManager(), transactionDetailsFragment.getTag());
    }

    @Override
    public void onWalletAdded(Wallet wallet) {
        fundList.add(wallet);
        fundAdapter.notifyItemInserted(fundList.size() - 1);
        setData();
    }

    @Override
    public void onWalletUpdated(Wallet wallet) {
        for (int i = 0; i < fundList.size(); i++) {
            if (fundList.get(i).getId().equals(wallet.getId())) {
                fundList.set(i, wallet);
                fundAdapter.notifyItemChanged(i);
                setData();
                break;
            }
        }
    }

    @Override
    public void onWalletDeleted(String walletId) {
        for (int i = 0; i < fundList.size(); i++) {
            if (fundList.get(i).getId().equals(walletId)) {
                fundList.remove(i);
                fundAdapter.notifyItemRemoved(i);
                setData();
                break;
            }
        }
    }

    private void setData() {
        BigDecimal result = new BigDecimal(0);

        for (int i = 0; i < fundList.size(); i++) {
            result = result.add(fundList.get(i).getAmount());
        }

        if (fundList.isEmpty()) {
            emptyWallet.setVisibility(View.VISIBLE);
        } else {
            emptyWallet.setVisibility(View.GONE);
        }
    }
}
