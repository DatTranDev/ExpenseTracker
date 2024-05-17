package com.example.expensetracker.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;

import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.AppUser.AppUserApi;
import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.bottom_sheet.TransactionDetailsFragment;
import com.example.expensetracker.enums.Period;
import com.example.expensetracker.enums.Type;
import com.example.expensetracker.model.AppUser;

import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.utils.Constant;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.view.MainActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;

public class TransactionFragment extends Fragment implements TransactionAdapter.OnItemClickListener {
    private TransactionAdapter transactionAdapter;
    private List<TransactionExp> transactions = new ArrayList<>();
    private List<TransactionExp> allTransactions = new ArrayList<>();
    private TabLayout tabLayoutFilter;
    private ImageButton previousTime;
    private TextView openingBalance, endingBalance, difference;
    private LinearLayout transactionEmpty;
    private ImageButton nextTime;
    private TextView time;
    private View view;
    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_transaction, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");
        AppUser user = new Gson().fromJson(userJson, AppUser.class);

        initView(view);
        nextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustTimePeriod(1);
            }
        });

        previousTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustTimePeriod(-1);
            }
        });

        tabLayoutFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateDateRange();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                updateDateRange();
            }
        });

        MainActivity mainActivity = (MainActivity)getActivity();
        RecyclerView rvTransaction = view.findViewById(R.id.transaction_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        rvTransaction.setLayoutManager(linearLayoutManager);
        transactionAdapter = new TransactionAdapter(transactions, this);
        getTransactionsForUser(user.getId());
        rvTransaction.setAdapter(transactionAdapter);

        return view;
    }

    private void updateDateRange() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        try {
            Date currentDate = dateFormat.parse(Helper.formatDate(new Date()));
            Calendar calendarStart = Calendar.getInstance();
            Calendar calendarEnd = Calendar.getInstance();
            calendarStart.setTime(currentDate);
            calendarEnd.setTime(currentDate);
            switch (getFilter()) {
                case "Tuần":
                    calendarStart.set(Calendar.DAY_OF_WEEK, calendarStart.getFirstDayOfWeek());
                    calendarEnd.set(Calendar.DAY_OF_WEEK, calendarEnd.getFirstDayOfWeek());
                    calendarEnd.add(Calendar.DAY_OF_WEEK, 6);
                    break;
                case "Tháng":
                    calendarStart.set(Calendar.DAY_OF_MONTH, 1);
                    calendarEnd.set(Calendar.DAY_OF_MONTH, calendarEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
                    break;
                case "Năm":
                    calendarStart.set(Calendar.DAY_OF_YEAR, 1);
                    calendarEnd.set(Calendar.DAY_OF_YEAR, calendarEnd.getActualMaximum(Calendar.DAY_OF_YEAR));
                    break;
            }
            filterTransactions(calendarStart.getTime(), calendarEnd.getTime());
            time.setText(Helper.formatDate(calendarStart.getTime()) + " - " + Helper.formatDate(calendarEnd.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void adjustTimePeriod(int increment) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        String[] dateRange = time.getText().toString().split(" - ");
        try {
            Date startDate = dateFormat.parse(dateRange[0]);
            Date endDate = dateFormat.parse(dateRange[1]);

            Calendar calendarStart = Calendar.getInstance();
            Calendar calendarEnd = Calendar.getInstance();
            calendarStart.setTime(startDate);
            calendarEnd.setTime(endDate);

            switch (getFilter()) {
                case "Tuần":
                    calendarStart.add(Calendar.WEEK_OF_YEAR, increment);
                    calendarEnd.add(Calendar.WEEK_OF_YEAR, increment);
                    break;
                case "Tháng":
                    calendarStart.add(Calendar.MONTH, increment);
                    calendarEnd.add(Calendar.MONTH, increment);
                    break;
                case "Năm":
                    calendarStart.add(Calendar.YEAR, increment);
                    calendarEnd.add(Calendar.YEAR, increment);
                    break;
            }

            String newStartDate = Helper.formatDate(calendarStart.getTime());
            String newEndDate = Helper.formatDate(calendarEnd.getTime());
            time.setText(newStartDate + " - " + newEndDate);

            filterTransactions(calendarStart.getTime(), calendarEnd.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void filterTransactions(Date startDate, Date endDate) {
        List<TransactionExp> filteredTransactions = new ArrayList<>();
        BigDecimal openingBalanceAmount = new BigDecimal(0);
        BigDecimal endingBalanceAmount = new BigDecimal(0);
        BigDecimal differenceAmount = new BigDecimal(0);

        for (TransactionExp transaction : allTransactions) {
            Date createdAt = transaction.getCreatedAt();
            List<String> income = Arrays.asList(Type.KHOAN_THU.getDisplayName(), Type.THU_NO.getDisplayName(), Type.DI_VAY.getDisplayName());
            BigDecimal transactionAmount = transaction.getSpend();
            if (!income.contains(transaction.getCategory().getType())) {
                transactionAmount = new BigDecimal(0).subtract(transactionAmount);
            }

            if (createdAt.before(startDate)) {
                openingBalanceAmount = openingBalanceAmount.add(transactionAmount);
            }
            if (createdAt.after(startDate) && createdAt.before(endDate) || createdAt.equals(startDate) || createdAt.equals(endDate)) {
                filteredTransactions.add(transaction);
            }
            if (createdAt.equals(endDate) || createdAt.before(endDate)) {
                endingBalanceAmount = endingBalanceAmount.add(transactionAmount);
            }
        }

        differenceAmount = endingBalanceAmount.subtract(openingBalanceAmount);

        transactionAdapter.updateTransaction(filteredTransactions);
        transactionAdapter.notifyDataSetChanged();

        String currency = "";
        if (!filteredTransactions.isEmpty()) {
            currency = filteredTransactions.get(0).getCurrency();
            transactionEmpty.setVisibility(View.GONE);
        } else {
            transactionEmpty.setVisibility(View.VISIBLE);
        }

        if (openingBalanceAmount.compareTo(new BigDecimal(0)) < 0) {
            openingBalance.setText(String.format("%s %s", Helper.formatCurrency(openingBalanceAmount), currency));
            openingBalance.setTextColor(Color.parseColor("#F48484"));
        } else {
            openingBalance.setText(String.format("+%s %s", Helper.formatCurrency(openingBalanceAmount), currency));
            openingBalance.setTextColor(Color.parseColor("#00DDB0"));

        }

        if (endingBalanceAmount.compareTo(new BigDecimal(0)) < 0) {
            endingBalance.setText(String.format("%s %s", Helper.formatCurrency(endingBalanceAmount), currency));
            endingBalance.setTextColor(Color.parseColor("#F48484"));
        } else {
            endingBalance.setText(String.format("+%s %s", Helper.formatCurrency(endingBalanceAmount), currency));
            endingBalance.setTextColor(Color.parseColor("#00DDB0"));

        }

        if (differenceAmount.compareTo(new BigDecimal(0)) < 0) {
            difference.setText(String.format("%s %s", Helper.formatCurrency(differenceAmount), currency));
            difference.setTextColor(Color.parseColor("#F48484"));
        } else {
            difference.setText(String.format("+%s %s", Helper.formatCurrency(differenceAmount), currency));
            difference.setTextColor(Color.parseColor("#00DDB0"));

        }

    }

    private String getFilter() {
        TabLayout.Tab tab = tabLayoutFilter.getTabAt(tabLayoutFilter.getSelectedTabPosition());
        String period = tab.getText().toString();
        return period;
    }

    private void initView(View view) {
        tabLayoutFilter = view.findViewById(R.id.filter);
        previousTime = view.findViewById(R.id.previous_time);
        nextTime = view.findViewById(R.id.next_time);
        time = view.findViewById(R.id.time);
        openingBalance = view.findViewById(R.id.opening_balance);
        endingBalance = view.findViewById(R.id.ending_balance);
        difference = view.findViewById(R.id.difference);
        transactionEmpty = view.findViewById(R.id.transaction_empty);
    }

    private void getTransactionsForUser(String userId) {
        AppUserRepository repository = AppUserRepository.getInstance();
        repository.getTransaction(userId, new ApiCallBack<List<TransactionExp>>() {
            @Override
            public void onSuccess(List<TransactionExp> transactions) {
                allTransactions = transactions;
                filterTransactions(new Date(), new Date());

            }

            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    @Override
    public void onItemClick(TransactionExp transactionExp) {
        TransactionDetailsFragment transactionDetailsFragment = TransactionDetailsFragment.newInstance(transactionExp);
        transactionDetailsFragment.show(getActivity().getSupportFragmentManager(), transactionDetailsFragment.getTag());
    }
}