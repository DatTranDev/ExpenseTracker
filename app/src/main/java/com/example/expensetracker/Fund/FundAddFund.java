package com.example.expensetracker.Fund;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.expensetracker.api.Wallet.WalletReq;
import com.example.expensetracker.databinding.ActivityFundAddFundBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.math.BigDecimal;

public class FundAddFund extends BottomSheetDialogFragment {
    ImageView imageQuayLai;
    private Button btnHuy;
    private EditText txtTenQuyMoi;
    private EditText txtMucTieu;
    private Button btnLuu;
    private AppUser user;
    private WalletViewModel walletViewModel;

    public static FundAddFund newInstance() {
        return new FundAddFund();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityFundAddFundBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_fund_add_fund, container, false);
        binding.setLifecycleOwner(this);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");
        user = new Gson().fromJson(userJson, AppUser.class);

        initView(binding.getRoot());

        btnHuy.setOnClickListener(v -> dismiss());

        imageQuayLai.setOnClickListener(v -> dismiss());

        btnLuu.setOnClickListener(v -> {
            String name = txtTenQuyMoi.getText().toString();
            BigDecimal amount = txtMucTieu.getText().toString().isEmpty() ? BigDecimal.ZERO : new BigDecimal(txtMucTieu.getText().toString());

            if (name.isEmpty() || amount.equals(BigDecimal.ZERO)) {
                Toast.makeText(getContext(), "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
                return;
            }

            WalletReq walletReq = new WalletReq();
            walletReq.setAmount(amount);
            walletReq.setCurrency("VND");
            walletReq.setName(name);
            walletReq.setSharing(true);
            walletReq.setUserId(user.getId());

            walletViewModel.addWallet(walletReq, getContext());
            dismiss();
        });

        return binding.getRoot();
    }

    private void initView(View view) {
        btnHuy = view.findViewById(R.id.buttonHuy);
        btnLuu = view.findViewById(R.id.buttonTaoQuy);
        txtTenQuyMoi = view.findViewById(R.id.editTextTenQuyMoi);
        txtMucTieu = view.findViewById(R.id.editTextMucTieu);
        imageQuayLai = view.findViewById(R.id.imageQuayLai);
    }
}
