package com.example.expensetracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.TransactionAdapter;
import com.example.expensetracker.databinding.FragmentTransactionBinding;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.view.MainActivity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class TransactionFragment extends Fragment {
    private RecyclerView rvTransaction;
    FragmentTransactionBinding binding;
    private MainActivity mainActivity;

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTransactionBinding.inflate(inflater);
        mainActivity = (MainActivity)getActivity();

        ArrayList<TransactionExp> transactionExps = new ArrayList<>();
        transactionExps.add(new TransactionExp(1, 1, 1, "oke", new BigDecimal(122), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(1, 1, 1, "oke", new BigDecimal(122), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(1, 1, 1, "oke", new BigDecimal(122), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(1, 1, 1, "oke", new BigDecimal(122), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(1, 1, 1, "oke", new BigDecimal(122), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(1, 1, 1, "oke", new BigDecimal(122), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(1, 1, 1, "oke", new BigDecimal(122), 1, "", 2, new Timestamp(new Date().getTime())));

        TransactionAdapter transactionAdapter = new TransactionAdapter(this.getContext(), transactionExps, new TransactionAdapter.IClickItemListener() {
            @Override
            public void onClickItem(TransactionExp transactionExp) {
                mainActivity.goToTransactionDetail(transactionExp);
            }
        });

        binding.transactionList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.transactionList.setAdapter(transactionAdapter);

        return binding.getRoot();
    }
}