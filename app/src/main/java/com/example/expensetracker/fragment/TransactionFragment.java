package com.example.expensetracker.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.TransactionAdapter;
import com.example.expensetracker.api.AppUser.AppUserApi;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.databinding.FragmentTransactionBinding;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.view.MainActivity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

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
                .baseUrl("https://expensetrackerbe.onrender.com/api/v1/")
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
                Toast.makeText(getActivity(), "Call API Error", Toast.LENGTH_SHORT).show();
            }

        });

        return transactionExps;
    }


}