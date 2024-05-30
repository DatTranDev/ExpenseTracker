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

public class FundShowAdapter extends RecyclerView.Adapter<FundShowAdapter.FundShowViewHolder> {
    private List<Wallet> fundList;
    private final OnFundModifyClickListener fundModifyListener;

    public FundShowAdapter(List<Wallet> wallets, OnFundModifyClickListener listener) {
        this.fundList = wallets;
        this.fundModifyListener = listener;
    }

    public interface OnFundModifyClickListener {
        void onFundModifyClick(Wallet wallet);
    }

    @NonNull
    @Override
    public FundShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fund_item_show, parent, false);
        return new FundShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FundShowAdapter.FundShowViewHolder holder, int position) {
        Wallet wallet = fundList.get(position);
        if (wallet == null) {
            return;
        }

        holder.fundAmount.setText(Helper.formatCurrency(wallet.getAmount()));
        holder.fundName.setText(wallet.getName());

        holder.modifyFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fundModifyListener.onFundModifyClick(wallet);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (fundList != null) {
            return fundList.size();
        }
        return 0;
    }

    public static class FundShowViewHolder extends RecyclerView.ViewHolder {
        private final TextView fundName;
        private final TextView fundAmount;
        private final TextView modifyFund;

        public FundShowViewHolder(@NonNull View itemView) {
            super(itemView);
            fundName = itemView.findViewById(R.id.fund_name);
            fundAmount = itemView.findViewById(R.id.fund_amount);
            modifyFund = itemView.findViewById(R.id.modify_fund);
        }
    }

    public void updateWallet(List<Wallet> wallets) {
        this.fundList = wallets;
        notifyDataSetChanged();
    }
}
