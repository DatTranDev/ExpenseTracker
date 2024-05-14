package com.example.expensetracker.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.expensetracker.fragment.AccountFragment;
import com.example.expensetracker.fragment.BudgetFragment;
import com.example.expensetracker.fragment.FundFragment;
import com.example.expensetracker.fragment.HomeFragment;
import com.example.expensetracker.fragment.TransactionDetailsFragment;
import com.example.expensetracker.fragment.TransactionFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private Fragment[] fragments;
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        fragments = new Fragment[] {new HomeFragment(),
                                    new TransactionFragment(),
                                    new BudgetFragment(),
                                    new FundFragment(),
                                    new AccountFragment(),
                                    new Fragment()};
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments[position];
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
    public void setTransactionDetailsFragment(TransactionDetailsFragment transactionDetailsFragment) {
        fragments[5] = transactionDetailsFragment;
        notifyDataSetChanged();
    }
}
