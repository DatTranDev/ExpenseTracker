package com.example.expensetracker.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.expensetracker.R;
import com.example.expensetracker.enums.Type;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.utils.SharedPreferencesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FundTransactionAdapter extends RecyclerView.Adapter<FundTransactionAdapter.TransactionViewHolder> {
    private List<TransactionExp> transactionExps;
    private static final String KEY_TRANSACTION = "transaction_info";
    private OnItemClickListener listener;
    private Context context;
    private AppUser user;

    public FundTransactionAdapter(Context context, List<TransactionExp> transactionExps, OnItemClickListener listener) {
        this.context = context;
        this.transactionExps = transactionExps;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_fund_transaction_item, parent, false);
        user = SharedPreferencesManager.getInstance(parent.getContext()).getObject("user", AppUser.class);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionExp transactionExp = transactionExps.get(position);
        if (transactionExp == null) {
            return;
        }

        List<String> income = Arrays.asList(Type.KHOAN_THU.getDisplayName(), Type.THU_NO.getDisplayName(), Type.DI_VAY.getDisplayName());
        if (income.contains(transactionExp.getCategory().getType())) {
            holder.transactionPrice.setText(String.format("+%s", Helper.formatCurrency(transactionExp.getSpend())));
            holder.transactionPrice.setTextColor(Color.parseColor("#00DDB0")); // accent_green
        } else {
            holder.transactionPrice.setText(String.format("-%s", Helper.formatCurrency(transactionExp.getSpend())));
            holder.transactionPrice.setTextColor(Color.parseColor("#F48484")); // light_red
        }

        holder.transactionTime.setText(Helper.formatDate(transactionExp.getCreatedAt()));
        holder.transactionName.setText(String.valueOf(transactionExp.getCategory().getName()));
        holder.transactionUsername.setText(transactionExp.getUser().getUserName());


        String iconName = transactionExp.getCategory().getIcon().getLinking();
        int iconId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());

        holder.transactionIcon.setImageResource(iconId);
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
        List<TransactionExp> temp = new ArrayList<>();

        for (TransactionExp transactionExp : transactions) {
            temp.add(transactionExp);
        }

        if (transactionExps != null) {
            transactionExps.clear();
            transactionExps.addAll(temp);
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
        private TextView transactionUsername;
        private TextView transactionTime;
        private ImageView transactionIcon;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            transactionPrice = itemView.findViewById(R.id.transaction_price);
            transactionName = itemView.findViewById(R.id.transaction_name);
            transactionUsername = itemView.findViewById(R.id.transaction_username);
            transactionTime = itemView.findViewById(R.id.transaction_time);
            transactionIcon = itemView.findViewById(R.id.transaction_icon);
        }
    }
}
