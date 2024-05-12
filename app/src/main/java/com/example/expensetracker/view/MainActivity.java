package com.example.expensetracker.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.databinding.ActivityMainBinding;
import com.example.expensetracker.fragment.AccountFragment;
import com.example.expensetracker.fragment.BudgetFragment;
import com.example.expensetracker.fragment.FundFragment;
import com.example.expensetracker.fragment.HomeFragment;
import com.example.expensetracker.fragment.TransactionDetailsFragment;
import com.example.expensetracker.fragment.TransactionFragment;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.repository.AppUserRepository;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        //TEST API
        //GET ALL CATEGORY
//        AppUserRepository ins = AppUserRepository.getInstance();
//        ins.getCategory("6615a4b40d01b7dd489839bc", new ApiCallBack<List<Category>>() {
//            @Override
//            public void onSuccess(List<Category> categories) {
//                for (Category category : categories) {
//                    System.out.println(category.getName());
//                }
//            }
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });





        binding.navBar.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.transactionLog) {
                replaceFragment(new TransactionFragment());
            } else if (item.getItemId() == R.id.budget) {
                replaceFragment(new BudgetFragment());
            } else if (item.getItemId() == R.id.mutualFund) {
                replaceFragment(new FundFragment());
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(new AccountFragment());
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.commit();
    }

}