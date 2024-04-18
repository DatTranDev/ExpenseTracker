package com.example.expensetracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.databinding.TransactionItemBinding;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.Helper;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    Context context;
    ArrayList<TransactionExp> transactionExps;
    public TransactionAdapter(Context context, ArrayList<TransactionExp> transactionExps) {
        this.context = context;
        this.transactionExps = transactionExps;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionViewHolder(LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionExp transactionExp = transactionExps.get(position);
        holder.binding.transactionPrice.setText(String.valueOf(transactionExp.getSpend()));
        holder.binding.transactionTime.setText(Helper.formatDate(transactionExp.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return transactionExps.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        TransactionItemBinding binding;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = TransactionItemBinding.bind(itemView);

        }
    }

}
