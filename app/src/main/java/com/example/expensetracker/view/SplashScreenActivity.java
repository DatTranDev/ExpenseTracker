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
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Budget;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Icon;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.repository.IconRepository;
import com.example.expensetracker.view.register.WelcomeActivity;
import com.google.gson.Gson;
import android.Manifest;
import java.util.List;

public class SplashScreenActivity extends AppCompatActivity{
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
            public void onSuccess(List<Icon> response) {
                //Save icons to shared preferences
                SharedPreferences sharedPreferences = getSharedPreferences("icons", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("icons", new Gson().toJson(response));
                editor.apply();
                boolean load = loadAllData(retry);
                if (!load) {
                    new android.os.Handler().postDelayed(new Runnable() {
                        public void run() {
                            android.content.Intent intent = new android.content.Intent(SplashScreenActivity.this, WelcomeActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 100);
                }
                else{
                    new android.os.Handler().postDelayed(new Runnable() {
                        public void run() {
                            android.content.Intent intent = new android.content.Intent(SplashScreenActivity.this, MainActivity.class);
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

    private boolean loadAllData(Button retry){
        // Get user if logged in
        SharedPreferences sharedPreferencesUs = getSharedPreferences("user", MODE_PRIVATE);
        String userJson = sharedPreferencesUs.getString("user", null);
        AppUser appUser = new AppUser();
        if (userJson != null) {
            appUser = new Gson().fromJson(userJson, AppUser.class);

            // Define shared preferences for each feature
            SharedPreferences sharedPreferencesCat = getSharedPreferences("categories", MODE_PRIVATE);
            SharedPreferences sharedPreferencesBudget = getSharedPreferences("budgets", MODE_PRIVATE);
            SharedPreferences sharedPreferencesTrans = getSharedPreferences("transactions", MODE_PRIVATE);
            SharedPreferences sharedPreferencesWallet = getSharedPreferences("wallets", MODE_PRIVATE);
            SharedPreferences sharedPreferencesSharingWallet = getSharedPreferences("sharingWallets", MODE_PRIVATE);
            String test = sharedPreferencesWallet.getString("wallets", null);
            // Load categories if not stored
            if (!sharedPreferencesCat.contains("categories")) {
                AppUserRepository.getInstance().getCategory(appUser.getId(), new ApiCallBack<List<Category>>() {
                    @Override
                    public void onSuccess(List<Category> response) {
                        SharedPreferences.Editor editor = sharedPreferencesCat.edit();
                        editor.putString("categories", new Gson().toJson(response));
                        editor.apply();
                    }
                    @Override
                    public void onError(String message) {
                        android.widget.Toast.makeText(SplashScreenActivity.this, "Failed to load Category", android.widget.Toast.LENGTH_SHORT).show();
                        retry.setVisibility(android.view.View.VISIBLE);
                    }
                });
            }

            // Load budgets if not stored
            if (!sharedPreferencesBudget.contains("budgets")) {
                AppUserRepository.getInstance().getBudget(appUser.getId(), new ApiCallBack<List<Budget>>() {
                    @Override
                    public void onSuccess(List<Budget> response) {
                        SharedPreferences.Editor editor = sharedPreferencesBudget.edit();
                        editor.putString("budgets", new Gson().toJson(response));
                        editor.apply();
                    }
                    @Override
                    public void onError(String message) {
                        android.widget.Toast.makeText(SplashScreenActivity.this, "Failed to load Budget", android.widget.Toast.LENGTH_SHORT).show();
                        retry.setVisibility(android.view.View.VISIBLE);
                    }
                });
            }

            // Load transactions if not stored
            if (!sharedPreferencesTrans.contains("transactions")) {
                AppUserRepository.getInstance().getTransaction(appUser.getId(), new ApiCallBack<List<TransactionExp>>() {
                    @Override
                    public void onSuccess(List<TransactionExp> response) {
                        SharedPreferences.Editor editor = sharedPreferencesTrans.edit();
                        editor.putString("transactions", new Gson().toJson(response));
                        editor.apply();
                    }
                    @Override
                    public void onError(String message) {
                        android.widget.Toast.makeText(SplashScreenActivity.this, "Failed to load Transaction", android.widget.Toast.LENGTH_SHORT).show();
                        retry.setVisibility(android.view.View.VISIBLE);
                    }
                });
            }

            // Load wallets if not stored
            if (!sharedPreferencesWallet.contains("wallets")) {
                AppUserRepository.getInstance().getWallet(appUser.getId(), new ApiCallBack<List<Wallet>>() {
                    @Override
                    public void onSuccess(List<Wallet> response) {
                        SharedPreferences.Editor editor = sharedPreferencesWallet.edit();
                        editor.putString("wallets", new Gson().toJson(response));
                        editor.apply();
                    }
                    @Override
                    public void onError(String message) {
                        android.widget.Toast.makeText(SplashScreenActivity.this, "Failed to load Wallet", android.widget.Toast.LENGTH_SHORT).show();
                        retry.setVisibility(android.view.View.VISIBLE);
                    }
                });
            }

            // Load sharing wallets if not stored
            if (!sharedPreferencesSharingWallet.contains("sharingWallets")) {
                AppUserRepository.getInstance().getSharingWallet(appUser.getId(), new ApiCallBack<List<Wallet>>() {
                    @Override
                    public void onSuccess(List<Wallet> response) {
                        SharedPreferences.Editor editor = sharedPreferencesSharingWallet.edit();
                        editor.putString("sharingWallets", new Gson().toJson(response));
                        editor.apply();
                    }
                    @Override
                    public void onError(String message) {
                        android.widget.Toast.makeText(SplashScreenActivity.this, "Failed to load Sharing Wallet", android.widget.Toast.LENGTH_SHORT).show();
                        retry.setVisibility(android.view.View.VISIBLE);
                    }
                });
            }
            return true;
        }
        else return false;
    }
}
