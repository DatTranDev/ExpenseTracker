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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.FundTransactionAdapter;
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
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class FundTransactionActivity extends BottomSheetDialogFragment implements FundTransactionAdapter.OnItemClickListener{
    private ImageView btnReturn;
    private Button btnAdd;
    private Wallet wallet;
    private TextView txtTransactionName;
    private FundTransactionAdapter transactionAdapter;
    private List<TransactionExp> allTransactions = new ArrayList<>();
    private WalletViewModel walletViewModel;
    private LinearLayout transactionEmpty;
    private AppUser user;

    public FundTransactionActivity(Wallet wallet){
        this.wallet = wallet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Gọi lại các hàm cần thiết để làm mới dữ liệu và cập nhật giao diện
        walletViewModel.loadIsSharingTransactions(this.wallet.getId(), this.wallet);
        observWalletViewModel();
    }

    @NonNull
    @Override
    public synchronized View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fund_transaction, container, false);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(view);
        setupRecycleView(view);

        txtTransactionName.setText("Tất cả giao dịch quỹ " + wallet.getName());

        walletViewModel.loadIsSharingTransactions(this.wallet.getId(), this.wallet);
        observWalletViewModel();

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
        txtTransactionName = view.findViewById(R.id.txtChiTietGiaoDich);
    }

    private void observWalletViewModel() {
        walletViewModel.getTransactionsLiveData().observe(getViewLifecycleOwner(), transactions -> {
            List<TransactionExp> transactionExpsisSharing = new ArrayList<>();
            for (TransactionExp exp : transactions) {
                // Kiểm tra nếu exp hoặc wallet là null
                if (exp != null) {
                    Wallet wallet = exp.getWallet();
                    if (wallet != null && wallet.isSharing() && this.wallet != null && wallet.getId().equals(this.wallet.getId())) {
                        transactionExpsisSharing.add(exp);
                    }
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

        walletViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), errorMessage ->
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show());
    }

    public void setupRecycleView(View view){
        LinearLayoutManager transactionLayoutManager = new LinearLayoutManager(requireActivity());
        RecyclerView rvTransaction = view.findViewById(R.id.fund_transaction_list);
        rvTransaction.setLayoutManager(transactionLayoutManager);
        transactionAdapter = new FundTransactionAdapter(getContext(), allTransactions, this);
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