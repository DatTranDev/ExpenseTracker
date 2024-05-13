package com.example.expensetracker.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.core.cartesian.series.Line;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.expensetracker.R;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.adapter.WalletAdapter;
import com.example.expensetracker.api.AppUser.AppUserApi;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Constant;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.view.MainActivity;
import com.google.android.material.transition.Hold;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {
    private WalletAdapter walletAdapter;
    AnyChartView chartView;
    private TextView totalBalance;
    private TextView userName;
    public HomeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");
        AppUser user = new Gson().fromJson(userJson, AppUser.class);

        // Set total balance
        totalBalance = view.findViewById(R.id.total_balance);
        List<Wallet> walletList = getWalletList();

        // Chart view initialize
        chartView = view.findViewById(R.id.analysis_view);
        setUpChartView();

        // Set user name
        userName = view.findViewById(R.id.user_name);
        userName.setText(user.getUserName());

        // Wallet initialize
        MainActivity mainActivity = (MainActivity)getActivity();

        RecyclerView rvWallet = view.findViewById(R.id.wallet_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);

        rvWallet.setLayoutManager(linearLayoutManager);

        walletAdapter = new WalletAdapter(walletList);
        rvWallet.setAdapter(walletAdapter);
        return view;
    }
    private void getTransactionList() {
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
                    BigDecimal income = new BigDecimal(0);
                    BigDecimal outcome = new BigDecimal(0);
                    for (int i = 0; i < transactionExps.size(); i++) {

                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<TransactionExp>>> call, Throwable t) {
            }

        });
    }
    private List<Wallet> getWalletList() {
        List<Wallet> walletList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AppUserApi appUserApi = retrofit.create(AppUserApi.class);

        String userId = "6615a4b40d01b7dd489839bc";
        Call<DataResponse<List<Wallet>>> call = appUserApi.getWallet(userId);
        call.enqueue(new Callback<DataResponse<List<Wallet>>>() {
            @Override
            public void onResponse(Call<DataResponse<List<Wallet>>> call, Response<DataResponse<List<Wallet>>> response) {
                if (response.isSuccessful()) {
                    walletList.addAll(response.body().getData());
                    String currency = walletList.get(0).getCurrency();
                    walletAdapter.notifyDataSetChanged();
                    totalBalance.setText(String.format("%s %s", Helper.formatCurrency(getTotalBalance(walletList)), currency));
                } else {
                }
            }

            @Override
            public void onFailure(Call<DataResponse<List<Wallet>>> call, Throwable t) {
            }

        });

        return walletList;
    }
    private void setUpChartView() {
        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Tháng trước", 80540));
        data.add(new ValueDataEntry("Tháng này", 94190));

        Column column = cartesian.column(data);

        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("${%Value}{groupsSeparator: }");

        cartesian.animation(true);

        cartesian.yScale().minimum(0d);
        cartesian.yAxis(0).title().enabled(false);
        cartesian.yAxis(0).labels().format("${%Value}{groupsSeparator: }");

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesian.interactivity().hoverMode(HoverMode.BY_X);

        chartView.setChart(cartesian);
    }
    private BigDecimal getTotalBalance(List<Wallet> walletList) {
        BigDecimal result = new BigDecimal(0);

        for (int i = 0; i < walletList.size(); i++) {
            result = result.add(walletList.get(i).getAmount());
        }

        return result;
    }
}