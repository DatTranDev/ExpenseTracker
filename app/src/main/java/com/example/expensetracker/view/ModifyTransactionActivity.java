package com.example.expensetracker.view;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.example.expensetracker.databinding.ActivityModifyTransactionBinding;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.view.addTransaction.CategoryAdapter;
import com.example.expensetracker.view.addTransaction.ChooseCategoryActivity;
import com.example.expensetracker.view.addTransaction.ChooseWalletFragment;
import com.example.expensetracker.view.addTransaction.ThousandSeparatorTextWatcher;
import com.example.expensetracker.viewmodel.AddTransactionViewModel;
import com.example.expensetracker.viewmodel.ModifyTransactionViewModel;
import com.google.gson.Gson;

import java.sql.Timestamp;
import java.util.Calendar;

public class ModifyTransactionActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private CategoryAdapter myAdapter;

    private ImageButton btnSpend, btnRevenue, btnLoan;
    private String typeTransaction="spend";
    private  RelativeLayout layCategory, layTime, layNote, layWallet, layBorrower;
    private ImageView btnBack, iconCategory;
    private TextView timeTran;
    private EditText money;
    private TransactionExp transaction;
    Intent intent= getIntent();
    private ModifyTransactionViewModel modifyTransactionViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        ActivityModifyTransactionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_modify_transaction);
        modifyTransactionViewModel = new ModifyTransactionViewModel(this);
        binding.setModifyTransactionViewModel(modifyTransactionViewModel);


        Intent intent = getIntent();
        if (intent != null) {
            String transactionJson = intent.getStringExtra("transaction");
            if (transactionJson != null) {
                Gson gson = new Gson();
                transaction = gson.fromJson(transactionJson, TransactionExp.class);
                if (transaction != null) {
                    modifyTransactionViewModel.setData(transaction);
                    binding.setTransactionExp(transaction);
                }
            }
        }


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

        timeTran.setText(Helper.formatDate(modifyTransactionViewModel.timeTransaction.get()));

        if (transaction != null && transaction.getCategory() != null) {
            Log.e("Type: ", transaction.getCategory().getType());
            switch (transaction.getCategory().getType()) {
                case "Khoản chi":
                    btnSpend.setSelected(true);
                    typeTransaction="spend";
                    btnSpend.setBackgroundResource(R.drawable.choose_type_button);
                    btnRevenue.setBackgroundResource(R.drawable.default_type_button);
                    btnLoan.setBackgroundResource(R.drawable.default_type_button);
                    layBorrower.setVisibility(View.GONE);
                    money.setTextColor(getResources().getColor(R.color.red));
                    break;
                case "Khoản thu":
                    btnRevenue.setSelected(true);
                    typeTransaction="revenue";
                    btnRevenue.setBackgroundResource(R.drawable.choose_type_button);
                    btnLoan.setBackgroundResource(R.drawable.default_type_button);
                    btnSpend.setBackgroundResource(R.drawable.default_type_button);
                    layBorrower.setVisibility(View.GONE);
                    money.setTextColor(getResources().getColor(R.color.green));

                    break;
                default:
                    btnLoan.setSelected(true);
                    typeTransaction="loan";
                    btnLoan.setBackgroundResource(R.drawable.choose_type_button);
                    btnRevenue.setBackgroundResource(R.drawable.default_type_button);
                    btnSpend.setBackgroundResource(R.drawable.default_type_button);
                    layBorrower.setVisibility(View.VISIBLE);
                    money.setTextColor(getResources().getColor(R.color.orange));
                    break;
            }
        }


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
            Intent intent2= new Intent(ModifyTransactionActivity.this, ChooseCategoryActivity.class);
            intent2.putExtra("typeTrans",typeTransaction);
            startActivityForResult(intent2,69);
        });
        layTime.setOnClickListener(v -> {
            Timestamp transactionTimestamp = modifyTransactionViewModel.timeTransaction.get();

            final Calendar calendar = Calendar.getInstance();

            if (transactionTimestamp != null) {
                calendar.setTimeInMillis(transactionTimestamp.getTime());
            }

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog= new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    timeTran.setText(selectedDate);
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
                    selectedCalendar.set(Calendar.MILLISECOND, 0);
                    modifyTransactionViewModel.timeTransaction.set(new Timestamp(selectedCalendar.getTimeInMillis()));
                }
            }, year, month, day);

            datePickerDialog.show();
        });
        btnBack.setOnClickListener(v -> finish());
        layWallet.setOnClickListener(v -> {
            ChooseWalletFragment chooseWalletFragment= new ChooseWalletFragment();
            chooseWalletFragment.show(getSupportFragmentManager(), "listWallet");
            chooseWalletFragment.setSend(new ChooseWalletFragment.sendWallet() {
                @Override
                public void selectedWallet(Wallet selected) {
                    modifyTransactionViewModel.wallet.set(selected);
                }
            });
        });
        money.addTextChangedListener(new ThousandSeparatorTextWatcher(money));
        modifyTransactionViewModel.message.observe(this, message -> {
            if (message != null) {
                Toast.makeText(ModifyTransactionActivity.this, message, Toast.LENGTH_SHORT).show();
                if (message.equals("Sửa giao dịch thành công!")) {
                    Intent resultIntent = new Intent();
                    Gson gson = new Gson();
                    String updatedTransactionJson = gson.toJson(modifyTransactionViewModel.getUpdatedTransaction(transaction));
                    resultIntent.putExtra("updatedTransaction", updatedTransactionJson);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
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
            modifyTransactionViewModel.category.set(selected);
            int resourceId = getResources().getIdentifier(selected.getIcon().getLinking(), "drawable", getPackageName());
            iconCategory.setImageResource(resourceId);
        }
    }
    public void resetData()
    {
        modifyTransactionViewModel.category.set(null);
        modifyTransactionViewModel.borrower.set(null);
        modifyTransactionViewModel.node.set(null);
        iconCategory.setImageResource(R.drawable.ic_category);
    }
}
