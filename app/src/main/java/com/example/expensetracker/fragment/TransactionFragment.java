package com.example.expensetracker.fragment;

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
import com.example.expensetracker.api.AppUser.AppUserApi;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.Constant;
import com.example.expensetracker.view.MainActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;

public class TransactionFragment extends Fragment {
    private TransactionAdapter transactionAdapter;
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

        transactionAdapter = new TransactionAdapter(getTransactionList());
        rvTransaction.setAdapter(transactionAdapter);

        return view;
    }

    private List<TransactionExp> getTransactionList() {
        List<TransactionExp> transactionExps = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AppUserApi appUserApi = retrofit.create(AppUserApi.class);

        String userId = "6615a4b40d01b7dd489839bc";
        Call<DataResponse<List<TransactionExp>>> call = appUserApi.getTransaction(userId);
        call.enqueue(new Callback<DataResponse<List<TransactionExp>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<TransactionExp>>> call, Response<DataResponse<List<TransactionExp>>> response) {
                if (response.isSuccessful()) {
                    transactionExps.addAll(response.body().getData());
                    transactionAdapter.notifyDataSetChanged();
                } else {
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<TransactionExp>>> call, Throwable t) {
            }

        });

        return transactionExps;
    }
}