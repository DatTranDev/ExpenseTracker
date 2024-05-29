package com.example.expensetracker.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.databinding.ActivityMainBinding;
import com.example.expensetracker.fragment.AccountFragment;
import com.example.expensetracker.fragment.BudgetFragment;
import com.example.expensetracker.fragment.FundFragment;
import com.example.expensetracker.fragment.HomeFragment;
import com.example.expensetracker.fragment.TransactionFragment;
import com.example.expensetracker.model.Icon;
import com.example.expensetracker.repository.IconRepository;
import com.example.expensetracker.view.addTransaction.mainAddActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private ViewPager2 viewPager;
    private FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
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
        IconRepository.getInstance().getAllIcons(new ApiCallBack<List<Icon>>() {
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

        //REQUEST
        //GET REQUEST BY USER
//        RequestRepository.getInstance().getRequestsByUser("6615ab5c55cbe4d6104aa825", new ApiCallBack<List<Request>>() {
//            @Override
//            public void onSuccess(List<Request> requests) {
//                for (Request request : requests) {
//                    System.out.println(request.getName());
//                }
//            }
//
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });
        //RESPONSE REQUEST
//        RequestRes requestRes = new RequestRes();
//        requestRes.setRequestId("661758e9fc1bf79782d9ce37");
//        requestRes.setAccepted(true);
//        RequestRepository.getInstance().responseRequest(requestRes, new ApiCallBack<Request>() {
//            @Override
//            public void onSuccess(Request request) {
//                System.out.println(request.getName());
//            }
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });

        //Wallet
        //ADD WALLET
        //Ví riêng isSharing = false
//        WalletReq walletReq = new WalletReq();
//        walletReq.setName("Quỹ đen giấu vk");
//        walletReq.setAmount(new BigDecimal(1000000));
//        walletReq.setCurrency("VND");
//        walletReq.setSharing(false);
//        walletReq.setUserId("6615a4b40d01b7dd489839bc");
        //Ví chung isSharing = true
        //Ví chung có thêm list email để mời
        //walletReq.setInviteUserMail(List<String> inviteUserMail);

//        WalletRepository.getInstance().addWallet(walletReq, new ApiCallBack<Wallet>() {
//            @Override
//            public void onSuccess(Wallet wallet) {
//                System.out.println(wallet.getName());
//            }
//
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });


//        AppUserRepository.getInstance().findByEmail("neban0444@gmail.com", new ApiCallBack<AppUser>() {
//            @Override
//            public void onSuccess(AppUser appUser) {
//                System.out.println(appUser.getEmail());
//            }
//
//            @Override
//            public void onError(String message) {
//                System.out.println(message);
//            }
//        });
//        TransactionRepository.getInstance().getNeedToPay("6615a4b40d01b7dd489839bc", new ApiCallBack<List<TransactionExp>>() {
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
        fab = findViewById(R.id.fab);

        binding.navBar.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
                fab.show();
            } else if (item.getItemId() == R.id.transactionLog) {
                replaceFragment(new TransactionFragment());
                fab.show();
            } else if (item.getItemId() == R.id.budget) {
                replaceFragment(new BudgetFragment());
                fab.hide();
            } else if (item.getItemId() == R.id.mutualFund) {
                replaceFragment(new FundFragment());
                fab.hide();
            } else if (item.getItemId() == R.id.account) {
                replaceFragment(new AccountFragment());
                fab.hide();
            }

            return true;
        });
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, mainAddActivity.class);
            startActivityForResult(intent,69);
        });
    }

    public void navigateToTransactions() {
        replaceFragment(new TransactionFragment());
        binding.navBar.setSelectedItemId(R.id.transactionLog);
    }

    public void navigateToHome() {
        replaceFragment(new HomeFragment());
        binding.navBar.setSelectedItemId(R.id.home);
    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentLayout, fragment);
        fragmentTransaction.addToBackStack(fragment.getTag());
        fragmentTransaction.commit();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 69 && resultCode == 1) {
            if(binding.navBar.getSelectedItemId()==R.id.home)
            {
                replaceFragment(new HomeFragment());
            }

            if(binding.navBar.getSelectedItemId()==R.id.transactionLog)
            {
                replaceFragment(new TransactionFragment());
            }

        }
    }
}