package com.example.expensetracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;

import java.util.List;

public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.WalletViewHolder> {
    private List<Wallet> walletList;

    public WalletAdapter(List<Wallet> walletList) {
        this.walletList = walletList;
    }

    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_item, parent, false);

        return new WalletAdapter.WalletViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, int position) {
        Wallet wallet = walletList.get(position);
        if (wallet == null) {
            return;
        }

        String currency = wallet.getCurrency();
        holder.walletAmount.setText(Helper.formatCurrency(wallet.getAmount()));
        holder.walletName.setText(wallet.getName());
    }

    @Override
    public int getItemCount() {
        if (walletList != null) {
            return walletList.size();
        }
        return 0;
    }

    public class WalletViewHolder extends RecyclerView.ViewHolder {
        private TextView walletName;
        private TextView walletAmount;
        public WalletViewHolder(@NonNull View itemView) {
            super(itemView);
            walletName = itemView.findViewById(R.id.wallet_name);
            walletAmount = itemView.findViewById(R.id.wallet_amount);
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
