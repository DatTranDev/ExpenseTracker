package com.example.expensetracker.view.addTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityChooseCategoryBinding;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.viewmodel.addTransactionVM.ChooseCategoryViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ChooseCategoryActivity extends AppCompatActivity {
    private  ImageView btnBack;
    private AppCompatButton test;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        Intent intent = getIntent();
        String type= intent.getStringExtra("typeTrans");
        boolean check = intent.getBooleanExtra("check",false);
        Log.d("lỗi",type);
        ActivityChooseCategoryBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_choose_category);
        ChooseCategoryViewModel chooseCategoryViewModel= new ChooseCategoryViewModel(this, check);
        chooseCategoryViewModel.setTypeTransaction(type);
        binding.setChooseCategoryViewModel(chooseCategoryViewModel);

        btnBack=findViewById(R.id.btnBackCategory);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(0,intent);
                finish();
            }
        });


        //adapter
        // Kết nối RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Tạo Adapter



        CategoryAdapter adapter = new CategoryAdapter(this,new ArrayList<>());
        recyclerView.setAdapter(adapter);
        chooseCategoryViewModel.getListCategory().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (categories != null && !categories.isEmpty()) {
                    adapter.updateCategories(categories);
                }
            }
        });


        // Thiết lập sự kiện click
        adapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Category clickedItem = chooseCategoryViewModel.getListCategory().getValue().get(position);
                Gson gson= new Gson();
                String json = gson.toJson(clickedItem);
                Log.d("testttt",json);
                intent.putExtra("selectedString",json);
                setResult(1,intent);
                finish();
            }
        });

        // Quan sát dữ liệu thay đổi trong ViewModel




    }

}