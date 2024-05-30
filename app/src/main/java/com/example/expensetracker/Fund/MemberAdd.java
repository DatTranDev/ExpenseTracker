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
import com.example.expensetracker.api.Wallet.AddMemberReq;
import com.example.expensetracker.databinding.ActivityFundAddFundBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MemberAdd extends BottomSheetDialogFragment {
    ImageView imageQuayLai;
    Button btnHuy;
    Button btnGuiLoiMoi;
    private EditText txtEmail;
    private AppUser user;
    private Wallet wallet;
    private WalletViewModel walletViewModel;

    public MemberAdd(Wallet wallet) {
        this.wallet = wallet;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityFundAddFundBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_fund_add_member, container, false);
        binding.setLifecycleOwner(this);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(binding.getRoot());

        btnHuy.setOnClickListener(v -> dismiss());

        imageQuayLai.setOnClickListener(v -> dismiss());

        btnGuiLoiMoi.setOnClickListener(v -> {
            String email = txtEmail.getText().toString();

            if (email.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }

            AddMemberReq addMemberReq = new AddMemberReq(wallet.getId(), user.getId(), email);

            walletViewModel.addMember(addMemberReq, wallet,getContext());
            dismiss();
        });

        return binding.getRoot();
    }

    private void initView(View view) {
        btnHuy = view.findViewById(R.id.buttonHuy);
        btnGuiLoiMoi = view.findViewById(R.id.buttonGuiLoiMoi);
        txtEmail = view.findViewById(R.id.editTextEmail);
        imageQuayLai = view.findViewById(R.id.imageQuayLai);
    }
}