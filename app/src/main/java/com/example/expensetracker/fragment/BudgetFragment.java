package com.example.expensetracker.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
//import com.example.expensetracker.databinding.FragmentBudgetBinding;
import com.example.expensetracker.enums.Type;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.view.MainActivity;

import com.example.expensetracker.view.budget.AddBudgetActivity;
import com.example.expensetracker.view.budget.BudgetAdapter;
import com.example.expensetracker.view.budget.BudgetItem;
import com.example.expensetracker.viewmodel.budgetVM.MainBudgetViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class BudgetFragment extends Fragment {

    private Button btnAddBudget;
    private TabLayout tabLayout;
    RecyclerView recycler;
    LinearLayout  transactionEmpty, layoutStatus;
    Intent intent;
    Context context;
    private ImageView test;
    private TextView time,status,total_amount,total_money_enable,total_spend;
    ProgressBar test2;
    BudgetAdapter adapter;
    MainBudgetViewModel mainBudgetViewModel;
    //   private String period="Tuần";
    private List<TransactionExp> allTransactions= new ArrayList<>();
    private List<Budget> allBudgets= new ArrayList<>();
    public BudgetFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view= inflater.inflate(R.layout.fragment_budget, container, false);
        context= getContext();
//         FragmentBudgetBinding binding = DataBindingUtil.setContentView(getActivity(), R.layout.fragment_budget);
//         mainBudgetViewModel= new MainBudgetViewModel(context);
//         binding.setMainBudgetViewModel(mainBudgetViewModel);
        btnAddBudget= view.findViewById(R.id.buttonAddBudget);
//        allTransactions= mainBudgetViewModel.listTransaction.get();
//        allBudgets= mainBudgetViewModel.listBudget.get();

        tabLayout= view.findViewById(R.id.filter);
        time= view.findViewById(R.id.timeBudget);
        transactionEmpty= view.findViewById(R.id.transaction_empty);
        recycler=view.findViewById(R.id.budget_list);
        recycler.setLayoutManager(new LinearLayoutManager((MainActivity)getActivity()));
        adapter= new BudgetAdapter(new ArrayList<>());
        recycler.setAdapter(adapter);
        status= view.findViewById(R.id.status_text);
        total_spend= view.findViewById(R.id.total_spend);
        total_amount= view.findViewById(R.id.total_amount);
        total_money_enable= view.findViewById(R.id.total_money_enable);
        layoutStatus= view.findViewById(R.id.layout1);
        adjustTimePeriod(0);
        getData();
        btnAddBudget.setOnClickListener(v -> {
            intent= new Intent(getActivity(), AddBudgetActivity.class);
            startActivity(intent);
        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

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
//        adjustTimePeriod(0);
//        mainBudgetViewModel.message.observe(this, message -> {
//            if (message != null) {
//                if(message.equals("reset"))
//                {
//                    allTransactions=mainBudgetViewModel.listTransaction.get();
//                    allBudgets=mainBudgetViewModel.listBudget.get();
//
//                }
//                else
//                {
//                    // Hiển thị thông báo
//                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });

        return view;
    }
    private String getFilter() {
        try{
            TabLayout.Tab tab = tabLayout.getTabAt(tabLayout.getSelectedTabPosition());
            String period = tab.getText().toString();
            return period;
        }
        catch (Exception ex)
        {
            return "Tuần";
        }
    }
    private void updateDateRange() {
        Calendar calendarStart = Calendar.getInstance(Locale.US);
        Calendar calendarEnd = Calendar.getInstance(Locale.US);
        calendarStart.setFirstDayOfWeek(Calendar.MONDAY);
        calendarEnd.setFirstDayOfWeek(Calendar.MONDAY);
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
        filterBudgets(calendarStart.getTime(), calendarEnd.getTime());
        time.setText(Helper.formatDate(calendarStart.getTime()) + " - " + Helper.formatDate(calendarEnd.getTime()));

    }
    private void adjustTimePeriod(int increment) {
        Log.d("testt", "đã tới 8");
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

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    public void filterBudgets(Date startDate, Date endDate) {
        BigDecimal totalBudget=new BigDecimal(0);
        BigDecimal moneyEnable= new BigDecimal(0);
        BigDecimal totalSpend = new BigDecimal(0);
        List<TransactionExp> filteredTransactions = new ArrayList<>();
        List<BudgetItem> filterBudget= new ArrayList<>();
        startDate = Helper.normalizeDate(startDate, true);
        endDate = Helper.normalizeDate(endDate, false);

        for (TransactionExp transaction : allTransactions) {
            Date createdAt = transaction.getCreatedAt();
            List<String> income = Arrays.asList(Type.KHOAN_THU.getDisplayName(), Type.THU_NO.getDisplayName(), Type.DI_VAY.getDisplayName());
            BigDecimal transactionAmount = transaction.getSpend();
            if(transaction.getCategory().getType().equals("Khoản chi"))
            {
                if (createdAt.after(startDate) && createdAt.before(endDate) || createdAt.equals(startDate) || createdAt.equals(endDate)) {
                    filteredTransactions.add(transaction);
                }
            }
        }
        for(Budget budget: allBudgets){
            if(budget.getPeriod().equals(getFilter()))
            {
                totalBudget=totalBudget.add(budget.getAmount());//tính tổng ngân sách
                BigDecimal spendBudget= new BigDecimal(0);
                int progress=0;
                BigDecimal enable= new BigDecimal(0);
                for (TransactionExp transaction : filteredTransactions) {
                    if(transaction.getCategoryId().equals(budget.getCategoryId()))
                    {
                        spendBudget=spendBudget.add(transaction.getSpend());
                    }
                }
                totalSpend=totalSpend.add(spendBudget);//Tính tổng chi
                BigDecimal result = spendBudget.divide(budget.getAmount(), 10, RoundingMode.HALF_UP); // Làm tròn kết quả với độ chính xác 10 chữ số thập phân
                result = result.multiply(new BigDecimal("100"));
                progress = result.setScale(0, RoundingMode.HALF_UP).intValue();
                enable=budget.getAmount().subtract(spendBudget);
                try {
                    String name= budget.getCategory().getIcon().getLinking();
                    Log.d("Húp",name);
                    int id = getResources().getIdentifier(name, "drawable", getActivity().getPackageName());
                    BudgetItem budgetItem= new BudgetItem(budget.getCategory().getName(),id,progress,Helper.formatMoney(budget.getAmount()),enable);
                    filterBudget.add(budgetItem);
                }
                catch (Exception ex)
                {
                    Log.d("Húp",ex.toString());
                }
            }
        }

        total_amount.setText(Helper.formatMoney(totalBudget));
        total_spend.setText(Helper.formatMoney(totalSpend));
        if(totalBudget.compareTo(new BigDecimal(0))>=1)
        {
            BigDecimal result2 = totalSpend.divide(totalBudget, 10, RoundingMode.HALF_UP); // Làm tròn kết quả với độ chính xác 10 chữ số thập phân
            result2 = result2.multiply(new BigDecimal("100"));
            int curency = result2.setScale(0, RoundingMode.HALF_UP).intValue();
            if(curency<=100)
            {
                status.setText("Còn lại");
                moneyEnable=totalBudget.subtract(totalSpend);//Còn lại
                total_money_enable.setText(Helper.formatMoney(moneyEnable));
                layoutStatus.setBackgroundResource(R.drawable.background_border_green);
                if(60<curency && curency<90)
                {
                    layoutStatus.setBackgroundResource(R.drawable.background_border_orange);
                }
                if(curency>=90)
                {
                    layoutStatus.setBackgroundResource(R.drawable.background_border_red);
                }
            }
            else
            {
                moneyEnable=totalSpend.subtract(totalBudget);
                total_money_enable.setText(Helper.formatMoney(moneyEnable));
                status.setText("Vượt quá");
                layoutStatus.setBackgroundResource(R.drawable.background_border_red);

            }
        }
        else
        {
            layoutStatus.setBackgroundResource(R.drawable.background_border_green);
            total_money_enable.setText("0");
        }

        adapter.updateBudgets(filterBudget);
        if (!filterBudget.isEmpty()) {
            transactionEmpty.setVisibility(View.GONE);
        } else {
            transactionEmpty.setVisibility(View.VISIBLE);
        }
    }
    public void getData(){
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");
        AppUser user = new Gson().fromJson(userJson, AppUser.class);
        if(user!=null) {
            AppUserRepository.getInstance().getTransaction(user.getId(), new ApiCallBack<List<TransactionExp>>() {
                @Override
                public void onSuccess(List<TransactionExp> transactions) {
                    allTransactions=(transactions);
                    updateDateRange();
                }

                @Override
                public void onError(String errorMessage) {
                }
            });
            AppUserRepository.getInstance().getBudget(user.getId(), new ApiCallBack<List<Budget>>() {
                @Override
                public void onSuccess(List<Budget> budgets) {

                    allBudgets=(budgets);
                    updateDateRange();
                }

                @Override
                public void onError(String message) {

                }
            });
        }
    }
}