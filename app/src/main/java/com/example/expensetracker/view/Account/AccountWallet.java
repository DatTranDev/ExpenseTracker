package com.example.expensetracker.view.Account;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensetracker.R;
import com.example.expensetracker.adapter.WalletShowAdapter;
import com.example.expensetracker.bottom_sheet.AddWalletFragment;
import com.example.expensetracker.bottom_sheet.ModifyWalletFragment;
import com.example.expensetracker.bottom_sheet.WalletUpdateListener;
import com.example.expensetracker.databinding.AccountWalletBinding;
import com.example.expensetracker.databinding.ActivityAddCategoryBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.example.expensetracker.viewmodel.accountVM.AccountWalletViewModel;
import com.example.expensetracker.viewmodel.accountVM.AddCategoryViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountWallet extends AppCompatActivity implements WalletUpdateListener, WalletShowAdapter.OnWalletModifyClickListener{

    private static final String KEY_WALLET_LIST = "wallet_list";
    private ImageButton btnCancel;
    private Button btnAdd;
    private AppUser user;
    private WalletShowAdapter walletAdapter;
    private WalletUpdateListener walletUpdateListener;

    private WalletViewModel walletViewModel;
    private AccountWalletViewModel accountWalletViewModel;
    private List<Wallet> wallets;

//    public static AccountWallet newInstance(List<Wallet> walletList) {
//        AccountWallet accountWallet = new AccountWallet();
//        Bundle bundle = new Bundle();
//        bundle.putParcelableArrayList(KEY_WALLET_LIST, new ArrayList<>(walletList));
//        accountWallet.setArguments(bundle);
//        return accountWallet;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_wallet);


        AccountWalletBinding binding = DataBindingUtil.setContentView(this, R.layout.account_wallet);
//        accountWalletViewModel = new AccountWalletViewModel();
//        binding.setAccountWalletViewModel(accountWalletViewModel);
        walletViewModel =  new ViewModelProvider(this).get(WalletViewModel.class);


        initView();
        observeViewModel();

        user = SharedPreferencesManager.getInstance(this).getObject("user", AppUser.class);
//        accountWalletViewModel.loadWallets(user.getId());
        walletViewModel.loadWallets(user.getId(), this);
        btnCancel.setOnClickListener(v -> finish());

        btnAdd.setOnClickListener(v -> {
            addWallet();
            observeViewModel();
        });

    }

    private void observeViewModel() {
        walletViewModel.getWalletsLiveData().observe(this, wallets -> {
            walletAdapter.updateWallet(wallets);
            //setData(wallets);
        });

        walletViewModel.getErrorMessageLiveData().observe(this, errorMessage ->
                Toast.makeText(AccountWallet.this, errorMessage, Toast.LENGTH_SHORT).show());
    }

    private void initView() {
        btnAdd = findViewById(R.id.account_add_wallet);
        btnCancel = findViewById(R.id.wallet_back);
        RecyclerView recyclerView = findViewById(R.id.account_wallet_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        walletAdapter = new WalletShowAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(walletAdapter);
    }

//    private void setData() {
//        BigDecimal result = new BigDecimal(0);
//        for (int i = 0; i < wallets.size(); i++) {
//            result = result.add(wallets.get(i).getAmount());
//        }
//
//        //String currency = wallets.get(0).getCurrency();
//    }

    private void addWallet() {
        AddWalletFragment addWalletFragment = AddWalletFragment.newInstance();
//        addWalletFragment.setWalletUpdateListener(this);
        addWalletFragment.show(getSupportFragmentManager(), addWalletFragment.getTag());
        observeViewModel();
    }

    @Override
    public void onWalletModifyClick(Wallet wallet) {
        ModifyWalletFragment modifyWalletFragment = ModifyWalletFragment.newInstance(wallet);
        modifyWalletFragment.show(getSupportFragmentManager(), modifyWalletFragment.getTag());
        observeViewModel();
    }

    @Override
    public void onWalletAdded(Wallet wallet) {
        wallets.add(wallet);
        walletAdapter.notifyItemInserted(wallets.size() - 1);
        //setData();
        walletUpdateListener.onWalletAdded(wallet);
    }

    @Override
    public void onWalletUpdated(Wallet wallet) {
        for (int i = 0; i < wallets.size(); i++) {
            if (wallets.get(i).getId().equals(wallet.getId())) {
                wallets.set(i, wallet);
                walletAdapter.notifyItemChanged(i);
               // setData();
                break;
            }
        }
        walletUpdateListener.onWalletUpdated(wallet);
    }

    @Override
    public void onWalletDeleted(String walletId) {
        for (int i = 0; i < wallets.size(); i++) {
            if (wallets.get(i).getId().equals(walletId)) {
                wallets.remove(i);
                walletAdapter.notifyItemRemoved(i);
                //setData();
                break;
            }
        }
        walletUpdateListener.onWalletDeleted(walletId);
    }
    public void setWalletUpdateListener(WalletUpdateListener listener) {
        this.walletUpdateListener = listener;
    }

}
