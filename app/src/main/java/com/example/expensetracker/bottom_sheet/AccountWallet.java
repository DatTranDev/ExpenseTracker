package com.example.expensetracker.bottom_sheet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.WalletAdapter;
import com.example.expensetracker.adapter.WalletShowAdapter;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.view.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountWallet extends BottomSheetDialogFragment implements WalletUpdateListener, WalletShowAdapter.OnWalletModifyClickListener{

    private static final String KEY_WALLET_LIST = "wallet_list";
    private ImageButton btnCancel;
    private FloatingActionButton btnAdd;
    private RecyclerView recyclerView;
    private WalletShowAdapter walletAdapter;
    private WalletUpdateListener walletUpdateListener;
    private TextView total;
    private List<Wallet> wallets;

    public static AccountWallet newInstance(List<Wallet> walletList) {
        AccountWallet accountWallet = new AccountWallet();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(KEY_WALLET_LIST, new ArrayList<>(walletList));
        accountWallet.setArguments(bundle);
        return accountWallet;
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
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.account_wallet, null);
        bottomSheetDialog.setContentView(viewDialog);
        initView(viewDialog);
        setData();

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    addWallet();
                }
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

                    int maxHeight = getResources().getDisplayMetrics().heightPixels;

                    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.height = maxHeight;;
                        bottomSheet.setLayoutParams(layoutParams);
                    }
                }
            }
        });
        return bottomSheetDialog;
    }

    private void initView(View view) {
        btnAdd = view.findViewById(R.id.account_add_wallet);
        btnCancel = view.findViewById(R.id.wallet_back);
        recyclerView = view.findViewById(R.id.account_wallet_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        walletAdapter = new WalletShowAdapter(wallets, this);
//        total = view.findViewById(R.id.wallet_total_amount);
        recyclerView.setAdapter(walletAdapter);
    }

    private void setData() {
        BigDecimal result = new BigDecimal(0);

        for (int i = 0; i < wallets.size(); i++) {
            result = result.add(wallets.get(i).getAmount());
        }

        String currency = wallets.get(0).getCurrency();
//        total.setText(String.format("%s %s", Helper.formatCurrency(result), currency));
    }

    private void addWallet() {
        AddWalletFragment addWalletFragment = AddWalletFragment.newInstance();
        addWalletFragment.setWalletUpdateListener(this);
        addWalletFragment.show(getActivity().getSupportFragmentManager(), addWalletFragment.getTag());
    }

    @Override
    public void onWalletModifyClick(Wallet wallet) {
        ModifyWalletFragment modifyWalletFragment = ModifyWalletFragment.newInstance(wallet);
        modifyWalletFragment.setWalletUpdateListener(this);
        modifyWalletFragment.show(getActivity().getSupportFragmentManager(), modifyWalletFragment.getTag());
    }

    @Override
    public void onWalletAdded(Wallet wallet) {
        wallets.add(wallet);
        walletAdapter.notifyItemInserted(wallets.size() - 1);
        setData();
        walletUpdateListener.onWalletAdded(wallet);
    }

    @Override
    public void onWalletUpdated(Wallet wallet) {
        for (int i = 0; i < wallets.size(); i++) {
            if (wallets.get(i).getId().equals(wallet.getId())) {
                wallets.set(i, wallet);
                walletAdapter.notifyItemChanged(i);
                setData();
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
                setData();
                break;
            }
        }
        walletUpdateListener.onWalletDeleted(walletId);
    }
    public void setWalletUpdateListener(WalletUpdateListener listener) {
        this.walletUpdateListener = listener;
    }

}
