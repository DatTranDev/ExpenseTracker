package com.example.expensetracker.view.addTransaction;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ChooseWalletFragment extends BottomSheetDialogFragment {

    private  RecyclerView recycler;
    private List<Wallet> walletList;
    private BigDecimal totalAmount = new BigDecimal(0);
    private TextView total;

    ChooseWallerAdapter adapter;
    sendWallet send;
    public interface sendWallet{
        void selectedWallet(Wallet selected);
    }
    public void setSend(sendWallet a){
        send=a;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_choose_wallet, null);
        total= view.findViewById(R.id.total_amount);
        getWallets();

        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("lỗi","tới rồi 3");
        // Thiết lập sự kiện cho các nút bấm
        view.findViewById(R.id.close_choosewallet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        Log.d("lỗi","tới rồi 2");
            FragmentActivity activity = getActivity();
            recycler = view.findViewById(R.id.recyclerViewWallet);
            recycler.setLayoutManager(new LinearLayoutManager(activity));
            Log.d("lỗi","tới rồi");
            adapter = new ChooseWallerAdapter(new ArrayList<>());
            recycler.setAdapter(adapter);


        adapter.setOnItemClickListener(new ChooseWallerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Wallet clickedItem =walletList.get(position);
                if(send!=null)
                {
                    send.selectedWallet(clickedItem);
                }
                dismiss();
            }
        });

    }
    public void getWallets()
    {
        Context context = getContext();
        AppUser user = SharedPreferencesManager.getInstance(context).getObject("user", AppUser.class);

        if(user!=null)
        {
            AppUserRepository.getInstance().getWallet(user.getId(), new ApiCallBack<List<Wallet>>() {
                @Override
                public void onSuccess(List<Wallet> wallets) {
                    walletList= wallets;
                    adapter.updateWallets(wallets);
                    for (int i = 0; i < wallets.size(); i++) {
                        totalAmount = totalAmount.add(wallets.get(i).getAmount());
                    }
                    total.setText(Helper.formatCurrency(totalAmount));
                }

                @Override
                public void onError(String message) {

                }
            });

        }
    }





}
