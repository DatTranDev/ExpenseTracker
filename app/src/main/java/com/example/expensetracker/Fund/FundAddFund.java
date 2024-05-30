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
import com.example.expensetracker.api.Wallet.WalletReq;
import com.example.expensetracker.databinding.ActivityFundAddFundBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;

public class FundAddFund extends BottomSheetDialogFragment {
    private ImageView imageQuayLai;
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

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(binding.getRoot());

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        imageQuayLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

            parent.getLayoutParams().height = getResources().getDisplayMetrics().heightPixels / 2;
            parent.requestLayout();
        }
    }

    private void initView(View view) {
        btnHuy = view.findViewById(R.id.buttonHuy);
        btnLuu = view.findViewById(R.id.buttonTaoQuy);
        txtTenQuyMoi = view.findViewById(R.id.editTextTenQuyMoi);
        txtMucTieu = view.findViewById(R.id.editTextMucTieu);
        imageQuayLai = view.findViewById(R.id.imageQuayLai);
    }
}
