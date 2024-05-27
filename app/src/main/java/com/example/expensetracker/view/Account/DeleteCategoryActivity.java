package com.example.expensetracker.view.Account;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
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
import com.example.expensetracker.R;
import com.example.expensetracker.databinding.ActivityDeleteCategoryBinding;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Icon;
import com.example.expensetracker.view.addTransaction.ChooseCategoryActivity;
import com.example.expensetracker.viewmodel.accountVM.AddCategoryViewModel;
import com.example.expensetracker.viewmodel.accountVM.DeleteCategoryViewModel;
import com.google.gson.Gson;

import java.util.Objects;

public class DeleteCategoryActivity extends AppCompatActivity {
    Category parentCategory;
    ImageView iconCategory;
    ImageView iconParentCategory;
    Button deletebtn;

    String title;

    Category category;

    DeleteCategoryViewModel deleteCategoryViewModel;
    Intent intent;
    private ImageView back;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        intent = getIntent();
        setContentView(R.layout.activity_delete_category);
        Gson gson= new Gson();
        category = gson.fromJson(intent.getStringExtra("selectedCategory"), Category.class);
        title = intent.getStringExtra("type");

        parentCategory = gson.fromJson(intent.getStringExtra("parentCate"),Category.class);

        ActivityDeleteCategoryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_delete_category);
        deleteCategoryViewModel = new DeleteCategoryViewModel(this);
        binding.setDeleteCategoryViewModel(deleteCategoryViewModel);

        iconCategory= findViewById(R.id.iconcategory);
        iconParentCategory = findViewById(R.id.iconparent);
        back = findViewById(R.id.imageView2);
        back.setOnClickListener(v -> {
            finish();
        });

        deleteCategoryViewModel.type.set(title);
        deleteCategoryViewModel.category.set(category);
        deleteCategoryViewModel.nameCategory.set(category.getName());
        if (category.getIcon() != null) {
            int resourceId = getResources().getIdentifier(category.getIcon().getLinking(), "drawable", getPackageName());
            iconCategory.setImageResource(resourceId);
        }

        if (parentCategory != null) {
            deleteCategoryViewModel.parentCategory.set(parentCategory.getName());
            int parentIconId = getResources().getIdentifier(parentCategory.getIcon().getLinking(), "drawable", getPackageName());
            iconParentCategory.setImageResource(parentIconId);
        }

        Log.e("title",title);
        deletebtn = findViewById(R.id.delete_category);
        if (Objects.equals(title, "Vay/nợ") || parentCategory == null){
            Log.e("delete","true");
            deletebtn.setVisibility(View.GONE);
        }

        deleteCategoryViewModel.get_message().observe(this, message -> {
            if (message != null) {
                Intent intent1 = new Intent();
                intent1.putExtra("typedelete",title);
                setResult(1,intent1);
                Toast.makeText(DeleteCategoryActivity.this, message, Toast.LENGTH_SHORT).show();
                if (message == "Xóa danh mục thành công")
                    finish();
            }
        });

    }
}
