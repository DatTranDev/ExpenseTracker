package com.example.expensetracker.view.budget;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.repository.BudgetRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.utils.ToastUtil;
import com.example.expensetracker.view.addTransaction.ChooseCategoryActivity;
import com.example.expensetracker.view.addTransaction.ThousandSeparatorTextWatcher;
import com.example.expensetracker.view.addTransaction.mainAddActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;

public class EditBudgetActivity extends AppCompatActivity {
    Intent intent;
    TextView name;
    EditText money;
    ImageView icon;
    Spinner time;
    Gson gson= new Gson();
    BudgetItem budgetItem;
    Button accept;
    String period;
    RelativeLayout categoryLayout;
    List<Budget> listBudget;
    View loading;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        setContentView(R.layout.activity_edit_budget);
        name= findViewById(R.id.nameCategory);
        money= findViewById(R.id.money_budget);
        icon=findViewById(R.id.image_category);
        time=findViewById(R.id.choosePeriod);
        loading= findViewById(R.id.loading);
        accept= findViewById(R.id.accept);
        back= findViewById(R.id.btnBack2);
        //lấy dữ liệu
        String json=intent.getStringExtra("budget");
        budgetItem= gson.fromJson(json,BudgetItem.class);
        String list= intent.getStringExtra("listBudget");
        Type type = new TypeToken<List<Budget>>() {}.getType();
        listBudget = gson.fromJson(list,type);
        //gán data
        name.setText(budgetItem.nameCategory);
        money.setText(budgetItem.Amount);
        money.addTextChangedListener(new ThousandSeparatorTextWatcher(money));
        icon.setImageResource(budgetItem.idIcon);
        categoryLayout=findViewById(R.id.layout_category);
        //spiner
        int selection;
        Log.d("testt",budgetItem.budget.getPeriod());
        if(budgetItem.budget.getPeriod().equals("Tuần"))
        {
            selection=0;
        }
        else {
            if(budgetItem.budget.getPeriod().equals("Tháng")){
                selection=1;
            }
            else
            {
                selection=2;
            }

        }
        String[] listPeriod = {"Theo tuần", "Theo tháng", "Theo năm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listPeriod);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        time.setAdapter(adapter);
        time.setSelection(selection);
        time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
               period=selectedItem;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Khi không chọn gì
            }
        });
        //click
        back.setOnClickListener(v -> {
            finish();
        });
        categoryLayout.setOnClickListener(v->{
            Intent intent2= new Intent(this, ChooseCategoryActivity.class);
            intent2.putExtra("typeTrans","spend");
            startActivityForResult(intent2,69);
        });
        //button
        accept.setOnClickListener(v->{
            BudgetItem newBudget= budgetItem;
            BigDecimal spend ;

            String cleanString = money.getText().toString().replaceAll("[,]", "");
            spend=new BigDecimal(cleanString);
//            int compare= spend.compareTo(wallet.get().getAmount());
//            if(compare>0)
//            {
//                showMessage("Số tiền đã vượt quá số tiền trong ví");
//                return;
//            }
            String periodStirng="Tuần";
            if(period.equals("Theo tháng"))
            {
                periodStirng="Tháng";
            }
            if(period.equals("Theo năm"))
            {
                periodStirng="Năm";
            }
            newBudget.budget.setAmount(spend);
            newBudget.budget.setPeriod(periodStirng);
            if (loading != null) {
                loading.setVisibility(View.VISIBLE);
            }
            Log.d("testtt",newBudget.budget.getId());
            BudgetRepository.getInstance().updateBudget(newBudget.budget, new ApiCallBack<Budget>() {
                @Override
                public void onSuccess(Budget budget) {

                    resetData(newBudget.budget);
                    SharedPreferencesManager.getInstance(EditBudgetActivity.this).saveList("budgets",listBudget);
                    setResult(1);
                    if (loading != null) {
                        loading.setVisibility(View.GONE);
                    }
                    ToastUtil.showCustomToast(EditBudgetActivity.this, "Sửa ngân sách thành công", 1000);
//                    Toast.makeText(EditBudgetActivity.this, "Sửa ngân sách thành công", Toast.LENGTH_SHORT).show();
                    setResult(1);
                    finish();
                }

                @Override
                public void onError(String message) {
                    ToastUtil.showCustomToast(EditBudgetActivity.this, "Sửa ngân sách thất bại",1000);
//                    Toast.makeText(EditBudgetActivity.this, "Sửa ngân sách thất bại", Toast.LENGTH_SHORT).show();
                    if (loading != null) {
                        loading.setVisibility(View.GONE);
                    }
                }
            });
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==69 && resultCode==1)
        {
            Gson gson= new Gson();
            Category selected= gson.fromJson(data.getStringExtra("selectedString"),Category.class);
            budgetItem.budget.setCategory(selected);
            int resourceId = getResources().getIdentifier(selected.getIcon().getLinking(), "drawable", getPackageName());
            icon.setImageResource(resourceId);
            name.setText(selected.getName());
        }
    }
    public void resetData(Budget budget)
    {
        for(int i=0;i<listBudget.size();i++)
        {
            if(listBudget.get(i).getId().equals(budget.getId()))
            {
                listBudget.remove(i);
                listBudget.add(i,budget);
                break;
            }
        }
    }
}
