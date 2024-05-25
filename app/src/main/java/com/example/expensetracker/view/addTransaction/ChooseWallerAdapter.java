package com.example.expensetracker.view.addTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;

import java.util.ArrayList;
import java.util.List;

public class ChooseWallerAdapter extends RecyclerView.Adapter<ChooseWallerAdapter.WalletViewHolder> {
    private List<Wallet> data;
    private OnItemClickListener mListener;

    public ChooseWallerAdapter(List<Wallet> list) {
        data=list;

    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void updateWallets(List<Wallet> newWallets) {
        data = newWallets;
        notifyDataSetChanged();

    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public WalletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.choosewallet_item, parent, false);
        return new WalletViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull WalletViewHolder holder, int position) {
        Wallet item = data.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public static class WalletViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView amount;

        public WalletViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.walletname);
            amount= itemView.findViewById(R.id.walletamount);

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
        public void bind(Wallet item) {
            name.setText(item.getName());
            Log.d("lỗi",item.getAmount().toString());
            amount.setText(Helper.formatCurrency(item.getAmount()));
        }
    }

    }
