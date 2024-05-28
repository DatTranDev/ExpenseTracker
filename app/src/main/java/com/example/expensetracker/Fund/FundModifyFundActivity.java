package com.example.expensetracker.Fund;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityFundModifyFundBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.UserWallet;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;
import java.util.Objects;

public class FundModifyFundActivity extends BottomSheetDialogFragment {
    private static final String KEY_WALLET = "wallet";
    private Wallet wallet;
    private AppUser user;
    private EditText walletName;
    private EditText walletAmount;
    private Button btnSave;
    private Button btnDelete;
    private ImageView imageReturn;
    private WalletViewModel walletViewModel;

    public static FundModifyFundActivity newInstance(Wallet wallet) {
        FundModifyFundActivity fundModifyFundActivity = new FundModifyFundActivity();
        Bundle args = new Bundle();
        args.putParcelable(KEY_WALLET, wallet);
        fundModifyFundActivity.setArguments(args);
        return fundModifyFundActivity;
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
        ActivityFundModifyFundBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_fund_modify_fund, container, false);
        binding.setLifecycleOwner(this);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);
        initView(binding.getRoot());

        setupListeners();

        return binding.getRoot();
    }

    private void initView(View view) {
        imageReturn = view.findViewById(R.id.imageQuayLai);
        btnSave = view.findViewById(R.id.save_fund);
        btnDelete = view.findViewById(R.id.delete_fund);
        walletName = view.findViewById(R.id.fund_name);
        walletAmount = view.findViewById(R.id.fund_amount);

        if (wallet != null) {
            walletName.setText(wallet.getName());
            walletAmount.setText(wallet.getAmount().toString());
        }
    }

    private void setupListeners() {
        imageReturn.setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> {
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
        });

        btnDelete.setOnClickListener(v -> {
            if (wallet == null || user == null) {
                Toast.makeText(getContext(), "Error: Fund or user data is missing.", Toast.LENGTH_SHORT).show();
                return;
            }

            UserWallet userWallet = new UserWallet();
            userWallet.setWalletId(wallet.getId());
            userWallet.setUserId(user.getId());
            userWallet.setDeleted(true);

            int index = -1;
            for (int i = 0; i < walletViewModel.getWalletsLiveData().getValue().size(); i++) {
                Wallet walletDeleted = walletViewModel.getWalletsLiveData().getValue().get(i);
                if (Objects.equals(walletDeleted.getId(), wallet.getId())) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                walletViewModel.deleteWallet(userWallet, getContext(), index);
                dismiss();
            } else {
                Toast.makeText(getContext(), "Error: Fund not found.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
