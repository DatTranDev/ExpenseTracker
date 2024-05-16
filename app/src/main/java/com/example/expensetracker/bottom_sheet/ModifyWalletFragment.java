package com.example.expensetracker.bottom_sheet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.expensetracker.R;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Wallet.WalletReq;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.WalletRepository;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import java.math.BigDecimal;

public class ModifyWalletFragment extends BottomSheetDialogFragment {

    private static final String KEY_WALLET = "wallet";
    private Wallet wallet;
    private AppUser user;
    private TextView title;
    private TextView btnCancel;
    private String[] currencies = {"USD", "VND"};
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> currencyItemAdapter;
    private WalletUpdateListener walletUpdateListener;
    private TextInputLayout textInputLayout;
    private EditText walletName;
    private EditText walletAmount;
    private TextView btnSave;

    public static ModifyWalletFragment newInstance(Wallet wallet) {
        ModifyWalletFragment fragment = new ModifyWalletFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_WALLET, wallet);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wallet = getArguments().getParcelable(KEY_WALLET);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userJson = sharedPreferences.getString("user", "");
        user = new Gson().fromJson(userJson, AppUser.class);

        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_add_wallet, null); // reuse the same layout
        bottomSheetDialog.setContentView(viewDialog);
        initView(viewDialog);

        currencyItemAdapter = new ArrayAdapter<>(getContext(), R.layout.select_item, currencies);
        autoCompleteTextView.setAdapter(currencyItemAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textInputLayout.setHint(null);
            }
        });

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
                String currency = autoCompleteTextView.getText().toString();

                if (name.isEmpty() || amount.equals(new BigDecimal(0)) || currency.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ các trường!", Toast.LENGTH_SHORT).show();
                    return;
                }

                wallet.setAmount(amount);
                wallet.setCurrency(currency);
                wallet.setName(name);

                WalletRepository.getInstance().updateWallet(wallet.getId(), wallet, new ApiCallBack<Wallet>() {
                    @Override
                    public void onSuccess(Wallet updatedWallet) {
                        Toast.makeText(getContext(), "Cập nhật ví thành công", Toast.LENGTH_SHORT).show();
                        if (walletUpdateListener != null) {
                            walletUpdateListener.onWalletUpdated(wallet);
                        }
                        dismiss();
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(getContext(), "Có lỗi xảy ra", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        bottomSheetDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                textInputLayout.setHint(null);
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                if (bottomSheet != null) {
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    int maxHeight = getResources().getDisplayMetrics().heightPixels;
                    maxHeight = maxHeight - maxHeight / 4;

                    ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                    if (layoutParams != null) {
                        layoutParams.height = maxHeight;
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
        autoCompleteTextView = view.findViewById(R.id.add_wallet_currency);
        textInputLayout = view.findViewById(R.id.text_input_currency);
        walletName = view.findViewById(R.id.wallet_name);
        walletAmount = view.findViewById(R.id.wallet_amount);
        title = view.findViewById(R.id.title);

        title.setText("Sửa ví");
        if (wallet != null) {
            walletName.setText(wallet.getName());
            walletAmount.setText(String.valueOf(wallet.getAmount()));
            autoCompleteTextView.setText(wallet.getCurrency(), false);
        }
    }

    public void setWalletUpdateListener(WalletUpdateListener listener) {
        this.walletUpdateListener = listener;
    }

}
