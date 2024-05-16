package com.example.expensetracker.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.AppUser.AppUserApi;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.utils.Constant;
import com.example.expensetracker.view.MainActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;

public class TransactionFragment extends Fragment implements TransactionAdapter.OnItemClickListener {
    private TransactionAdapter transactionAdapter;
    private List<TransactionExp> transactions = new ArrayList<>();
    private View view;
    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transaction, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");
        AppUser user = new Gson().fromJson(userJson, AppUser.class);
        MainActivity mainActivity = (MainActivity)getActivity();

        RecyclerView rvTransaction = view.findViewById(R.id.transaction_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);

        rvTransaction.setLayoutManager(linearLayoutManager);

        transactionAdapter = new TransactionAdapter(transactions, this);
        getTransactionsForUser(user.getId());

        rvTransaction.setAdapter(transactionAdapter);

        return view;
    }

    private void getTransactionsForUser(String userId) {
        AppUserRepository repository = AppUserRepository.getInstance();
        repository.getTransaction(userId, new ApiCallBack<List<TransactionExp>>() {
            @Override
            public void onSuccess(List<TransactionExp> transactions) {
                transactionAdapter.updateTransaction(transactions);
                transactionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void onItemClick(TransactionExp transactionExp) {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.showTransactionDetails(transactionExp);
        }
    }
}