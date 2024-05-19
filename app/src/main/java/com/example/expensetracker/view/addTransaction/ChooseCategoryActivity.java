package com.example.expensetracker.view.addTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Upload.UploadResponse;
import com.example.expensetracker.databinding.ActivityChooseCategoryBinding;

import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.repository.UploadRepository;
import com.example.expensetracker.view.login.LoginActivity;
import com.example.expensetracker.viewmodel.addTransactionVM.ChooseCategoryViewModel;
import com.google.gson.Gson;

public class ChooseCategoryActivity extends AppCompatActivity {
    private  ImageView btnBack;
    private AppCompatButton test;
    Intent intent= getIntent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_category);
        ActivityChooseCategoryBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_choose_category);
        ChooseCategoryViewModel chooseCategoryViewModel= new ChooseCategoryViewModel(this);
        binding.setChooseCategoryViewModel(chooseCategoryViewModel);

        btnBack=findViewById(R.id.btnBackCategory);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });


        //adapter
        // Kết nối RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewCategory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // Tạo Adapter
        CategoryAdapter adapter = new CategoryAdapter(chooseCategoryViewModel.getListCategory().getValue());
        recyclerView.setAdapter(adapter);

        // Thiết lập sự kiện click
        adapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Category clickedItem = chooseCategoryViewModel.getListCategory().getValue().get(position);
                finish();
            }
        });

        // Quan sát dữ liệu thay đổi trong ViewModel
        chooseCategoryViewModel.getListCategory().observe(this, items -> {
            adapter.notifyDataSetChanged(); // Thông báo Adapter cập nhật dữ liệu mới
        });

        //test
        test= findViewById(R.id.btnTest);
        test.setOnClickListener(v -> {
            UploadRepository.getInstance().uploadImage("app/src/main/res/drawable/ic_salary.png", new ApiCallBack<UploadResponse>() {
                @Override
                public void onSuccess(UploadResponse uploadResponse) {
                    Toast.makeText(ChooseCategoryActivity.this, "thành công" + uploadResponse.getImage(), Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onError(String message) {
                    Toast.makeText(ChooseCategoryActivity.this, "thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        });


    }

}
