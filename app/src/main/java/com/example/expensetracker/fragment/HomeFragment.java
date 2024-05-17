package com.example.expensetracker.fragment;

import android.content.Context;
import android.os.Bundle;
import android.content.SharedPreferences;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.expensetracker.R;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.adapter.WalletAdapter;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.bottom_sheet.ReportsFragment;
import com.example.expensetracker.bottom_sheet.TransactionDetailsFragment;
import com.example.expensetracker.bottom_sheet.WalletFragment;
import com.example.expensetracker.bottom_sheet.WalletUpdateListener;
import com.example.expensetracker.enums.Type;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.view.MainActivity;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends Fragment implements TransactionAdapter.OnItemClickListener, WalletUpdateListener {
    private WalletAdapter walletAdapter;
    private TransactionAdapter transactionAdapter;
    private List<TransactionExp> transactionList;
    private List<Wallet> walletList; // for WalletFragment bottom sheet
    private List<Wallet> subWallets; // for HomeFragment
    AnyChartView chartView;
    private TextView totalBalance;
    private AppUser user;
    private TextView income;
    private TextView outcome;
    private TextView showWallet;
    private TextView showTransaction;
    private TextView showReport;
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
        user = new Gson().fromJson(userJson, AppUser.class);

        // Set total balance
        totalBalance = view.findViewById(R.id.total_balance);

        // Chart view initialize
        chartView = view.findViewById(R.id.analysis_view);
        setUpChartView();

        // Show report
        showReport = view.findViewById(R.id.show_report);
        showReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReport(walletList);
            }
        });

        // Set user name
        userName = view.findViewById(R.id.user_name);
        userName.setText(user.getUserName());

        // Initialize wallets
        MainActivity mainActivity = (MainActivity)getActivity();
        LinearLayoutManager walletLayoutManager = new LinearLayoutManager(mainActivity);
        RecyclerView rvWallet = view.findViewById(R.id.wallet_list);
        rvWallet.setLayoutManager(walletLayoutManager);
        walletAdapter = new WalletAdapter(subWallets);
        getWalletList();
        rvWallet.setAdapter(walletAdapter);

        showWallet = view.findViewById(R.id.show_wallet);
        showWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllWallet(walletList);
            }
        });

        // Initialize recent transactions
        LinearLayoutManager transactionLayoutManager = new LinearLayoutManager(mainActivity);
        RecyclerView rvTransaction = view.findViewById(R.id.transaction_list_recent);
        rvTransaction.setLayoutManager(transactionLayoutManager);
        transactionAdapter = new TransactionAdapter(transactionList, this);
        getTransactionList();
        rvTransaction.setAdapter(transactionAdapter);


        showTransaction = view.findViewById(R.id.show_transaction);
        showTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.navigateToTransactions();
                }
            }
        });
        return view;
    }

    private void updateTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (Wallet wallet : walletList) {
            total = total.add(wallet.getAmount());
        }
        totalBalance.setText(Helper.formatCurrency(total));
    }

    private void showAllWallet(List<Wallet> walletList) {
        WalletFragment walletFragment = WalletFragment.newInstance(walletList);
        walletFragment.setWalletUpdateListener(this);
        walletFragment.show(getActivity().getSupportFragmentManager(), walletFragment.getTag());
    }

    private void showReport(List<Wallet> walletList) {
        ReportsFragment reportsFragment = ReportsFragment.newInstance(walletList);
        reportsFragment.show(getActivity().getSupportFragmentManager(), reportsFragment.getTag());
    }

    private void getTransactionList() {
        AppUserRepository repository = AppUserRepository.getInstance();
        repository.getTransaction(user.getId(), new ApiCallBack<List<TransactionExp>>() {
            @Override
            public void onSuccess(List<TransactionExp> transactions) {
                transactionList = transactions;
                if (transactions.size() > 3) {
                    transactions = transactions.subList(0, 3);
                }
                transactionAdapter.updateTransaction(transactions);
                transactionAdapter.notifyDataSetChanged();

                // Stats
                BigDecimal incomeVal = new BigDecimal(0);
                BigDecimal outcomeVal = new BigDecimal(0);
                List<String> incomeCategories = Arrays.asList(Type.KHOAN_THU.getDisplayName(), Type.THU_NO.getDisplayName(), Type.DI_VAY.getDisplayName());
                for (int i = 0; i < transactionList.size(); i++) {
                    if (incomeCategories.contains(transactionList.get(i).getCategory().getType())) {
                        incomeVal = incomeVal.add(transactionList.get(i).getSpend());
                    } else {
                        outcomeVal = outcomeVal.add(transactionList.get(i).getSpend());
                    }
                }
                income = getView().findViewById(R.id.stats_income_val);
                outcome = getView().findViewById(R.id.stats_outcome_val);
                income.setText(Helper.formatCurrency(incomeVal));
                outcome.setText(Helper.formatCurrency(outcomeVal));
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error here, display error message or retry logic
            }
        });
    }
    private void getWalletList() {
        AppUserRepository repository = AppUserRepository.getInstance();
        repository.getWallet(user.getId(), new ApiCallBack<List<Wallet>>() {
            @Override
            public void onSuccess(List<Wallet> wallets) {
                walletList = wallets;
                String currency = wallets.get(0).getCurrency();
                totalBalance.setText(String.format("%s %s", Helper.formatCurrency(getTotalBalance(wallets)), currency));

                if (wallets.size() > 3) {
                    wallets = wallets.subList(0, 3);
                }

                walletAdapter.updateWallet(wallets);
                walletAdapter.notifyDataSetChanged();

            }

            @Override
            public void onError(String message) {

            }
        });
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

    @Override
    public void onItemClick(TransactionExp transactionExp) {
        TransactionDetailsFragment transactionDetailsFragment = TransactionDetailsFragment.newInstance(transactionExp);
        transactionDetailsFragment.show(getActivity().getSupportFragmentManager(), transactionDetailsFragment.getTag());
    }

    @Override
    public void onWalletAdded(Wallet wallet) {
        walletList.add(wallet);
        walletAdapter.notifyItemInserted(walletList.size() - 1);
        setData();
    }

    @Override
    public void onWalletUpdated(Wallet wallet) {
        for (int i = 0; i < walletList.size(); i++) {
            if (walletList.get(i).getId().equals(wallet.getId())) {
                walletList.set(i, wallet);
                walletAdapter.notifyItemChanged(i);
                setData();
                break;
            }
        }
    }

    @Override
    public void onWalletDeleted(String walletId) {
        for (int i = 0; i < walletList.size(); i++) {
            if (walletList.get(i).getId().equals(walletId)) {
                walletList.remove(i);
                walletAdapter.notifyItemRemoved(i);
                setData();
                break;
            }
        }
    }

    private void setData() {
        BigDecimal result = new BigDecimal(0);

        for (int i = 0; i < walletList.size(); i++) {
            result = result.add(walletList.get(i).getAmount());
        }

        String currency = walletList.get(0).getCurrency();
        totalBalance.setText(String.format("%s %s", Helper.formatCurrency(result), currency));
    }
}