package com.example.expensetracker.view.Account;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.expensetracker.utils.ToastUtil;
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
    EditText name;

    Button modifybtn;

    String title;

    Category category;

    DeleteCategoryViewModel deleteCategoryViewModel;
    Intent intent;
    private ImageView back;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_category);

        ActivityDeleteCategoryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_delete_category);
        deleteCategoryViewModel = new DeleteCategoryViewModel(this);
        binding.setDeleteCategoryViewModel(deleteCategoryViewModel);


        intent = getIntent();

        Gson gson= new Gson();
        category = gson.fromJson(intent.getStringExtra("selectedCategory"), Category.class);
        title = intent.getStringExtra("type");


        parentCategory = gson.fromJson(intent.getStringExtra("parentCate"),Category.class);
        if (parentCategory!=null) {
            deleteCategoryViewModel.parentCate.set(parentCategory);
        }


        iconCategory= findViewById(R.id.iconcategory);
        if (!title.equals("Vay/nợ")) {
            iconCategory.setOnClickListener(v -> {
                Intent intent1 = new Intent(DeleteCategoryActivity.this, ChooseIconActivity.class);
                startActivityForResult(intent1, 69);
            });
        }


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
        modifybtn = findViewById(R.id.modify_category);
        name = findViewById(R.id.editTextText);
        if (Objects.equals(title, "Vay/nợ") ){
            Log.e("delete","true");
            deletebtn.setVisibility(View.GONE);
            modifybtn.setVisibility(View.GONE);
            name.setFocusable(false);
        }

        deleteCategoryViewModel.get_message().observe(this, message -> {
            if (message != null) {
                if (message == "Xóa danh mục thành công" ) {
                    Intent intent1 = new Intent();
                    intent1.putExtra("typedelete", title);
                    setResult(1, intent1);
                    ToastUtil.showCustomToast(DeleteCategoryActivity.this, message, 1000);
                    finish();
//                Toast.makeText(DeleteCategoryActivity.this, message, Toast.LENGTH_SHORT).show();
                }
                else
                if (message == "Sửa danh mục thành công") {
                    Intent intent1 = new Intent();
                    intent1.putExtra("typemodify", title);
                    setResult(1, intent1);
                    ToastUtil.showCustomToast(DeleteCategoryActivity.this, message, 1000);
                    finish();
                }
                else {
                    ToastUtil.showCustomToast(DeleteCategoryActivity.this, message, 1000);
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
            assert data != null;
            if (data.getStringExtra("selectedIcon") != null) {
                Icon icon = gson.fromJson(data.getStringExtra("selectedIcon"), Icon.class);
                deleteCategoryViewModel.iconCategory.set(icon);
                int id = getResources().getIdentifier(icon.getLinking(), "drawable", getPackageName());
                iconCategory.setImageResource(id);
            }
        }
    }
}
