package com.example.expensetracker.Fund;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.example.expensetracker.adapter.MemberShowAdapter;
import com.example.expensetracker.databinding.ActivityFundFragmentBinding;
import com.example.expensetracker.databinding.ActivityFundMemberFragmentBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.UserWallet;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MemberFragmentActivity extends BottomSheetDialogFragment implements MemberShowAdapter.OnMemberModifyClickListener {
    private ImageView imageReturn;
    private Button btnAdd;
    private RecyclerView recyclerView;
    private TextView fundName;
    private MemberShowAdapter memberAdapter;
    private WalletViewModel walletViewModel;
    private AppUser user;
    private Wallet wallet;

    public MemberFragmentActivity(Wallet wallet) {
        this.wallet = wallet;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityFundMemberFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_fund_member_fragment, container, false);
        binding.setLifecycleOwner(this);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(binding.getRoot());
        observeViewModel();

        fundName.setText("Thành viên quỹ " + wallet.getName());

        walletViewModel.loadMembers(user.getId(), this.wallet);
        observeViewModel();

        imageReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMember();
            }
        });

        return binding.getRoot();
    }

    private void observeViewModel() {
        walletViewModel.getUsersLiveData().observe(getViewLifecycleOwner(), new Observer<List<AppUser>>() {
            @Override
            public void onChanged(List<AppUser> users) {
                memberAdapter.updateAppUsers(users);
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
        btnAdd = view.findViewById(R.id.add_member);
        fundName = view.findViewById(R.id.txtFundName);
        imageReturn = view.findViewById(R.id.imageQuayLai);
        recyclerView = view.findViewById(R.id.member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        memberAdapter = new MemberShowAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(memberAdapter);
    }

    private void addMember() {
        MemberAdd fundAddMember = new MemberAdd(wallet);
        fundAddMember.show(requireActivity().getSupportFragmentManager(), fundAddMember.getTag());
    }

    @Override
    public void onMemberModifyClick(AppUser appUser) {
        MemberModifyActivity memberModifyActivity = new MemberModifyActivity(wallet, appUser);
        memberModifyActivity.show(requireActivity().getSupportFragmentManager(), memberModifyActivity.getTag());
    }
}


