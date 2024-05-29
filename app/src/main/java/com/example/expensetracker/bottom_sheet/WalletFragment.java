package com.example.expensetracker.bottom_sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.WalletShowAdapter;

import com.example.expensetracker.databinding.BottomSheetWalletBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class WalletFragment extends BottomSheetDialogFragment implements WalletShowAdapter.OnWalletModifyClickListener {
    private TextView btnCancel;
    private Button btnAdd;
    private RecyclerView recyclerView;
    private WalletShowAdapter walletAdapter;
    private TextView total;
    private WalletViewModel walletViewModel;
    private AppUser user;

    public static WalletFragment newInstance() {
        return new WalletFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetWalletBinding binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_wallet, container, false);
        binding.setLifecycleOwner(this);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(binding.getRoot());
        observeViewModel();

        walletViewModel.loadWallets(user.getId());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWallet();
            }
        });

        return binding.getRoot();
    }

    private void observeViewModel() {
        walletViewModel.getWalletsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Wallet>>() {
            @Override
            public void onChanged(List<Wallet> wallets) {
                walletAdapter.updateWallet(wallets);
                setData(wallets);
            }
        });

        walletViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
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

    private void initView(View view) {
        btnAdd = view.findViewById(R.id.add_wallet);
        btnCancel = view.findViewById(R.id.close_wallet);
        recyclerView = view.findViewById(R.id.wallet_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        walletAdapter = new WalletShowAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(walletAdapter);
        total = view.findViewById(R.id.wallet_total_amount);
    }

    private void addWallet() {
        AddWalletFragment addWalletFragment = AddWalletFragment.newInstance();
        addWalletFragment.show(getActivity().getSupportFragmentManager(), addWalletFragment.getTag());
    }

    private void setData(List<Wallet> wallets) {
        BigDecimal result = new BigDecimal(0);

        for (Wallet wallet : wallets) {
            result = result.add(wallet.getAmount());
        }

        total.setText(Helper.formatCurrency(result));
    }

    @Override
    public void onWalletModifyClick(Wallet wallet) {
        ModifyWalletFragment modifyWalletFragment = ModifyWalletFragment.newInstance(wallet);
        modifyWalletFragment.show(getActivity().getSupportFragmentManager(), modifyWalletFragment.getTag());
    }
}
