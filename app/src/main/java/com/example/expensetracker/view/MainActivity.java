package com.example.expensetracker.view;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.ViewPagerAdapter;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.databinding.ActivityMainBinding;
import com.example.expensetracker.fragment.AccountFragment;
import com.example.expensetracker.fragment.BudgetFragment;
import com.example.expensetracker.fragment.FundFragment;
import com.example.expensetracker.fragment.HomeFragment;
import com.example.expensetracker.fragment.TransactionDetailsFragment;
import com.example.expensetracker.fragment.TransactionFragment;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.repository.BudgetRepository;
import com.example.expensetracker.repository.IconRepository;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ViewPager2 viewPager;
    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //TEST API
        //USER
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
        //GET ALL TRANSACTION
//        AppUserRepository.getInstance().getTransaction("6615a4b40d01b7dd489839bc", new ApiCallBack<List<TransactionExp>>() {
//            @Override
//            public void onSuccess(List<TransactionExp> transactionExps) {
//                for (TransactionExp transactionExp : transactionExps) {
//                    System.out.println(transactionExp.getUserId());
//                }
//            }
//
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });
        //GET ALL WALLET
//        AppUserRepository.getInstance().getWallet("6615a4b40d01b7dd489839bc", new ApiCallBack<List<Wallet>>() {
//            @Override
//            public void onSuccess(List<Wallet> transactionExps) {
//                for (Wallet transactionExp : transactionExps) {
//                    System.out.println(transactionExp.getAmount());
//                }
//            }
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });
        //GET ALL SHARING WALLET
//        AppUserRepository.getInstance().getSharingWallet("6615a4b40d01b7dd489839bc", new ApiCallBack<List<Wallet>>() {
//            @Override
//            public void onSuccess(List<Wallet> wallets) {
//                for (Wallet wallet : wallets) {
//                    System.out.println(wallet.getAmount());
//                }
//            }
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });

        //GET ALL BUDGET
//        AppUserRepository.getInstance().getBudget("6615a4b40d01b7dd489839bc", new ApiCallBack<List<Budget>>() {
//            @Override
//            public void onSuccess(List<Budget> budgets) {
//                for (Budget budget : budgets) {
//                    System.out.println(budget.getAmount());
//                }
//            }
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });
        //UPDATE USER
//        AppUser appUser = new AppUser();
//        appUser.setAlertTime("10:00");
//        AppUserRepository.getInstance().update("6615a4b40d01b7dd489839bc", appUser, new ApiCallBack<AppUser>() {
//            @Override
//            public void onSuccess(AppUser appUser) {
//                //Update success,don't return appUser
//            }
//
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });

        //Budget
        //ADD BUDGET
//        Budget budget = new Budget();
//        BigDecimal amount = new BigDecimal(1500000);
//        budget.setAmount(amount);
//        budget.setCategoryId("66158875b71e80c899ce7746");
//        budget.setUserId("6615a4b40d01b7dd489839bc");
//        budget.setPeriod("Tháng");
//        budget.setCurrency("VND");
//        BudgetRepository.getInstance().addBudget(budget, new ApiCallBack<Budget>() {
//            @Override
//            public void onSuccess(Budget budget) {
//                System.out.println(budget.getAmount());
//            }
//
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });
        //UPDATE BUDGET

        //ICON
        //GET ALL ICON
        IconRepository.getInstance().getAllIcons(new ApiCallBack<List<com.example.expensetracker.model.Icon>>() {
            @Override
            public void onSuccess(List<com.example.expensetracker.model.Icon> icons) {
                for (com.example.expensetracker.model.Icon icon : icons) {
                    System.out.println(icon.getLinking());
                }
            }

            @Override
            public void onError(String message) {
                System.out.println(message);
            }
        });
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        fab = findViewById(R.id.fab);
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        binding.navBar.setSelectedItemId(R.id.home);
                        fab.show();
                        break;
                    case 1:
                        binding.navBar.setSelectedItemId(R.id.transactionLog);
                        fab.show();
                        break;
                    case 2:
                        binding.navBar.setSelectedItemId(R.id.budget);
                        fab.hide();
                        break;
                    case 3:
                        binding.navBar.setSelectedItemId(R.id.mutualFund);
                        fab.hide();
                        break;
                    case 4:
                        binding.navBar.setSelectedItemId(R.id.account);
                        fab.hide();
                        break;
                }
            }
        });

        binding.navBar.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                viewPager.setCurrentItem(0);
            } else if (item.getItemId() == R.id.transactionLog) {
                viewPager.setCurrentItem(1);
            } else if (item.getItemId() == R.id.budget) {
                viewPager.setCurrentItem(2);
            } else if (item.getItemId() == R.id.mutualFund) {
                viewPager.setCurrentItem(3);
            } else if (item.getItemId() == R.id.account) {
                viewPager.setCurrentItem(4);
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.addToBackStack(fragment.getTag());
        fragmentTransaction.commit();
    }

}