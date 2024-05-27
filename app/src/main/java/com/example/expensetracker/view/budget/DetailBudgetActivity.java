package com.example.expensetracker.view.budget;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityChooseCategoryBinding;
import com.example.expensetracker.databinding.ActivityDetailBudgetBinding;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.viewmodel.addTransactionVM.ChooseCategoryViewModel;
import com.example.expensetracker.viewmodel.budgetVM.DetailBudgetViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DetailBudgetActivity extends AppCompatActivity {
    Intent intent;
    private List<TransactionExp> listTransaction = new ArrayList<>();
    private List<Object> listTransactionShow= new ArrayList<>();
    private BudgetItem budgetItem;
    public Gson gson= new Gson();
    RecyclerView recyclerView;
    TextView total,name,enable,time;
    ImageView icon;
    LinearLayout empty;
    private DetailBudgetViewModel detailBudgetViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_budget);
        intent=getIntent();
        ActivityDetailBudgetBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_detail_budget);
        detailBudgetViewModel= new DetailBudgetViewModel(this);
        binding.setDetailBudgetViewModel((detailBudgetViewModel));
        //findView
        recyclerView=findViewById(R.id.recyclerViewBudgetDetail);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        empty=findViewById(R.id.transaction_empty2);
        total= findViewById(R.id.money_budget);
        name= findViewById(R.id.nameCategory);
        enable= findViewById(R.id.moneyEnabled);
        time=findViewById(R.id.time);
        icon= findViewById(R.id.imageCategory);

        DetailBudgetAdapter adapter= new DetailBudgetAdapter(new ArrayList<>(),this);
        recyclerView.setAdapter(adapter);


        //get data
        String budgetItemString=intent.getStringExtra("selectedBudget");
        String listTransactionString= intent.getStringExtra("listTransaction");
        Type type = new TypeToken<List<TransactionExp>>() {}.getType();
        budgetItem=gson.fromJson(budgetItemString,BudgetItem.class);
        listTransaction= gson.fromJson(listTransactionString,type);
        listTransaction.sort((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()));
        listTransactionShow=groupTransactionsByDate(listTransaction);
        adapter.setData(listTransactionShow);
        name.setText(budgetItem.nameCategory);
        total.setText(budgetItem.Amount);
        if(budgetItem.Progress>100)
        {

            enable.setText("Vượt quá " + Helper.formatMoney(new BigDecimal(0).subtract(budgetItem.Enabled)));
            enable.setTextColor(ColorStateList.valueOf(Color.parseColor("#F35656")));
        }
        else {

            enable.setText("Còn lại " + Helper.formatMoney(budgetItem.Enabled));
            if(budgetItem.Progress < 60)
            {
                enable.setTextColor(ColorStateList.valueOf(Color.parseColor("#1AEB16"))); // Thay Color.RED bằng màu bạn muốn
            }
            if(budgetItem.Progress>=60&& budgetItem.Progress<=90)
            {
                enable.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFC75C")));
            }
            if(budgetItem.Progress>90)
            {
                enable.setTextColor(ColorStateList.valueOf(Color.parseColor("#F35656")));
            }
        }
        if(budgetItem.idIcon==0)
        {
            icon.setImageResource(R.drawable.error);
        }
        else
        {
            icon.setImageResource(budgetItem.idIcon);
        }
    }
    public List<Object> groupTransactionsByDate(List<TransactionExp> transactions) {
        List<Object> groupedItems = new ArrayList<>();
        if(transactions.size()!=0)
        {
            Log.d("tttt",transactions.get(0).getCategory().getName());
            Timestamp currentDate = transactions.get(0).getCreatedAt();
            groupedItems.add(currentDate);
            for (TransactionExp transaction : transactions) {
                if(transaction.getCategoryId().equals(budgetItem.budget.getCategoryId()))
                {
                    if (!transaction.getCreatedAt().equals(currentDate)) {
                        currentDate = transaction.getCreatedAt();
                        groupedItems.add(currentDate);
                    }
                    groupedItems.add(transaction);
                }

            }

        }
       for(int i=0;i<groupedItems.size();i++)//lọc bảo ngày k có giao dịch
       {
           if(groupedItems.get(i) instanceof Timestamp)
           {
               if(i+1<groupedItems.size())
               {
                   if(!(groupedItems.get(i+1) instanceof TransactionExp))
                   {
                       groupedItems.remove(i);
                   }
               }
               else
               {
                   groupedItems.remove(i);
               }

           }
       }
        if (!groupedItems.isEmpty()) {
            empty.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.VISIBLE);
        }
        return groupedItems;
    }

}
