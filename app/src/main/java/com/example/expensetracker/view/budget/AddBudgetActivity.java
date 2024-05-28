package com.example.expensetracker.view.budget;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityAddBudgetBinding;
import com.example.expensetracker.databinding.ActivityAddTransactionBinding;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.addTransaction.ChooseCategoryActivity;
import com.example.expensetracker.view.addTransaction.ThousandSeparatorTextWatcher;
import com.example.expensetracker.view.addTransaction.mainAddActivity;
import com.example.expensetracker.viewmodel.AddTransactionViewModel;
import com.example.expensetracker.viewmodel.budgetVM.AddBudgetViewModel;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.Calendar;

public class AddBudgetActivity extends AppCompatActivity {
    private RelativeLayout layCategory, layTime;
    private Wallet wallet;
    private  EditText money;
    TextView time;
    ImageView iconCategory;
    Spinner period;
    AddBudgetViewModel addBudgetViewModel;
    Intent intent;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        intent=getIntent();
        setContentView(R.layout.activity_add_budget);
        String[] listPeriod = {"Theo tuần", "Theo tháng", "Theo năm"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listPeriod);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ActivityAddBudgetBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_budget);
         addBudgetViewModel= new AddBudgetViewModel(this);
        binding.setAddBudgetViewModel(addBudgetViewModel);
        layCategory= findViewById(R.id.layout_category);
        layTime= findViewById(R.id.lay_time);
        money= findViewById(R.id.money_budget);
        iconCategory= findViewById(R.id.image_category);
        back=findViewById(R.id.btnBack2);
        period= findViewById(R.id.choosePeriod);
        period.setAdapter(adapter);


//        time= findViewById(R.id.timeBudget);
        layCategory.setOnClickListener(v -> {
            Intent intent2= new Intent(AddBudgetActivity.this, ChooseCategoryActivity.class);
            intent2.putExtra("typeTrans","spend");
            startActivityForResult(intent2,69);
        });
//        layTime.setOnClickListener(v -> {
//            final Calendar calendar = Calendar.getInstance();
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            DatePickerDialog datePickerDialog= new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
//                @Override
//                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                    // Lấy dữ liệu ngày tháng năm mà người dùng đã chọn
//
//                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
//                    time.setText(selectedDate);
//                    Calendar selectedCalendar = Calendar.getInstance();
//                    selectedCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
//                    selectedCalendar.set(Calendar.MILLISECOND, 0);
//                   addBudgetViewModel.time.set(new Timestamp(selectedCalendar.getTimeInMillis()));
////                    System.out.println("Selected Timestamp: " + addTransactionViewModel.getTimeTransaction().toString());
//                }
//            }, year, month, day);
//
//            datePickerDialog.show();
//        });
        back.setOnClickListener(v -> {
            finish();
        });
        addBudgetViewModel.message.observe(this, message -> {
            if (message != null) {
                // Hiển thị thông báo
                Toast.makeText(AddBudgetActivity.this, message, Toast.LENGTH_SHORT).show();
                if(message.equals("Thêm ngân sách thành công"))
                {
                    setResult(1);
                    finish();
                }
            }
        });
        period.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                addBudgetViewModel.period.set(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Khi không chọn gì
            }
        });
        money.addTextChangedListener(new ThousandSeparatorTextWatcher(money));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==69 && resultCode==1)
        {
            Gson gson= new Gson();
            Category selected= gson.fromJson(data.getStringExtra("selectedString"),Category.class);
            addBudgetViewModel.category.set(selected);
            int resourceId = getResources().getIdentifier(selected.getIcon().getLinking(), "drawable", getPackageName());
            iconCategory.setImageResource(resourceId);
        }
    }
}
