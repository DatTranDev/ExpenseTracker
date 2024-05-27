package com.example.expensetracker.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.adapter.WalletAdapter;
import com.example.expensetracker.bottom_sheet.ReportsFragment;
import com.example.expensetracker.bottom_sheet.TransactionDetailsFragment;
import com.example.expensetracker.bottom_sheet.WalletFragment;
import com.example.expensetracker.bottom_sheet.WalletUpdateListener;
import com.example.expensetracker.enums.Type;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.viewmodel.ChartViewModel;
import com.example.expensetracker.viewmodel.TransactionViewModel;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.material.tabs.TabLayout;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements TransactionAdapter.OnItemClickListener, WalletUpdateListener {
    private WalletAdapter walletAdapter;
    private TransactionAdapter transactionAdapter;
    private List<TransactionExp> transactionList;
    private List<Wallet> walletList;
    private List<Wallet> subWallets;
    private TabLayout filterTabLayout;
    private BarChart chartView;
    private TextView totalBalance;
    private AppUser user;
    private TextView income;
    private TextView outcome;
    private TextView showWallet;
    private TextView showTransaction;
    private TextView emptyWallet;
    private TextView showReport;
    private TextView userName;
    private TextView message;
    private WalletViewModel walletViewModel;
    private ChartViewModel chartViewModel;
    private TransactionViewModel transactionViewModel;
    public HomeFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
        chartViewModel = new ViewModelProvider(requireActivity()).get(ChartViewModel.class);
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        user = SharedPreferencesManager.getInstance(getContext()).getObject("user", AppUser.class);

        initView(view);

        userName.setText(user.getUserName());
        setupTabLayout();
        setupRecyclerViews(view);
        setupClickListeners();

        walletViewModel.loadWallets(user.getId());
        observeWalletViewModel();

        transactionViewModel.loadTransactions(user.getId());
        observeTransactionViewModel();

        chartViewModel.getWeeklyOutcomes().observe(getViewLifecycleOwner(), new Observer<BigDecimal[]>() {
            @Override
            public void onChanged(BigDecimal[] outcomes) {
                String[] labels = {"Tuần trước", "Tuần này"};
                updateChartView(outcomes[0], outcomes[1], labels);
            }
        });

        chartViewModel.getMonthlyOutcomes().observe(getViewLifecycleOwner(), new Observer<BigDecimal[]>() {
            @Override
            public void onChanged(BigDecimal[] outcomes) {
                String[] labels = {"Tháng trước", "Tháng này"};
                updateChartView(outcomes[0], outcomes[1], labels);
            }
        });

        return view;


    }

    private void observeTransactionViewModel() {
        transactionViewModel.getTransactionsLiveData().observe(getViewLifecycleOwner(), new Observer<List<TransactionExp>>() {
            @Override
            public void onChanged(List<TransactionExp> transactions) {
                transactionList = sortTransactionsByDate(transactions);
                if (transactions.size() > 3) {
                    transactions = transactions.subList(0, 3);
                }
                transactionAdapter.updateTransaction(transactions);
                transactionAdapter.notifyDataSetChanged();

                chartViewModel.updateWeeklyOutcomes(transactionList);

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
        });

        transactionViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void observeWalletViewModel() {
        walletViewModel.getWalletsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Wallet>>() {
            @Override
            public void onChanged(List<Wallet> wallets) {
                walletList = wallets;
                if (walletList.isEmpty()) {
                    emptyWallet.setVisibility(View.VISIBLE);
                } else {
                    emptyWallet.setVisibility(View.GONE);
                }

                if (wallets.size() > 3) {
                    subWallets = wallets.subList(0, 3);
                } else {
                    subWallets = wallets;
                }

                walletAdapter.updateWallet(subWallets);
                walletAdapter.notifyDataSetChanged();
                updateTotalAmount(walletList);
            }
        });

        walletViewModel.getErrorMessageLiveData().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        emptyWallet = view.findViewById(R.id.wallet_empty);
        totalBalance = view.findViewById(R.id.total_balance);
        chartView = view.findViewById(R.id.analysis_view);
        filterTabLayout = view.findViewById(R.id.filter);
        userName = view.findViewById(R.id.user_name);
        showReport = view.findViewById(R.id.show_report);
        showWallet = view.findViewById(R.id.show_wallet);
        showTransaction = view.findViewById(R.id.show_transaction);
        message = view.findViewById(R.id.message_empty);
    }

    private List<TransactionExp> sortTransactionsByDate(List<TransactionExp> transactions) {
        Collections.sort(transactions, (t1, t2) -> t2.getCreatedAt().compareTo(t1.getCreatedAt()));
        return transactions;
    }


    private void setupTabLayout() {
        filterTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    updateChartForWeek();
                } else if (position == 1) {
                    updateChartForMonth();
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupRecyclerViews(View view) {
        MainActivity mainActivity = (MainActivity) getActivity();
        LinearLayoutManager walletLayoutManager = new LinearLayoutManager(mainActivity);
        RecyclerView rvWallet = view.findViewById(R.id.wallet_list);
        rvWallet.setLayoutManager(walletLayoutManager);
        walletAdapter = new WalletAdapter(subWallets);
        rvWallet.setAdapter(walletAdapter);

        LinearLayoutManager transactionLayoutManager = new LinearLayoutManager(mainActivity);
        RecyclerView rvTransaction = view.findViewById(R.id.transaction_list_recent);
        rvTransaction.setLayoutManager(transactionLayoutManager);
        transactionAdapter = new TransactionAdapter(getContext(), transactionList, this);
        rvTransaction.setAdapter(transactionAdapter);
    }

    private void setupClickListeners() {
        showReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReport(transactionList);
            }
        });

        showWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllWallet(walletList);
            }
        });

        showTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (mainActivity != null) {
                    mainActivity.navigateToTransactions();
                }
            }
        });
    }

    private void updateChartForWeek() {
        chartViewModel.updateWeeklyOutcomes(transactionList);
    }

    private void updateChartForMonth() {
        chartViewModel.updateMonthlyOutcomes(transactionList);
    }


    private void updateChartView(BigDecimal value1, BigDecimal value2, String[] labels) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        BarDataSet dataSet;

        if (value1.equals(BigDecimal.ZERO) && value2.equals(BigDecimal.ZERO)) {
            entries.add(new BarEntry(0f, 40f));
            entries.add(new BarEntry(1f, 40f));
            dataSet = new BarDataSet(entries, "No Data");
            dataSet.setColor(Color.parseColor("#CACAD8"));
            dataSet.setDrawValues(false);
            dataSet.setValueTextColor(Color.LTGRAY);
            message.setVisibility(View.VISIBLE);
        } else {
            entries.add(new BarEntry(0f, value1.floatValue()));
            entries.add(new BarEntry(1f, value2.floatValue()));
            dataSet = new BarDataSet(entries, "Expenses");
            dataSet.setColor(Color.parseColor("#F48484"));
            dataSet.setValueTextColor(Color.BLACK);
            message.setVisibility(View.GONE);
        }

        BarData barData = new BarData(dataSet);
        chartView.setData(barData);

        chartView.getDescription().setEnabled(false);
        chartView.getAxisLeft().setAxisMinimum(0);
        chartView.getAxisRight().setEnabled(false);

        chartView.getXAxis().setDrawGridLines(false);
        chartView.getXAxis().setDrawAxisLine(true);
        chartView.getAxisLeft().setDrawGridLines(false);
        chartView.getAxisLeft().setDrawAxisLine(false);
        chartView.getAxisRight().setDrawGridLines(false);
        chartView.getAxisRight().setDrawAxisLine(false);

        chartView.getAxisLeft().setDrawLabels(false);
        chartView.getAxisRight().setDrawLabels(false);

        XAxis xAxis = chartView.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setDrawAxisLine(true);
        xAxis.setAxisLineWidth(2f);
        xAxis.setTextSize(14f);

        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.montserrat);
        xAxis.setTypeface(typeface);

        dataSet.setHighlightEnabled(false);
        dataSet.setValueTextSize(16f);

        Legend legend = chartView.getLegend();
        legend.setEnabled(false);

        Description description = new Description();
        description.setText("");
        chartView.setDescription(description);

        barData.setBarWidth(0.3f);
        chartView.setExtraOffsets(40f, 40f, 40f, 40f);

        chartView.invalidate();
    }

    private void showAllWallet(List<Wallet> walletList) {
        WalletFragment walletFragment = WalletFragment.newInstance();
        walletFragment.show(getActivity().getSupportFragmentManager(), walletFragment.getTag());
    }

    private void showReport(List<TransactionExp> transactionExps) {
        ReportsFragment reportsFragment = ReportsFragment.newInstance(transactionExps);
        reportsFragment.setCancelable(false);
        reportsFragment.show(getActivity().getSupportFragmentManager(), reportsFragment.getTag());

    }

    private void updateTotalAmount(List<Wallet> wallets) {
        BigDecimal totalAmount = walletViewModel.getTotalBalance();
        totalBalance.setText(Helper.formatCurrency(totalAmount));
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

        totalBalance.setText(Helper.formatCurrency(result));

        if (walletList.isEmpty()) {
            emptyWallet.setVisibility(View.VISIBLE);
        } else {
            emptyWallet.setVisibility(View.GONE);
        }
    }
}