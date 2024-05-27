package com.example.expensetracker.view.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.WalletShowAdapter;
import com.example.expensetracker.bottom_sheet.WalletUpdateListener;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.view.addTransaction.CategoryAdapter;
import com.example.expensetracker.view.addTransaction.ChooseCategoryActivity;
import com.example.expensetracker.view.budget.AddBudgetActivity;
import com.example.expensetracker.viewmodel.addTransactionVM.ChooseCategoryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class ViewCategoryList extends AppCompatActivity {
    private ImageButton btnCancel;

    private TabLayout tabLayoutFilter;
    private AppCompatButton btnAdd;
    private RecyclerView recyclerView;
    private WalletShowAdapter walletAdapter;
    private WalletUpdateListener walletUpdateListener;
    private TextView total;
    private List<Wallet> wallets;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_category);
        Intent intent = getIntent();

        initView();

        //ActivityChooseCategoryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_choose_category);
        //ChooseCategoryViewModel chooseCategoryViewModel = new ChooseCategoryViewModel(this, type);
        //binding.setChooseCategoryViewModel(chooseCategoryViewModel);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0, intent);
                finish();
            }
        });

        tabLayoutFilter.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterCategories();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                filterCategories();
            }
        });

        getCategoriesForUser("spend");


        btnAdd.setOnClickListener(v -> {
            Intent intent1 = new Intent(ViewCategoryList.this, AddCategoryActivity.class);
            String type = "";
            switch (getFilter()) {
                case "Khoản chi":
                    type = "spend";
                    break;
                case "Khoản thu":
                    type = "revenue";
                    break;
                case "Vay/nợ":
                    type = "loan";
                    break;
            }
            intent1.putExtra("typeTrans",type);
            startActivity(intent1);
            getCategoriesForUser(type);
        });

    }

    private void initView() {
        btnAdd = findViewById(R.id.btnGoogleLogin);
        btnCancel = findViewById(R.id.category_back);
        tabLayoutFilter = findViewById(R.id.filter);
        recyclerView = findViewById(R.id.category_list);
        // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // walletAdapter = new WalletShowAdapter(wallets, this);
        //total = view.findViewById(R.id.wallet_total_amount);
        // recyclerView.setAdapter(walletAdapter);
    }

    private void filterCategories() {
        String check = "";
        switch (getFilter()) {
            case "Khoản chi":
                check = "spend";
                btnAdd.setVisibility(View.VISIBLE);
                break;
            case "Khoản thu":
                check = "revenue";
                btnAdd.setVisibility(View.VISIBLE);
                break;
            case "Vay/nợ":
                check = "loan";
                btnAdd.setVisibility(View.GONE);
                break;
        }

        getCategoriesForUser(check);
    }

    private void getCategoriesForUser(String check) {
        ChooseCategoryViewModel chooseCategoryViewModel= new ChooseCategoryViewModel(this,check,false);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Tạo Adapter

        CategoryAdapter adapter = new CategoryAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);
        chooseCategoryViewModel.getListCategory().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (categories != null && !categories.isEmpty()) {
                    adapter.updateCategories(categories);
                }
            }
        });
    }


    private String getFilter() {
        TabLayout.Tab tab = tabLayoutFilter.getTabAt(tabLayoutFilter.getSelectedTabPosition());
        String period = tab.getText().toString();
        return period;
    }
}
