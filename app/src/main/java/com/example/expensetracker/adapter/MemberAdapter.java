package com.example.expensetracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.UserWallet;
import com.example.expensetracker.model.Wallet;


import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder>{
    private List<AppUser> appUserList;

    public MemberAdapter(List<AppUser> appUserList) {
        this.appUserList = appUserList;
    }

    @NonNull
    @Override
    public MemberAdapter.MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item, parent, false);
        return new MemberAdapter.MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.MemberViewHolder holder, int position) {
        AppUser appUser = appUserList.get(position);
        if (appUser != null) {
            holder.memberName.setText(appUser.getUserName());
        }
    }

    @Override
    public int getItemCount() {
        return appUserList != null ? appUserList.size() : 0;
    }

    public void updateMemberWallet(List<AppUser> userList) {
        if ( appUserList!= null) {
            appUserList.clear();
            appUserList.addAll(userList);
        } else {
            appUserList = userList;
        }
        notifyDataSetChanged();
    }

    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        private final TextView memberName;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.member_name);
        }
    }
}
