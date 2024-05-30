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
import com.example.expensetracker.utils.SharedPreferencesManager;


import java.util.List;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MemberViewHolder>{
    private List<AppUser> appUserList;

    private AppUser user;

    public MemberAdapter(List<AppUser> appUserList) {
        this.appUserList = appUserList;
    }

    @NonNull
    @Override
    public MemberAdapter.MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item, parent, false);
        user = SharedPreferencesManager.getInstance(null).getObject("user", AppUser.class);
        return new MemberAdapter.MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.MemberViewHolder holder, int position) {
        AppUser appUser = appUserList.get(position);
        if (appUser != null) {
            holder.memberName.setText(appUser.getUserName());
        }
    }

    public int getItemCount() {
        if (appUserList != null) {
            return appUserList.size();
        }
        return 0;
    }

    public void updateMemberWallet(List<AppUser> members) {
        if (appUserList != null) {
            appUserList.clear();
            appUserList.addAll(members);
            notifyDataSetChanged();
        } else {
            appUserList = members;
            notifyDataSetChanged();
        }
    }


    public static class MemberViewHolder extends RecyclerView.ViewHolder {
        private final TextView memberName;
        public MemberViewHolder(@NonNull View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.member_name);
        }
    }
}
