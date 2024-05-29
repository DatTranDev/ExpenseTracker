package com.example.expensetracker.view.addTransaction;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityAddTransactionBinding;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.viewmodel.AddTransactionViewModel;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.Calendar;

public class mainAddActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private CategoryAdapter myAdapter;

    private ImageButton btnSpend, btnRevenue, btnLoan;
    private String typeTransaction="spend";
    private  RelativeLayout layCategory, layTime, layNote, layWallet, layBorrower;
    private ImageView btnBack, iconCategory;
    private TextView timeTran;
    private EditText money;
    Intent intent= getIntent();
    AddTransactionViewModel addTransactionViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        ActivityAddTransactionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_transaction);
        addTransactionViewModel = new AddTransactionViewModel(this);
        binding.setAddTransactionViewModel(addTransactionViewModel);
        ImageButton btnChoose;
        btnSpend = findViewById(R.id.btn_spend);
        btnRevenue = findViewById(R.id.btn_revenue);
        btnLoan = findViewById(R.id.btn_loan);
        layBorrower= findViewById(R.id.borrower);
        layCategory= findViewById(R.id.chooseCategory);
        layTime= findViewById(R.id.chooseTime);
        layNote= findViewById(R.id.chooseNote);
        layWallet= findViewById(R.id.chooseWallet);
        btnBack= findViewById(R.id.btnBack1);
        timeTran= findViewById(R.id.timeTran);
        iconCategory=findViewById(R.id.icon_category);
        money= findViewById(R.id.edit_text_money);
        money.setTextColor(getResources().getColor(R.color.red));
//        btnChoose=btnSpend;
//        btnChoose.setBackgroundResource(R.drawable.choose_type_button);
        btnSpend.setOnClickListener(v -> {
            typeTransaction="spend";
            btnSpend.setBackgroundResource(R.drawable.choose_type_button);
            btnRevenue.setBackgroundResource(R.drawable.default_type_button);
            btnLoan.setBackgroundResource(R.drawable.default_type_button);
            layBorrower.setVisibility(View.GONE);
            money.setTextColor(getResources().getColor(R.color.red));
            resetData();
        });
        btnLoan.setOnClickListener(v -> {
            typeTransaction="loan";
            btnLoan.setBackgroundResource(R.drawable.choose_type_button);
            btnRevenue.setBackgroundResource(R.drawable.default_type_button);
            btnSpend.setBackgroundResource(R.drawable.default_type_button);
            layBorrower.setVisibility(View.VISIBLE);
            money.setTextColor(getResources().getColor(R.color.orange));
            resetData();
        });
        btnRevenue.setOnClickListener(v -> {
            typeTransaction="revenue";
            btnRevenue.setBackgroundResource(R.drawable.choose_type_button);
            btnLoan.setBackgroundResource(R.drawable.default_type_button);
            btnSpend.setBackgroundResource(R.drawable.default_type_button);
            layBorrower.setVisibility(View.GONE);
            money.setTextColor(getResources().getColor(R.color.green));
            resetData();
        });
        layCategory.setOnClickListener(v -> {
            Intent intent2= new Intent(mainAddActivity.this,ChooseCategoryActivity.class);
            intent2.putExtra("typeTrans",typeTransaction);
            startActivityForResult(intent2,69);
        });
        layTime.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog= new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    // Lấy dữ liệu ngày tháng năm mà người dùng đã chọn

                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    timeTran.setText(selectedDate);
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                    selectedCalendar.set(Calendar.MILLISECOND, 0);
                    addTransactionViewModel.timeTransaction.set(new Timestamp(selectedCalendar.getTimeInMillis()));
//                    System.out.println("Selected Timestamp: " + addTransactionViewModel.getTimeTransaction().toString());
                }
            }, year, month, day);

            datePickerDialog.show();
        });
        btnBack.setOnClickListener(v -> finish());
//        String a="ic_cho_vay";
//        int resourceId = getResources().getIdentifier(a, "drawable", getPackageName());
//        btnBack.setImageResource(resourceId);
        layWallet.setOnClickListener(v -> {
            ChooseWalletFragment chooseWalletFragment= new ChooseWalletFragment();
            chooseWalletFragment.show(getSupportFragmentManager(), "listWallet");
            chooseWalletFragment.setSend(new ChooseWalletFragment.sendWallet() {
                @Override
                public void selectedWallet(Wallet selected) {
                    addTransactionViewModel.wallet.set(selected);
                }
            });
        });
        money.addTextChangedListener(new ThousandSeparatorTextWatcher(money));
        addTransactionViewModel.message.observe(this, message -> {
            if (message != null) {
                if(message.equals("Thêm giao dịch thành công"))
                {
                    // Hiển thị thông báo
                    Toast.makeText(mainAddActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                }
                // Hiển thị thông báo
                Toast.makeText(mainAddActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==69 && resultCode==1)
        {
            Gson gson= new Gson();
            Category selected= gson.fromJson(data.getStringExtra("selectedString"),Category.class);
            addTransactionViewModel.category.set(selected);
            int resourceId = getResources().getIdentifier(selected.getIcon().getLinking(), "drawable", getPackageName());
            iconCategory.setImageResource(resourceId);
        }
    }
    public void resetData()
    {
        addTransactionViewModel.category.set(null);
        addTransactionViewModel.borrower.set(null);
        addTransactionViewModel.node.set(null);
        iconCategory.setImageResource(R.drawable.ic_category);
    }
}
