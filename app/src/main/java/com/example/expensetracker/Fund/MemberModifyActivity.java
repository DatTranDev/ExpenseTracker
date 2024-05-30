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
import com.example.expensetracker.api.Wallet.RemoveMemberReq;
import com.example.expensetracker.databinding.ActivityFundModifyFundBinding;
import com.example.expensetracker.databinding.ActivityFundModifyMemberBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.UserWallet;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;
import java.util.Objects;

public class MemberModifyActivity extends BottomSheetDialogFragment {
    private AppUser user;
    private Wallet wallet;
    private AppUser removeUser;
    private Button btnCancel;
    private Button btnDelete;
    private EditText memberName;
    private EditText memberEmail;
    private ImageView imageReturn;
    private WalletViewModel walletViewModel;

    public MemberModifyActivity(Wallet wallet, AppUser user) {
        this.wallet = wallet;
        this.removeUser = user;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityFundModifyMemberBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_fund_modify_member, container, false);
        binding.setLifecycleOwner(this);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(binding.getRoot());

        memberName.setText(removeUser.getUserName());
        memberEmail.setText(removeUser.getEmail());
        memberName.setEnabled(false);
        memberEmail.setEnabled(false);

        imageReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveMemberReq removeMemberReq = new RemoveMemberReq(wallet.getId(), user.getId(), removeUser.getId());
                walletViewModel.removeMember(removeMemberReq, wallet, requireContext());
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
        imageReturn = view.findViewById(R.id.imageQuayLai);
        btnCancel = view.findViewById(R.id.button_cancel);
        btnDelete = view.findViewById(R.id.delete_member);
        memberName = view.findViewById(R.id.member_name);
        memberEmail = view.findViewById(R.id.member_email);
    }
}

