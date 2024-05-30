package com.example.expensetracker.Fund;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.MemberShowAdapter;
import com.example.expensetracker.databinding.ActivityFundFragmentBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.UserWallet;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class MemberFragmentActivity extends BottomSheetDialogFragment implements MemberShowAdapter.OnMemberModifyClickListener{
    private ImageView imageReturn;
    private Button btnAdd;
    private RecyclerView recyclerView;
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
        ActivityFundFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_fund_member_fragment, container, false);
        binding.setLifecycleOwner(this);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(binding.getRoot());
        observeViewModel();

        walletViewModel.loadFunds(user.getId());

        imageReturn.setOnClickListener(v -> dismiss());

        btnAdd.setOnClickListener(v -> addMember());

        return binding.getRoot();
    }

    private void observeViewModel() {
        walletViewModel.getWalletsLiveData().observe(getViewLifecycleOwner(), wallets -> {
            // Lọc ra danh sách các thành viên từ danh sách các ví
            List<AppUser> members = new ArrayList<>();
            for (Wallet wallet : wallets) {
                members.addAll(wallet.getMembers());
            }
            memberAdapter.updateAppUsers(members);
        });

        walletViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), errorMessage ->
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show()
        );
    }

    private void initView(View view) {
        btnAdd = view.findViewById(R.id.add_member);
        imageReturn = view.findViewById(R.id.imageQuayLai);
        recyclerView = view.findViewById(R.id.member_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        memberAdapter = new MemberShowAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(memberAdapter);
    }

    private void addMember() {
        MemberAdd fundAddMember = new MemberAdd(this.wallet);
        fundAddMember.show(getActivity().getSupportFragmentManager(), fundAddMember.getTag());
    }

    @Override
    public void onMemberModifyClick(AppUser appUser) {
        MemberModifyActivity memberModifyActivity = new MemberModifyActivity(this.wallet, appUser);
        memberModifyActivity.show(getActivity().getSupportFragmentManager(), memberModifyActivity.getTag());
    }
}

