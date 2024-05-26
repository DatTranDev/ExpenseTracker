package com.example.expensetracker.bottom_sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetracker.R;
import com.example.expensetracker.api.Wallet.WalletReq;
import com.example.expensetracker.databinding.BottomSheetAddWalletBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;

public class AddWalletFragment extends BottomSheetDialogFragment {

    private TextView btnCancel;
    private EditText walletName;
    private EditText walletAmount;
    private TextView btnSave;
    private AppUser user;
    private WalletViewModel walletViewModel;

    public static AddWalletFragment newInstance() {
        AddWalletFragment addWalletFragment = new AddWalletFragment();
        return addWalletFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetAddWalletBinding binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_add_wallet, container, false);
        binding.setLifecycleOwner(this);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(binding.getRoot());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = walletName.getText().toString();
                BigDecimal amount = walletAmount.getText().toString().isEmpty() ? BigDecimal.ZERO : new BigDecimal(walletAmount.getText().toString());

                if (name.isEmpty() || amount.equals(BigDecimal.ZERO)) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
                    return;
                }

                WalletReq walletReq = new WalletReq();
                walletReq.setAmount(amount);
                walletReq.setCurrency("VND");
                walletReq.setName(name);
                walletReq.setSharing(false);
                walletReq.setUserId(user.getId());

                walletViewModel.addWallet(walletReq, getContext());
                dismiss();
            }
        });

        return binding.getRoot();
    }

    private void initView(View view) {
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSave = view.findViewById(R.id.save_wallet);
        walletName = view.findViewById(R.id.wallet_name);
        walletAmount = view.findViewById(R.id.wallet_amount);
    }
}
