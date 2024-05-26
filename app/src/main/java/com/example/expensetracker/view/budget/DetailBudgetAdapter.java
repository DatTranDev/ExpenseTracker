package com.example.expensetracker.view.budget;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;

import java.util.List;

public class DetailBudgetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_DATE = 0;
    private static final int VIEW_TYPE_TRANSACTION = 1;
    private List<Object> items;

    public DetailBudgetAdapter(List<Object> items) {
        this.items = items;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof DateItem) {
            return VIEW_TYPE_DATE;
        } else {
            return VIEW_TYPE_TRANSACTION;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_DATE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_time_detail_budget, parent, false);
            return new DateViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transaction_detail_budget, parent, false);
            return new TransactionViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_DATE) {
            DateViewHolder dateViewHolder = (DateViewHolder) holder;
            dateViewHolder.bind((DateItem) items.get(position));
        } else {
            TransactionViewHolder transactionViewHolder = (TransactionViewHolder) holder;
            transactionViewHolder.bind((Transaction) items.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static class DateViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView;

        public DateViewHolder(View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        public void bind(DateItem dateItem) {
            dateTextView.setText(dateItem.date);
        }
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView descriptionTextView;
        TextView amountTextView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
        }

        public void bind(Transaction transaction) {
            descriptionTextView.setText(transaction.description);
            amountTextView.setText(String.valueOf(transaction.amount));
        }
    }

}
