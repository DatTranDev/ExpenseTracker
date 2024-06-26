package com.example.expensetracker.Fund;

import android.content.Context;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.FundShowAdapter;
import com.example.expensetracker.databinding.ActivityFundFragmentBinding;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class FundFragmentActivity extends BottomSheetDialogFragment implements FundShowAdapter.OnFundModifyClickListener {
    private ImageView imageReturn;
    private Button btnAdd;
    private RecyclerView recyclerView;
    private FundShowAdapter fundAdapter;
    private TextView total;
    private WalletViewModel walletViewModel;
    private static AppUser user;

    public static FundFragmentActivity newInstance() {
        return new FundFragmentActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ActivityFundFragmentBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_fund_fragment, container, false);
        binding.setLifecycleOwner(this);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        initView(binding.getRoot());
        observeViewModel();

        walletViewModel.loadFunds(user.getId());

        imageReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFund();
            }
        });

        return binding.getRoot();
    }

    private void observeViewModel() {
        walletViewModel.getWalletsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Wallet>>() {
            @Override
            public void onChanged(List<Wallet> wallets) {
                fundAdapter.updateWallet(wallets);
                setData(wallets);
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
        btnAdd = view.findViewById(R.id.add_fund);
        imageReturn = view.findViewById(R.id.imageQuayLai);
        recyclerView = view.findViewById(R.id.fund_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fundAdapter = new FundShowAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(fundAdapter);
        total = view.findViewById(R.id.fund_total_amount);
    }

    private void addFund() {
        FundAddFund fundAddFund = FundAddFund.newInstance();
        fundAddFund.show(getActivity().getSupportFragmentManager(), fundAddFund.getTag());
    }

    private void setData(List<Wallet> wallets) {
        BigDecimal result = BigDecimal.ZERO;

        for (Wallet wallet : wallets) {
            result = result.add(wallet.getAmount());
        }

        total.setText(Helper.formatCurrency(result));
    }

    @Override
    public void onFundModifyClick(Wallet wallet) {
        FundModifyFundActivity fundModifyFundActivity = FundModifyFundActivity.newInstance(wallet);
        fundModifyFundActivity.show(getActivity().getSupportFragmentManager(), fundModifyFundActivity.getTag());

    }
}
