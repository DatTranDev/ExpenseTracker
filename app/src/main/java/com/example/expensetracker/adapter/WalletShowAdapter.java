package com.example.expensetracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;

import java.util.List;

public class WalletShowAdapter extends RecyclerView.Adapter<WalletShowAdapter.WalletShowViewHolder> {
    private List<Wallet> walletList;
    private OnWalletModifyClickListener walletModifyListener;

    public WalletShowAdapter(List<Wallet> walletList, OnWalletModifyClickListener listener) {
        this.walletList = walletList;
        this.walletModifyListener = listener;
    }

    public interface OnWalletModifyClickListener {
        void onWalletModifyClick(Wallet wallet);
    }

    @NonNull
    @Override
    public WalletShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item_show, parent, false);
        return new WalletShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletShowViewHolder holder, int position) {
        Wallet wallet = walletList.get(position);
        if (wallet == null) {
            return;
        }

        String currency = wallet.getCurrency();
        holder.walletAmount.setText(Helper.formatCurrency(wallet.getAmount()));
        holder.walletName.setText(wallet.getName());

        holder.modifyWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                walletModifyListener.onWalletModifyClick(wallet);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (walletList != null) {
            return walletList.size();
        }
        return 0;
    }

    public static class WalletShowViewHolder extends RecyclerView.ViewHolder {
        private TextView walletName;
        private TextView walletAmount;
        private TextView modifyWallet;

        public WalletShowViewHolder(@NonNull View itemView) {
            super(itemView);
            walletName = itemView.findViewById(R.id.wallet_name);
            walletAmount = itemView.findViewById(R.id.wallet_amount);
            modifyWallet = itemView.findViewById(R.id.modify_wallet);
        }
    }

    public void updateWallet(List<Wallet> wallets) {
        if (walletList != null) {
            walletList.clear();
            walletList.addAll(wallets);
            notifyDataSetChanged();
        } else {
            walletList = wallets;
            notifyDataSetChanged();
        }
    }
}
