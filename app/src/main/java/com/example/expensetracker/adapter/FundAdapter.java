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

public class FundAdapter extends RecyclerView.Adapter<FundAdapter.FundViewHolder> {
    private List<Wallet> fundList;
    private OnFundClickListener onFundClickListener;

    public FundAdapter(List<Wallet> fundList, OnFundClickListener onFundClickListener) {
        this.fundList = fundList;
        this.onFundClickListener = onFundClickListener;
    }

    public interface OnFundClickListener {
        void onFundClick(Wallet wallet);
    }

    @NonNull
    @Override
    public FundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fund_item, parent, false);
        return new FundViewHolder(view, onFundClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FundViewHolder holder, int position) {
        Wallet wallet = fundList.get(position);
        if (wallet != null) {
            holder.fundAmount.setText(Helper.formatCurrency(wallet.getAmount()));
            holder.fundName.setText(wallet.getName());
            holder.itemView.setTag(wallet); // Set the wallet object as the tag of the itemView
        }
    }

    @Override
    public int getItemCount() {
        return fundList != null ? fundList.size() : 0;
    }

    public void updateWallet(List<Wallet> wallets) {
        if (fundList != null) {
            fundList.clear();
            fundList.addAll(wallets);
        } else {
            fundList = wallets;
        }
        notifyDataSetChanged();
    }

    public static class FundViewHolder extends RecyclerView.ViewHolder {
        private final TextView fundName;
        private final TextView fundAmount;

        public FundViewHolder(@NonNull View itemView, OnFundClickListener listener) {
            super(itemView);
            fundName = itemView.findViewById(R.id.fund_name);
            fundAmount = itemView.findViewById(R.id.fund_amount);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Wallet clickedWallet = (Wallet) itemView.getTag(); // Get the wallet object from the itemView tag
                        listener.onFundClick(clickedWallet); // Pass the wallet object to the listener
                    }
                }
            });
        }
    }
}
