package com.example.expensetracker.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.expensetracker.R;
import com.example.expensetracker.fragment.TransactionDetailsFragment;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.Helper;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<TransactionExp> transactionExps;
    private static final String KEY_TRANSACTION = "transaction_info";
    private OnItemClickListener listener;

    public TransactionAdapter(List<TransactionExp> transactionExps, OnItemClickListener listener) {
        this.transactionExps = transactionExps;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionExp transactionExp = transactionExps.get(position);
        if (transactionExp == null) {
            return;
        }

        holder.transactionPrice.setText(String.valueOf(transactionExp.getSpend()));
        holder.transactionTime.setText(Helper.formatDate(transactionExp.getCreatedAt()));
        holder.transactionName.setText(String.valueOf(transactionExp.getCategory().getName()));
        holder.transactionType.setText(String.valueOf(transactionExp.getNote()));
        holder.itemView.setOnClickListener(v -> listener.onItemClick(transactionExp));
    }
    @Override
    public int getItemCount() {
        if (transactionExps != null) {
            return transactionExps.size();
        }
        return 0;
    }

    public void updateTransaction(List<TransactionExp> transactions) {
        if (transactionExps != null) {
            transactionExps.clear();
            transactionExps.addAll(transactions);
            notifyDataSetChanged();
        } else {
            transactionExps = transactions;
            notifyDataSetChanged();
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TransactionExp transactionExp);
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        private TextView transactionPrice;
        private TextView transactionName;
        private TextView transactionType;
        private TextView transactionTime;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionPrice = itemView.findViewById(R.id.transaction_price);
            transactionName = itemView.findViewById(R.id.transaction_name);
            transactionType = itemView.findViewById(R.id.transaction_type);
            transactionTime = itemView.findViewById(R.id.transaction_time);
        }
    }
}
