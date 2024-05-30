package com.example.expensetracker.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetracker.Fund.FundFragmentActivity;
import com.example.expensetracker.Fund.FundTransactionActivity;
import com.example.expensetracker.Fund.MemberFragmentActivity;
import com.example.expensetracker.R;
import com.example.expensetracker.adapter.FundAdapter;
import com.example.expensetracker.adapter.MemberAdapter;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.bottom_sheet.TransactionDetailsFragment;
import com.example.expensetracker.bottom_sheet.WalletUpdateListener;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.TransactionViewModel;
import com.example.expensetracker.viewmodel.WalletViewModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FundFragment extends Fragment implements TransactionAdapter.OnItemClickListener, WalletUpdateListener, FundAdapter.OnFundClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int MAX_DISPLAY_ITEMS = 3;

    private String mParam1;
    private String mParam2;

    private Wallet currentWallet;
    private FundAdapter fundAdapter;
    private MemberAdapter memberAdapter;
    private TransactionAdapter transactionAdapter;
    private List<TransactionExp> transactionList = new ArrayList<>();
    private List<Wallet> fundList = new ArrayList<>();
    private List<Wallet> subFunds = new ArrayList<>();
    private AppUser user;
    private EditText showFund;
    private EditText showTransaction;
    private EditText showMember;
    private TextView emptyWallet;
    private TextView emptyTransaction;
    private TextView emptyMember;
    private WalletViewModel walletViewModel;
    private TransactionViewModel transactionViewModel;
    private ProgressBar progressBar;
    private View overlay;

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

    @Nullable
    @Override
    public synchronized View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fund, container, false);

        user = SharedPreferencesManager.getInstance(getContext()).getObject("user", AppUser.class);
        initView(view);

        setupRecyclerViews(view);
        setupClickListeners();
        observeLoadingState();

        walletViewModel.loadFunds(user.getId());
        walletViewModel.getWalletsLiveData().observe(getViewLifecycleOwner(), wallets -> {
            fundAdapter.updateWallet(wallets);
            fundAdapter.notifyDataSetChanged();
        });
        observeWalletViewModel();

        transactionViewModel.loadIsSharingTransactions(user.getId());
        observeTransactionViewModel();

        if(!walletViewModel.getWalletsLiveData().getValue().isEmpty()) {
            currentWallet = walletViewModel.getWalletsLiveData().getValue().get(0);
            Toast.makeText(getContext(), "Đã chọn quỹ " + currentWallet.getName(), Toast.LENGTH_SHORT).show();
        }

        FundFragmentActivity fundFragmentActivity = FundFragmentActivity.newInstance();
        fundFragmentActivity.show(getActivity().getSupportFragmentManager(), fundFragmentActivity.getTag());
        fundFragmentActivity.dismiss();

        return view;
    }

    private void observeLoadingState() {
        walletViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> updateLoadingState());
        transactionViewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> updateLoadingState());
    }

    private void updateLoadingState() {
        Boolean isWalletLoading = walletViewModel.getIsLoading().getValue();
        Boolean isTransactionLoading = transactionViewModel.getIsLoading().getValue();

        if (Boolean.TRUE.equals(isWalletLoading) || Boolean.TRUE.equals(isTransactionLoading)) {
            overlay.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            overlay.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
        }
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
                emptyTransaction.setVisibility(View.VISIBLE);
            } else {
                emptyTransaction.setVisibility(View.GONE);
            }

            if (transactionExpsisSharing.size() > MAX_DISPLAY_ITEMS) {
                transactionExpsisSharing = transactionExpsisSharing.subList(0, MAX_DISPLAY_ITEMS);
            }

            transactionAdapter.updateTransaction(transactionExpsisSharing);
            transactionAdapter.notifyDataSetChanged();
        });

        transactionViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), errorMessage ->
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show());
    }


    private void observeWalletViewModel() {
        walletViewModel.getWalletsLiveData().observe(getViewLifecycleOwner(), wallets -> {
            if (!wallets.isEmpty()) {
                fundList = wallets;
                List<Wallet> sharingWallets = new ArrayList<>();
                for (Wallet wallet : wallets) {
                    if (wallet.isSharing()) {
                        sharingWallets.add(wallet);
                    }
                }
                subFunds = sharingWallets;
                fundAdapter.updateWallet(subFunds);

                if (!subFunds.isEmpty()) {
                    currentWallet = subFunds.get(0);
                    loadMembersForCurrentWallet(); // Load members for the first wallet
                    fundAdapter.setSelectedPosition(0); // Set the first item as selected
                } else {
                    Log.e("FundFragment", "Wallet list is empty");
                }
            }

            walletViewModel.getUsersLiveData().observe(getViewLifecycleOwner(), members -> {
                if (currentWallet != null) {
                    List<AppUser> appUserList = currentWallet.getMembers();
                    if (appUserList == null) {
                        appUserList = new ArrayList<>(); // Ensure the list is not null
                    }
                    if(appUserList.size() > MAX_DISPLAY_ITEMS){
                       appUserList = appUserList.subList(0,MAX_DISPLAY_ITEMS);
                    }
                    memberAdapter.updateMemberWallet(appUserList);
                    memberAdapter.notifyDataSetChanged();
                } else {
                    Log.e("FundFragment", "currentWallet is null");
                }
            });

            walletViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), errorMessage ->
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show());
        });
    }


    private void loadMembersForCurrentWallet() {
        if (currentWallet != null) {
            walletViewModel.loadMembers(user.getId(), currentWallet);
        }
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.progress_bar_fund);
        overlay = view.findViewById(R.id.overlay);
        emptyWallet = view.findViewById(R.id.wallet_empty);
        emptyTransaction = view.findViewById(R.id.transaction_empty);
        emptyMember = view.findViewById(R.id.member_empty);
        showFund = view.findViewById(R.id.show_fund);
        showTransaction = view.findViewById(R.id.show_transaction);
        showMember = view.findViewById(R.id.show_member);
    }

    private void setupRecyclerViews(View view) {
        // Initialize wallet RecyclerView
        LinearLayoutManager walletLayoutManager = new LinearLayoutManager(requireActivity());
        RecyclerView rvWallet = view.findViewById(R.id.fund_list);
        rvWallet.setLayoutManager(walletLayoutManager);
        fundAdapter = new FundAdapter(subFunds, this);
        rvWallet.setAdapter(fundAdapter);

        // Initialize transaction RecyclerView
        LinearLayoutManager transactionLayoutManager = new LinearLayoutManager(requireActivity());
        RecyclerView rvTransaction = view.findViewById(R.id.transaction_list_fund);
        rvTransaction.setLayoutManager(transactionLayoutManager);
        transactionAdapter = new TransactionAdapter(getContext(), transactionList, this);
        rvTransaction.setAdapter(transactionAdapter);

        // Initialize member RecyclerView
        LinearLayoutManager memberLayoutManager = new LinearLayoutManager(requireActivity());
        RecyclerView rvMember = view.findViewById(R.id.member_list);
        rvMember.setLayoutManager(memberLayoutManager);
        memberAdapter = new MemberAdapter(currentWallet != null ? currentWallet.getMembers() : new ArrayList<>());
        rvMember.setAdapter(memberAdapter);
    }

    private void setupClickListeners() {
        showFund.setOnClickListener(v -> showAllFund(fundList));

        showMember.setOnClickListener(v -> showAllMember());

        showTransaction.setOnClickListener(v -> showAllTransaction());

    }

    private void showAllTransaction(){
        FundTransactionActivity fundTransactionActivity = new FundTransactionActivity();
        fundTransactionActivity.show(getActivity().getSupportFragmentManager(), fundTransactionActivity.getTag());
    }

    private void showAllFund(List<Wallet> walletList) {
        FundFragmentActivity fundFragmentActivity = FundFragmentActivity.newInstance();
        fundFragmentActivity.show(getActivity().getSupportFragmentManager(), fundFragmentActivity.getTag());
    }

    private void showAllMember(){
        MemberFragmentActivity memberFragmentActivity = new MemberFragmentActivity(currentWallet);
        memberFragmentActivity.show(getActivity().getSupportFragmentManager(), memberFragmentActivity.getTag());
    }

    @Override
    public void onItemClick(TransactionExp transactionExp) {
        TransactionDetailsFragment transactionDetailsFragment = TransactionDetailsFragment.newInstance(transactionExp);
        transactionDetailsFragment.show(getActivity().getSupportFragmentManager(), transactionDetailsFragment.getTag());
    }

    @Override
    public void onFundClick(Wallet wallet) {
        currentWallet = wallet;
        if (currentWallet != null) {
            walletViewModel.loadMembers(user.getId(), currentWallet);
            Toast.makeText(getContext(), "Đã chọn quỹ " + currentWallet.getName(), Toast.LENGTH_SHORT).show();
            // Hiển thị lại danh sách thành viên
            List<AppUser> members = currentWallet.getMembers();
            memberAdapter.updateMemberWallet(members != null ? members : new ArrayList<>());
            memberAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "Chưa có quỹ nào được chọn", Toast.LENGTH_SHORT).show();
        }
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

