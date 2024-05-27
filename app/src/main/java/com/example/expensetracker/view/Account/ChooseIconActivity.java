package com.example.expensetracker.view.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.expensetracker.R;
import com.example.expensetracker.adapter.IconAdapter;
import com.example.expensetracker.databinding.ActivityChooseCategoryBinding;
import com.example.expensetracker.databinding.ActivityChooseIconBinding;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Icon;
import com.example.expensetracker.view.addTransaction.CategoryAdapter;
import com.example.expensetracker.viewmodel.accountVM.ChooseIconViewModel;
import com.example.expensetracker.viewmodel.addTransactionVM.ChooseCategoryViewModel;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class ChooseIconActivity extends AppCompatActivity {
    private  ImageView btnBack;

    private IconAdapter adapter;
    private List<Icon> iconList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_icon);

        ActivityChooseIconBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_choose_icon);
        ChooseIconViewModel chooseIconViewModel= new ChooseIconViewModel(this);
        binding.setChooseIconViewModel(chooseIconViewModel);

        btnBack=findViewById(R.id.btnBackCategory);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0);
                finish();
            }
        });


        //adapter
        // Kết nối RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewIcon);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        // Tạo Adapter

        adapter = new IconAdapter(this, iconList);
        recyclerView.setAdapter(adapter);


        chooseIconViewModel.getListIcon().observe(this, new Observer<List<Icon>>() {
            @Override
            public void onChanged(List<Icon> icons) {

                if (icons != null && !icons.isEmpty()) {
                    iconList = icons;
                    adapter.updateIcons(iconList);
                }
            }
        });


        adapter.setOnItemClickListener(new IconAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Icon clickedItem) {
                if (clickedItem != null) {
                    Gson gson = new Gson();
                    String json = gson.toJson(clickedItem);
                    Intent intent = new Intent();
                    Log.d("testttt", json);
                    intent.putExtra("selectedIcon", json);
                    setResult(1, intent);
                    finish();
                } else {
                    Log.e("ChooseIconActivity", "Clicked item is null");
                }
            }
        });
    }
}
