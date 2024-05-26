package com.example.expensetracker.view.Account;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.example.expensetracker.databinding.ActivityAddCategoryBinding;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.view.addTransaction.ChooseCategoryActivity;
import com.example.expensetracker.view.addTransaction.ThousandSeparatorTextWatcher;
import com.example.expensetracker.view.budget.AddBudgetActivity;
import com.example.expensetracker.viewmodel.accountVM.AddCategoryViewModel;
import com.example.expensetracker.viewmodel.budgetVM.AddBudgetViewModel;
import com.google.gson.Gson;

public class AddCategoryActivity extends AppCompatActivity {
    private LinearLayout parentCategory;
    ImageView iconCategory;
    ImageView iconParentCategory;

    TextView title;

    AddCategoryViewModel addCategoryViewModel;
    Intent intent;
    private ImageView back;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        intent = getIntent();
        setContentView(R.layout.activity_add_category);

        ActivityAddCategoryBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_add_category);
        addCategoryViewModel = new AddCategoryViewModel(this);
        binding.setAddCategoryViewModel(addCategoryViewModel);

        parentCategory= findViewById(R.id.chooseParentCategory);
        iconCategory= findViewById(R.id.iconcategory);
        iconParentCategory = findViewById(R.id.iconparent);
        back = findViewById(R.id.imageView2);
        title = findViewById(R.id.title_category);


        String type = intent.getStringExtra("typeTrans");
        Log.e("Type:", type);
        switch (type) {
            case "spend":
                addCategoryViewModel.type.set("Khoản chi");
                break;
            case "revenue":
                addCategoryViewModel.type.set("Khoản thu");
                break;
        }

        parentCategory.setOnClickListener(v -> {
            boolean check = true;
            Intent intent2= new Intent(AddCategoryActivity.this, ChooseCategoryActivity.class);
            intent2.putExtra("typeTrans",type);
            intent2.putExtra("check", check);
            startActivityForResult(intent2,69);
        });

        iconCategory.setOnClickListener(v -> {
            Intent intent1 = new Intent(AddCategoryActivity.this, ChooseIconActivity.class);
            startActivityForResult(intent1,69);
        });

        back.setOnClickListener(v -> {
            finish();
        });

        addCategoryViewModel.get_message().observe(this, message -> {
            if (message != null) {

                Toast.makeText(AddCategoryActivity.this, message, Toast.LENGTH_SHORT).show();
                finish();
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
            Category selected= gson.fromJson(data.getStringExtra("selectedString"),Category.class);
            addCategoryViewModel.parentCategory.set(selected);
            int resourceId = getResources().getIdentifier(selected.getIcon().getLinking(), "drawable", getPackageName());
            iconParentCategory.setImageResource(resourceId);

        }
    }
}
