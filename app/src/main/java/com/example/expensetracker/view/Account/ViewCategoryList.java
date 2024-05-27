package com.example.expensetracker.view.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ViewCategoryList extends AppCompatActivity {
    private ImageButton btnCancel;

    private TabLayout tabLayoutFilter;
    private AppCompatButton btnAdd;
    private RecyclerView recyclerView;

    private ChooseCategoryViewModel chooseCategoryViewModel;
    private CategoryAdapter adapter;
    private String type;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_category);
        Intent intent = getIntent();
        chooseCategoryViewModel = new ChooseCategoryViewModel(this, false);
        //chooseCategoryViewModel.setTypeTransaction("spend");

        initView();

        getCategoriesForUser("spend");

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


        btnAdd.setOnClickListener(v -> {
            Intent intent1 = new Intent(ViewCategoryList.this, AddCategoryActivity.class);
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
            startActivityForResult(intent1, 69);
            getCategoriesForUser(type);
        });

        //setupAdapter();

    }

    private void setupAdapter() {

        adapter.setOnItemClickListener(position -> {
            Category clickedItem = chooseCategoryViewModel.getListCategory().getValue().get(position);
            Gson gson = new Gson();
            String json = gson.toJson(clickedItem);
            Intent intent2 = new Intent(ViewCategoryList.this, DeleteCategoryActivity.class);
            intent2.putExtra("selectedCategory", json);
            intent2.putExtra("type", getFilter());
            startActivity(intent2);
        });
    }


    private void initView() {
        btnAdd = findViewById(R.id.btnGoogleLogin);
        btnCancel = findViewById(R.id.category_back);
        tabLayoutFilter = findViewById(R.id.filter);
        recyclerView = findViewById(R.id.category_list);
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
        chooseCategoryViewModel.setTypeTransaction(check);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategoryAdapter(this, new ArrayList<>() );
        recyclerView.setAdapter(adapter);
        chooseCategoryViewModel.getListCategory().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (categories != null && !categories.isEmpty()) {
                    adapter.updateCategories(categories);
                }
            }
        });

        adapter.setOnItemClickListener(position -> {
            Category clickedItem = chooseCategoryViewModel.getListCategory().getValue().get(position);
            Gson gson = new Gson();
            String json = gson.toJson(clickedItem);
            Intent intent2 = new Intent(ViewCategoryList.this, DeleteCategoryActivity.class);
            intent2.putExtra("selectedCategory", json);
            intent2.putExtra("type", getFilter());
            Category parent = chooseCategoryViewModel.getParent(clickedItem.getParentCategoryId());
            String json2 = gson.toJson(parent);
            intent2.putExtra("parentCate",json2);

            startActivityForResult(intent2 , 70);
        });
    }

    private void getCategoriesForUser2(String check) {
        chooseCategoryViewModel.setTypeTransaction(check);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new CategoryAdapter(this, new ArrayList<>() );
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69 && resultCode == 1){
            getCategoriesForUser(type);
        }
        if (requestCode == 70 && resultCode == 1)
        {
            if (data != null && data.hasExtra("typedelete")) {
                String typeDelete = data.getStringExtra("typedelete");
                switch (typeDelete) {
                    case "Khoản chi":
                        getCategoriesForUser("spend");
                        break;
                    case "Khoản thu":
                        getCategoriesForUser("revenue");
                        break;
                }
            }
        }
    }
}
