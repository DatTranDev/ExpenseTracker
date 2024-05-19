package com.example.expensetracker.bottom_sheet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.WalletAdapter;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Wallet.WalletReq;
import com.example.expensetracker.enums.Currency;
import com.example.expensetracker.enums.Type;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.WalletRepository;
import com.example.expensetracker.utils.Helper;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddWalletFragment extends BottomSheetDialogFragment {
    private TextView btnCancel;
    private WalletUpdateListener walletUpdateListener;
    private EditText walletName;
    private EditText walletAmount;
    private TextView btnSave;
    private AppUser user;

    public static AddWalletFragment newInstance() {
        AddWalletFragment addWalletFragment = new AddWalletFragment();
        return addWalletFragment;
    }

    public void setWalletUpdateListener(WalletUpdateListener walletUpdateListener) {
        this.walletUpdateListener = walletUpdateListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");
        user = new Gson().fromJson(userJson, AppUser.class);

        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_add_wallet, null);
        bottomSheetDialog.setContentView(viewDialog);
        initView(viewDialog);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = walletName.getText().toString();
                BigDecimal amount = (walletAmount.getText().toString()).isEmpty() ? new BigDecimal(0) : new BigDecimal(walletAmount.getText().toString());

                if (name.isEmpty() || amount.equals(new BigDecimal(0))) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
                    return;
                }

                WalletReq walletReq = new WalletReq();
                walletReq.setAmount(amount);
                walletReq.setCurrency("VND");
                walletReq.setName(name);
                walletReq.setSharing(false);
                walletReq.setUserId(user.getId());

                WalletRepository.getInstance().addWallet(walletReq, new ApiCallBack<Wallet>() {
                    @Override
                    public void onSuccess(Wallet wallet) {
                        Toast.makeText(getContext(), "Tạo mới ví thành công", Toast.LENGTH_SHORT).show();
                        if (walletUpdateListener != null) {
                            walletUpdateListener.onWalletAdded(wallet);
                        }
                        dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getContext(), "Tạo ví thất bại!\n Lỗi: " + message, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    int maxHeight = getResources().getDisplayMetrics().heightPixels;
                    maxHeight = maxHeight - maxHeight / 4;

                    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.height = maxHeight;;
                        bottomSheet.setLayoutParams(layoutParams);
                    }
                }
            }
        });

        return bottomSheetDialog;
    }

    private void initView(View view) {
        btnCancel = view.findViewById(R.id.btn_cancel);
        btnSave = view.findViewById(R.id.save_wallet);
        walletName = view.findViewById(R.id.wallet_name);
        walletAmount = view.findViewById(R.id.wallet_amount);
    }


}
