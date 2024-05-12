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
import java.util.Random;

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
        for (int i = 0; i < 10; i++) {
            transactionExps.add(new TransactionExp(
                    "id" + i + 1,
                    "user" + i + 1,
                    "category" + i + 1,
                    "This is a note " + i + 1,
                    new BigDecimal("" + new Random().nextInt(1000)),
                    "VNĐ",
                    "partner" + i +1,
                    "wallet" + i + 1,
                    new Timestamp(System.currentTimeMillis()),
                    "image" + i + 1
            ));
        }

        return transactionExps;
    }

}