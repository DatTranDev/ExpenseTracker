package com.example.expensetracker.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.view.SurfaceControl;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Category.CategoryReq;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Icon;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.repository.IconRepository;
import com.example.expensetracker.utils.Constant;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.register.WelcomeActivity;
import com.google.gson.Gson;
import android.Manifest;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity{
    boolean flag = true;
    @Override
    protected void onCreate(android.os.Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        Button retry = findViewById(R.id.reloadButton);
        retry.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                retry.setVisibility(android.view.View.GONE);
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        IconRepository.getInstance().getAllIcons(new ApiCallBack<List<Icon>>() {
            @Override
            public synchronized void onSuccess(List<Icon> response) {
                //Save icons to shared preferences
                SharedPreferencesManager.getInstance(SplashScreenActivity.this).saveList("icons", response);
                boolean load = loadAllData(retry);
                if (!load) {
                    new android.os.Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 100);
                }
                else{
                    new android.os.Handler().postDelayed(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 100);
                }
            }
            @Override
            public void onError(String message) {
                android.widget.Toast.makeText(SplashScreenActivity.this, message, android.widget.Toast.LENGTH_SHORT).show();
                retry.setVisibility(android.view.View.VISIBLE);
            }
        });
    }

    private synchronized boolean loadAllData(Button retry){

        //test update
//        CategoryReq categoryReq = new CategoryReq();
//        categoryReq.setUserId("6615a4b40d01b7dd489839bc");
//        categoryReq.setName("Xu tiktok thanh toán 12");
//        categoryReq.setIconId("6647af5e58421edbe63eabe2");
//        categoryReq.setParentCategoryId("6615894eb71e80c899ce774b");
//        categoryReq.setType("Khoản chi");
//        categoryReq.setPublic(false);
//
//        String cateId = "66587cf7ac866f93a662b165";
//
//        CategoryRepository.getInstance().updateCategory(cateId, categoryReq, new ApiCallBack<Category>() {
//            @Override
//            public void onSuccess(Category category) {
//                android.widget.Toast.makeText(SplashScreenActivity.this, "Update category success", android.widget.Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onError(String message) {
//                android.widget.Toast.makeText(SplashScreenActivity.this, message, android.widget.Toast.LENGTH_SHORT).show();
//            }
//        });


        // Get user if logged in
        AppUser appUser = SharedPreferencesManager.getInstance(this).getObject("user", AppUser.class);
        if (appUser != null) {
            AppUserRepository.getInstance().getCategory(appUser.getId(), new ApiCallBack<List<Category>>() {
                @Override
                public synchronized void onSuccess(List<Category> response) {
                    SharedPreferencesManager.getInstance(SplashScreenActivity.this).saveList("categories", response );
                }
                @Override
                public void onError(String message) {
                    android.widget.Toast.makeText(SplashScreenActivity.this, "Failed to load Category", android.widget.Toast.LENGTH_SHORT).show();
                    retry.setVisibility(android.view.View.VISIBLE);
                    flag = false;
                }
            });
            AppUserRepository.getInstance().getBudget(appUser.getId(), new ApiCallBack<List<Budget>>() {
                @Override
                public synchronized void onSuccess(List<Budget> response) {
                    SharedPreferencesManager.getInstance(SplashScreenActivity.this).saveList("budgets", response );
                }
                @Override
                public void onError(String message) {
                    android.widget.Toast.makeText(SplashScreenActivity.this, "Failed to load Budget", android.widget.Toast.LENGTH_SHORT).show();
                    retry.setVisibility(android.view.View.VISIBLE);
                    flag = false;
                }
            });
            AppUserRepository.getInstance().getTransaction(appUser.getId(), new ApiCallBack<List<TransactionExp>>() {
                @Override
                public synchronized void onSuccess(List<TransactionExp> response) {
                    SharedPreferencesManager.getInstance(SplashScreenActivity.this).saveList("transactions", response );
                }
                @Override
                public void onError(String message) {
                    android.widget.Toast.makeText(SplashScreenActivity.this, "Failed to load Transaction", android.widget.Toast.LENGTH_SHORT).show();
                    retry.setVisibility(android.view.View.VISIBLE);
                    flag = false;
                }
            });
            AppUserRepository.getInstance().getWallet(appUser.getId(), new ApiCallBack<List<Wallet>>() {
                @Override
                public synchronized void onSuccess(List<Wallet> response) {
                    SharedPreferencesManager.getInstance(SplashScreenActivity.this).saveList("wallets", response );
                }
                @Override
                public void onError(String message) {
                    android.widget.Toast.makeText(SplashScreenActivity.this, "Failed to load Wallet", android.widget.Toast.LENGTH_SHORT).show();
                    retry.setVisibility(android.view.View.VISIBLE);
                    flag = false;
                }
            });

            AppUserRepository.getInstance().getSharingWallet(appUser.getId(), new ApiCallBack<List<Wallet>>() {
                @Override
                public synchronized void onSuccess(List<Wallet> response) {
                    SharedPreferencesManager.getInstance(SplashScreenActivity.this).saveList("sharingWallets", response );
                }
                @Override
                public void onError(String message) {
                    android.widget.Toast.makeText(SplashScreenActivity.this, "Failed to load Sharing Wallet", android.widget.Toast.LENGTH_SHORT).show();
                    retry.setVisibility(android.view.View.VISIBLE);
                    flag = false;

                }
            });
            return flag;
        }
        else
            return false;
    }
}