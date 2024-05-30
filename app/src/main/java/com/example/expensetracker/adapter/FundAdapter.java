package com.example.expensetracker.adapter;

import android.graphics.Color;
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
    private int selectedPosition = RecyclerView.NO_POSITION;

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
            holder.itemView.setTag(wallet);
            holder.itemView.setBackgroundColor(selectedPosition == position ? Color.GREEN : Color.TRANSPARENT);
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

    public void setSelectedPosition(int position) {
        selectedPosition = position;
        notifyDataSetChanged();
    }

    public class FundViewHolder extends RecyclerView.ViewHolder {
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
                        Wallet clickedWallet = (Wallet) itemView.getTag();
                        listener.onFundClick(clickedWallet);

                        // Update selected position and notify adapter
                        notifyItemChanged(selectedPosition);
                        selectedPosition = position;
                        notifyItemChanged(selectedPosition);
                    }
                }
            });
        }
    }
}

