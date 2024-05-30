package com.example.expensetracker.view.budget;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.databinding.ActivityDetailBudgetBinding;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.repository.BudgetRepository;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.utils.ToastUtil;
import com.example.expensetracker.view.addTransaction.mainAddActivity;
import com.example.expensetracker.viewmodel.budgetVM.DetailBudgetViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailBudgetActivity extends AppCompatActivity {
    Intent intent;
    private List<TransactionExp> listTransaction = new ArrayList<>();
    private List<Object> listTransactionShow= new ArrayList<>();
    List<Budget> listBudget= new ArrayList<>();
    private BudgetItem budgetItem;
    public Gson gson= new Gson();
    RecyclerView recyclerView;
    TextView total,name,enable,time;
    ImageView icon;
    LinearLayout empty;
    Button edit,delete;
    String listBudgetString;
    View loading;
    ImageView back;
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
        edit=findViewById(R.id.button_edit);
        delete=findViewById(R.id.button_delete);
        loading= findViewById(R.id.loading);
        back= findViewById(R.id.btnBack2);

        DetailBudgetAdapter adapter= new DetailBudgetAdapter(new ArrayList<>(),this);
        recyclerView.setAdapter(adapter);


        //get data
        String budgetItemString=intent.getStringExtra("selectedBudget");
        String listTransactionString= intent.getStringExtra("listTransaction");
        listBudgetString = intent.getStringExtra("listBudget");
        Type type2 = new TypeToken<List<Budget>>() {}.getType();
        listBudget= gson.fromJson(listBudgetString,type2);
        Type type = new TypeToken<List<TransactionExp>>() {}.getType();
        budgetItem=gson.fromJson(budgetItemString,BudgetItem.class);
        listTransaction= gson.fromJson(listTransactionString,type);
        listTransaction.sort((t1, t2) -> t1.getCreatedAt().compareTo(t2.getCreatedAt()));
        listTransactionShow=groupTransactionsByDate(listTransaction);
        adapter.setData(listTransactionShow);
        name.setText(budgetItem.nameCategory);
        total.setText(budgetItem.Amount);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM");
        time.setText(formatter.format(budgetItem.startDate)+" - "+formatter.format(budgetItem.endDate));
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
        back.setOnClickListener(v->{
            finish();
        });
        edit.setOnClickListener(v->{
            Intent intent2= new Intent(DetailBudgetActivity.this,EditBudgetActivity.class);
            intent2.putExtra("budget",budgetItemString);
            intent2.putExtra("listBudget", listBudgetString);
            startActivityForResult(intent2,69);

        });
        delete.setOnClickListener(v->{
            if(loading!=null)
            {
                loading.setVisibility(View.VISIBLE);
            }
            BudgetRepository.getInstance().deleteBudget(budgetItem.budget.getId(), new ApiCallBack<Budget>() {
                @Override
                public void onSuccess(Budget budget) {

                        for(int i=0;i<listBudget.size();i++)
                        {
                            if(listBudget.get(i).getId().equals(budgetItem.budget.getId()))
                            {
                                listBudget.remove(i);
                                break;
                            }
                        }
                    SharedPreferencesManager.getInstance(DetailBudgetActivity.this).saveList("budgets",listBudget);
                    if(loading!=null)
                    {
                        loading.setVisibility(View.GONE);
                    }
                    Toast.makeText(DetailBudgetActivity.this, "Xóa ngân sách thành công", Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();

                }

                @Override
                public void onError(String message) {
                    if(loading!=null)
                    {
                        loading.setVisibility(View.GONE);
                    }
                    ToastUtil.showCustomToast(DetailBudgetActivity.this,"Xóa ngân sách thất bại",1000);
//                    Toast.makeText(DetailBudgetActivity.this, "Xóa ngân sách thất bại", Toast.LENGTH_SHORT).show();

                }
            });
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==69 && resultCode==1)
        {
            finish();
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
