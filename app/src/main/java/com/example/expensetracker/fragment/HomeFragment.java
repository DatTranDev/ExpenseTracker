package com.example.expensetracker.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.content.SharedPreferences;

import androidx.core.content.res.ResourcesCompat;
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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment implements TransactionAdapter.OnItemClickListener, WalletUpdateListener {
    private WalletAdapter walletAdapter;
    private TransactionAdapter transactionAdapter;
    private List<TransactionExp> transactionList;
    private List<Wallet> walletList; // for WalletFragment bottom sheet
    private List<Wallet> subWallets; // for HomeFragment
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

        emptyWallet = view.findViewById(R.id.wallet_empty);

        // Load initial data
        getWalletList();
        getTransactionList();

        // Set total balance
        totalBalance = view.findViewById(R.id.total_balance);

        // Chart view initialize
        chartView = view.findViewById(R.id.analysis_view);
        filterTabLayout = view.findViewById(R.id.filter);
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
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Set user name
        userName = view.findViewById(R.id.user_name);
        userName.setText(user.getUserName());

        // Show report
        showReport = view.findViewById(R.id.show_report);
        showReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReport(transactionList);
            }
        });

        // Initialize wallets
        MainActivity mainActivity = (MainActivity)getActivity();
        LinearLayoutManager walletLayoutManager = new LinearLayoutManager(mainActivity);
        RecyclerView rvWallet = view.findViewById(R.id.wallet_list);
        rvWallet.setLayoutManager(walletLayoutManager);
        walletAdapter = new WalletAdapter(subWallets);
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

    private void updateChartForMonth() {
        Calendar cal = Calendar.getInstance();


        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfCurrentMonth = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfNextMonth = cal.getTime();


        cal.add(Calendar.MONTH, -2);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startOfPreviousMonth = cal.getTime();
        cal.add(Calendar.MONTH, 1);
        Date startOfCurrentMonthLastMonth = cal.getTime();

        BigDecimal lastMonthOutcome = getOutcomeForPeriod(startOfPreviousMonth, startOfCurrentMonthLastMonth);
        BigDecimal currentMonthOutcome = getOutcomeForPeriod(startOfCurrentMonth, startOfNextMonth);

        String[] labels = {"Tháng trước", "Tháng này"};
        updateChartView(lastMonthOutcome, currentMonthOutcome, labels);
    }


    private void updateChartView(BigDecimal value1, BigDecimal value2, String[] labels) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        if (value1.equals(BigDecimal.ZERO) && value2.equals(BigDecimal.ZERO)) {
            chartView.setNoDataText("No data found");
            chartView.invalidate();
            return;
        }
        entries.add(new BarEntry(0f, value1.floatValue()));
        entries.add(new BarEntry(1f, value2.floatValue()));

        BarDataSet dataSet = new BarDataSet(entries, "Expenses");
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

        dataSet.setColor(Color.parseColor("#F48484"));
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

    private BigDecimal getOutcomeForPeriod(Date start, Date end) {
        BigDecimal outcome = BigDecimal.ZERO;
        List<String> outcomeCategories = Arrays.asList(Type.KHOAN_CHI.getDisplayName(), Type.CHO_VAY.getDisplayName(), Type.TRA_NO.getDisplayName());
        for (TransactionExp transaction : transactionList) {
            Date transactionDate = transaction.getCreatedAt();
            if (!transactionDate.before(start) && transactionDate.before(end) &&
                    outcomeCategories.contains(transaction.getCategory().getType())) {
                outcome = outcome.add(transaction.getSpend());
            }
        }
        return outcome;
    }

    private void updateChartForWeek() {
        Date currentDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);

        cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
        Date startOfWeek = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date endOfWeek = cal.getTime();

        cal.add(Calendar.WEEK_OF_YEAR, -2);
        Date startOfLastWeek = cal.getTime();
        cal.add(Calendar.WEEK_OF_YEAR, 1);
        Date endOfLastWeek = cal.getTime();

        BigDecimal lastWeekOutcome = getOutcomeForPeriod(startOfLastWeek, endOfLastWeek);
        BigDecimal currentWeekOutcome = getOutcomeForPeriod(startOfWeek, endOfWeek);
        String[] labels = {"Tuần trước", "Tuần này"};
        updateChartView(lastWeekOutcome, currentWeekOutcome, labels);
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

    private void showReport(List<TransactionExp> transactionExps) {
        ReportsFragment reportsFragment = ReportsFragment.newInstance(transactionExps);
        reportsFragment.setCancelable(false);
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
                updateChartForWeek();

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
                totalBalance.setText(Helper.formatCurrency(getTotalBalance(wallets)));

                if (walletList.isEmpty()) {
                    emptyWallet.setVisibility(View.VISIBLE);
                } else {
                    emptyWallet.setVisibility(View.GONE);
                }

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

        totalBalance.setText(Helper.formatCurrency(result));

        if (walletList.isEmpty()) {
            emptyWallet.setVisibility(View.VISIBLE);
        } else {
            emptyWallet.setVisibility(View.GONE);
        }
    }
}