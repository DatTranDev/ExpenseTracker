package com.example.expensetracker.fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.expensetracker.R;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.adapter.WalletAdapter;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.view.Account.AccountWallet;
import com.example.expensetracker.bottom_sheet.DebtAccount;
import com.example.expensetracker.bottom_sheet.ExportFileAccount;
import com.example.expensetracker.bottom_sheet.NotifictionAccount;
import com.example.expensetracker.bottom_sheet.WalletUpdateListener;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.view.Account.ViewCategoryList;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.MainActivity;
import com.example.expensetracker.view.login.LoginActivity;


import java.util.List;


public class AccountFragment extends Fragment implements TransactionAdapter.OnItemClickListener, WalletUpdateListener{


    private WalletAdapter walletAdapter;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private List<Wallet> walletList;
    private ImageButton back;

    private AppUser user;

    private LinearLayout walletLayout;
    private LinearLayout btnlogout;
    private LinearLayout account_debt;
    private LinearLayout account_category;
    private LinearLayout account_notification;
    private LinearLayout account_export;
    private TextView userName;

    public static AccountFragment newInstance() {
        AccountFragment fragment = new AccountFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("onCreate: ", "Account");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);

        userName = view.findViewById(R.id.account_name);
        userName.setText(user.getUserName());

        back = view.findViewById(R.id.back);
        back.setOnClickListener(v -> {
            MainActivity mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.navigateToHome();
            }
        });

        walletAdapter = new WalletAdapter(walletList);
        getWalletList();
        walletLayout = view.findViewById(R.id.linearLayout_wallet);
//        walletLayout.setOnClickListener(v ->
//                showAllWallet(walletList));
        walletLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccountWallet.class);
                startActivity(intent);
            }
        });

        account_debt = view.findViewById(R.id.account_debt);
        account_debt.setOnClickListener(v -> {
            DebtAccount debtAccount = new DebtAccount();
            debtAccount.show(getActivity().getSupportFragmentManager(), debtAccount.getTag());
        });

        account_category = view.findViewById(R.id.account_category);
        account_category.setOnClickListener(v -> {
            Intent intent2= new Intent(getActivity(), ViewCategoryList.class);
            startActivity(intent2);
        });

        account_notification = view.findViewById(R.id.account_notifiication_layout);
        account_notification.setOnClickListener(v -> {
        Toast.makeText(getActivity(),"Tính năng đang được phát triển",Toast.LENGTH_SHORT).show();
//            NotifictionAccount notifictionAccount = new NotifictionAccount();
//            notifictionAccount.show(getActivity().getSupportFragmentManager(), notifictionAccount.getTag());
//            Intent intent = new Intent(getActivity(), NotifictionAccount.class);
//            startActivity(intent);
        });

        account_export = view.findViewById(R.id.account_export_file);
        account_export.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportFileAccount exportFileAccount = new ExportFileAccount();
                exportFileAccount.show(getActivity().getSupportFragmentManager(), exportFileAccount.getTag());
            }
        });

        btnlogout=view.findViewById(R.id.logout);
        btnlogout.setOnClickListener(v -> showLogoutConfirmationDialog());

        return view;
    }

    private void getWalletList() {
        AppUserRepository repository = AppUserRepository.getInstance();
        repository.getWallet(user.getId(), new ApiCallBack<List<Wallet>>() {
            @Override
            public void onSuccess(List<Wallet> wallets) {
                walletList = wallets;
//                walletAdapter.updateWallet(wallets);
//                walletAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Xác nhận đăng xuất");
        builder.setMessage("Bạn có muốn đăng xuất hay không?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                logout();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onItemClick(TransactionExp transactionExp) {

    }

    @Override
    public void onWalletAdded(Wallet wallet) {
        walletList.add(wallet);
        walletAdapter.notifyItemInserted(walletList.size() - 1);
        //setData();
    }

    @Override
    public void onWalletUpdated(Wallet wallet) {
        for (int i = 0; i < walletList.size(); i++) {
            if (walletList.get(i).getId().equals(wallet.getId())) {
                walletList.set(i, wallet);
                walletAdapter.notifyItemChanged(i);
                //setData();
                break;
            }
        }
    }

    @Override
    public void onWalletDeleted(String walletId) {
        for (int i = 0; i < walletList.size(); i++) {
            if (walletList.get(i).getId().equals(walletId)) {
                walletList.remove(i);
                walletAdapter.notifyItemRemoved(i);
                //setData();
                break;
            }
        }
    }

    private void logout() {
        SharedPreferencesManager.getInstance(getActivity()).removeKey("user");

        // Hiển thị thông báo đăng xuất thành công
        Toast.makeText(getActivity(), "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Chuyển về màn hình đăng nhập
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

        // Kết thúc activity hiện tại
        getActivity().finish();
    }

}