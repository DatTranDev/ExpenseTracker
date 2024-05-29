package com.example.expensetracker.bottom_sheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.BottomSheetModifyWalletBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.UserWallet;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;
import java.util.Objects;

public class ModifyWalletFragment extends BottomSheetDialogFragment {

    private static final String KEY_WALLET = "wallet";
    private Wallet wallet;
    private AppUser user;
    private TextView btnCancel;
    private EditText walletName;
    private EditText walletAmount;
    private Button btnSave;
    private Button btnDelete;
    private WalletViewModel walletViewModel;

    public static ModifyWalletFragment newInstance(Wallet wallet) {
        ModifyWalletFragment fragment = new ModifyWalletFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_WALLET, wallet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wallet = getArguments().getParcelable(KEY_WALLET);
        }
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        BottomSheetModifyWalletBinding binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_modify_wallet, container, false);
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

                wallet.setName(name);
                wallet.setAmount(amount);
                wallet.setCurrency("VND");

                walletViewModel.updateWallet(wallet, getContext());
                dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserWallet userWallet = new UserWallet();
                userWallet.setWalletId(wallet.getId());
                userWallet.setUserId(user.getId());
                userWallet.setDeleted(true);

                int index = 0;
                for (int i = 0; i < walletViewModel.getWalletsLiveData().getValue().size(); i++) {
                    Wallet walletDeleted = walletViewModel.getWalletsLiveData().getValue().get(i);
                    if (Objects.equals(walletDeleted.getId(), wallet.getId())) {
                        index = i;
                        break;
                    }
                }

                walletViewModel.deleteWallet(userWallet, getContext(), index);
                dismiss();
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null) {
            View parent = (View) view.getParent();
            BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            parent.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels * 2 / 3;
            parent.requestLayout();
        }
    }

    private void initView(View view) {
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSave = view.findViewById(R.id.save_wallet);
        btnDelete = view.findViewById(R.id.delete_wallet);
        walletName = view.findViewById(R.id.wallet_name);
        walletAmount = view.findViewById(R.id.wallet_amount);

        if (wallet != null) {
            walletName.setText(wallet.getName());
            walletAmount.setText(wallet.getAmount().toString());
        }
    }
}
