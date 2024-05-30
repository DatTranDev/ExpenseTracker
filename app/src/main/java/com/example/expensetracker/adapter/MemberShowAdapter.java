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

import java.util.List;

public class MemberShowAdapter extends RecyclerView.Adapter<MemberShowAdapter.MemberShowViewHolder>{
    private List<AppUser> appUserList;
    private final OnMemberModifyClickListener memberModifyListener;

    public MemberShowAdapter(List<AppUser> appUserList, OnMemberModifyClickListener listener) {
        this.appUserList = appUserList;
        this.memberModifyListener = listener;
    }

    public interface OnMemberModifyClickListener {
        void onMemberModifyClick(AppUser appUser);
    }

    @NonNull
    @Override
    public MemberShowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item_show, parent, false);
        return new MemberShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberShowViewHolder holder, int position) {
        AppUser appUser = appUserList.get(position);
        if (appUser == null) {
            return;
        }
        holder.memberName.setText(appUser.getUserName());
        holder.modifyMember.setOnClickListener(v -> memberModifyListener.onMemberModifyClick(appUser));

        holder.modifyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberModifyListener.onMemberModifyClick(appUser);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (appUserList != null) {
            return appUserList.size();
        }
        return 0;
    }

    public static class MemberShowViewHolder extends RecyclerView.ViewHolder {
        private final TextView memberName;
        private final TextView modifyMember;

        public MemberShowViewHolder(@NonNull View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.member_name);
            modifyMember = itemView.findViewById(R.id.modify_member);
        }
    }

    public void updateAppUsers(List<AppUser> appUsers) {
        this.appUserList = appUsers;
        notifyDataSetChanged();
    }
}


