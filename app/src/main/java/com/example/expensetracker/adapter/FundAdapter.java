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

public class FundAdapter extends RecyclerView.Adapter<FundAdapter.FundViewHolder>{
    private List<Wallet> fundList;

    public FundAdapter(List<Wallet> fundList) {
        this.fundList = fundList;
    }

    @NonNull
    @Override
    public FundAdapter.FundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fund_item, parent, false);
        return new FundAdapter.FundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FundAdapter.FundViewHolder holder, int position) {
        Wallet wallet = fundList.get(position);
        if (wallet == null) {
            return;
        }

        String currency = wallet.getCurrency();
        holder.fundAmount.setText(Helper.formatCurrency(wallet.getAmount()));
        holder.fundName.setText(wallet.getName());
    }

    @Override
    public int getItemCount() {
        if (fundList != null) {
            return fundList.size();
        }
        return 0;
    }

    public class FundViewHolder extends RecyclerView.ViewHolder {
        private TextView fundName;
        private TextView fundAmount;
        public FundViewHolder(@NonNull View itemView) {
            super(itemView);
            fundName = itemView.findViewById(R.id.fund_name);
            fundAmount = itemView.findViewById(R.id.fund_amount);
        }
    }

    public void updateWallet(List<Wallet> wallets) {
        if (fundList != null) {
            fundList.clear();
            fundList.addAll(wallets);
            notifyDataSetChanged();
        } else {
            fundList = wallets;
            notifyDataSetChanged();
        }
    }
}
