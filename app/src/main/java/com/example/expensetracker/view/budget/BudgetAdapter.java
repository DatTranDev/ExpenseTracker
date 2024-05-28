package com.example.expensetracker.view.budget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.view.addTransaction.CategoryAdapter;
import com.example.expensetracker.view.addTransaction.ChooseWallerAdapter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>{
    private List<BudgetItem> data;
    private OnItemClickListener mListener;

    public BudgetAdapter(List<BudgetItem> list) {
        data=list;

    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void updateBudgets(List<BudgetItem> newWallets) {
        data = newWallets;
        notifyDataSetChanged();

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.layout_budget, parent, false);
        return new BudgetViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        BudgetItem item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView amount,enable,time;
        ProgressBar progressBar;
        ImageView icon;


        public BudgetViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.nameCategory);
            amount= itemView.findViewById(R.id.money_budget);
            enable= itemView.findViewById((R.id.moneyEnabled));
            icon= itemView.findViewById(R.id.imageCategory);
            progressBar= itemView.findViewById(R.id.progress_budget);
            time=itemView.findViewById(R.id.timeBudget);

            //sự kiện click
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
        public void bind(BudgetItem item) {
           name.setText(item.nameCategory);
           amount.setText(item.Amount);
            // Định dạng Date thành chuỗi "dd/MM"
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");
            time.setText(formatter.format(item.startDate)+" - "+formatter.format(item.endDate));
            String formattedDate = formatter.format(item.startDate);
           if(item.Progress>100)
           {
               progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F35656")));
               progressBar.setProgress(100);
               enable.setText("Vượt quá " + Helper.formatMoney(new BigDecimal(0).subtract(item.Enabled)));
           }
           else {
               progressBar.setProgress(item.Progress);
               enable.setText("Còn lại " + Helper.formatMoney(item.Enabled));
               if(item.Progress < 60)
               {
                   progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#1AEB16"))); // Thay Color.RED bằng màu bạn muốn
               }
               if(item.Progress>=60&& item.Progress<=90)
               {
                   progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#FFC75C")));
               }
               if(item.Progress>90)
               {
                   progressBar.setProgressTintList(ColorStateList.valueOf(Color.parseColor("#F35656")));
               }

           }
           if(item.idIcon==0)
           {
               icon.setImageResource(R.drawable.error);
           }
           else
           {
               icon.setImageResource(item.idIcon);
           }
        }
    }
}
