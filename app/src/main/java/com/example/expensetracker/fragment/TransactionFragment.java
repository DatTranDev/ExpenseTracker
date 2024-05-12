package com.example.expensetracker.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.TransactionAdapter;
import com.example.expensetracker.databinding.FragmentTransactionBinding;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.view.MainActivity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionFragment extends Fragment {
    //    FragmentTransactionBinding binding;
    private View view;
    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaction, container, false);
        MainActivity mainActivity = (MainActivity)getActivity();

        RecyclerView rvTransaction = view.findViewById(R.id.transaction_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);

        rvTransaction.setLayoutManager(linearLayoutManager);

        TransactionAdapter transactionAdapter = new TransactionAdapter(getTransactionList());
        rvTransaction.setAdapter(transactionAdapter);

        return view;
    }

    private List<TransactionExp> getTransactionList() {
        List<TransactionExp> transactionExps = new ArrayList<>();
        transactionExps.add(new TransactionExp(1, 1, 1, "oke 1", new BigDecimal(122), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(2, 1, 2, "oke 2", new BigDecimal(1142), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(3, 1, 3, "oke 3", new BigDecimal(122551), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(4, 1, 4, "oke 4", new BigDecimal(12522), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(5, 1, 5, "oke 5", new BigDecimal(125162), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(6, 1, 6, "oke 6", new BigDecimal(1152), 1, "", 2, new Timestamp(new Date().getTime())));
        transactionExps.add(new TransactionExp(7, 1, 7, "oke 7", new BigDecimal(12212), 1, "", 2, new Timestamp(new Date().getTime())));

        return transactionExps;
    }

}