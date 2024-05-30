package com.example.expensetracker.bottom_sheet;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.expensetracker.R;
import com.example.expensetracker.view.ShowImageActivity;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.utils.Helper;
import com.example.expensetracker.view.ModifyTransactionActivity;
import com.example.expensetracker.viewmodel.TransactionViewModel;
import com.example.expensetracker.viewmodel.WalletViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

public class TransactionDetailsFragment extends BottomSheetDialogFragment {
    private static final String KEY_TRANSACTION = "transaction_info";
    private static final int REQUEST_CODE_MODIFY_TRANSACTION = 100;
    private TransactionExp transactionExp;
    private TextView transactionType, transactionCategory, transactionTime, transactionNote, transactionAmount, walletName;
    private ImageView transactionCurrency, transactionCategoryIcon;
    private LinearLayout wallet;
    private TransactionViewModel transactionViewModel;
    private WalletViewModel walletViewModel;
    private ImageView transactionImage;
    private TextView btnModify;
    private ImageButton btnBack;
    private LinearLayout borrowerLayout;
    private TextView transactionBorrowerName, type;
    private Button btnDelete;

    public static TransactionDetailsFragment newInstance(TransactionExp transactionExp) {
        TransactionDetailsFragment transactionDetailsFragment = new TransactionDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_TRANSACTION, transactionExp);
        transactionDetailsFragment.setArguments(bundle);

        return transactionDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundleReceiver = getArguments();
        if (bundleReceiver != null) {
            transactionExp = bundleReceiver.getParcelable(KEY_TRANSACTION);
        }
        transactionViewModel = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        walletViewModel = new ViewModelProvider(requireActivity()).get(WalletViewModel.class);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_transaction_details, null);
        bottomSheetDialog.setContentView(viewDialog);
        initView(viewDialog);
        setTransactionData();

        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                int maxHeight = getResources().getDisplayMetrics().heightPixels;
                maxHeight = maxHeight - maxHeight / 8;

                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.height = maxHeight;
                    bottomSheet.setLayoutParams(layoutParams);
                }
            }
        });

        btnModify.setOnClickListener(v -> modifyTransaction(transactionExp));
        btnBack.setOnClickListener(v -> dismiss());
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = transactionExp.getUserId();
                transactionViewModel.deleteTransaction(transactionExp, getContext());
                transactionViewModel.loadTransactions(userId, getContext());
                dismiss();

            }
        });

        return bottomSheetDialog;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_MODIFY_TRANSACTION && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null && data.hasExtra("updatedTransaction")) {
                Gson gson = new Gson();
                TransactionExp updatedTransaction = gson.fromJson(data.getStringExtra("updatedTransaction"), TransactionExp.class);
                if (updatedTransaction != null) {
                    transactionExp = updatedTransaction;
                    setTransactionData();
                    transactionViewModel.loadTransactions(transactionExp.getUserId(), getContext());
                    walletViewModel.loadWallets(transactionExp.getUserId(), getContext());
                }
            }
        }
    }

    private void modifyTransaction(TransactionExp transactionExp) {
        Intent intent = new Intent(getActivity(), ModifyTransactionActivity.class);
        Gson gson = new Gson();
        String transactionJson = gson.toJson(transactionExp);
        intent.putExtra("transaction", transactionJson);
        startActivityForResult(intent, REQUEST_CODE_MODIFY_TRANSACTION);
    }

    private void initView(View view) {
        transactionType = view.findViewById(R.id.transaction_type);
        transactionCategory = view.findViewById(R.id.transaction_category);
        transactionTime = view.findViewById(R.id.transaction_time);
        transactionNote = view.findViewById(R.id.transaction_note);
        transactionAmount = view.findViewById(R.id.transaction_price);
        walletName = view.findViewById(R.id.wallet_name);
        transactionCurrency = view.findViewById(R.id.transaction_currency);
        transactionCategoryIcon = view.findViewById(R.id.transaction_category_icon);
        wallet = view.findViewById(R.id.wallet);
        btnModify = view.findViewById(R.id.modify_transaction);
        btnBack = view.findViewById(R.id.btnBack);
        transactionImage = view.findViewById(R.id.transaction_image);
        btnDelete = view.findViewById(R.id.btnDelete);
        borrowerLayout = view.findViewById(R.id.borrower_layout);
        transactionBorrowerName = view.findViewById(R.id.name_borrower);
        type = view.findViewById(R.id.type);
    }

    private void setTransactionData() {
        if (transactionExp != null) {
            if (transactionExp.getCategory() != null) {
                transactionType.setText(transactionExp.getCategory().getType());
                transactionCategory.setText(transactionExp.getCategory().getName());
                String iconName = transactionExp.getCategory().getIcon().getLinking();
                int iconId = getContext().getResources().getIdentifier(iconName, "drawable", getContext().getPackageName());
                transactionCategoryIcon.setImageResource(iconId);

                if (!transactionExp.getCategory().getType().equals("Khoản thu") && !transactionExp.getCategory().getType().equals("Khoản chi")) {
                    borrowerLayout.setVisibility(View.VISIBLE);
                    transactionBorrowerName.setText(transactionExp.getPartner());
                    switch (transactionExp.getCategory().getType()) {
                        case "Đi vay":
                            type.setText("Người vay");
                            break;
                        case "Cho vay":
                            type.setText("Người cho vay");
                            break;
                        case "Thu nợ":
                            type.setText("Người thu nợ");
                            break;
                        case "Trả nợ":
                            type.setText("Người vay nợ");
                            break;
                        default:
                            type.setText("");
                            break;
                    }
                } else {
                    borrowerLayout.setVisibility(View.GONE);
                }
            } else {
                transactionType.setText(transactionExp.getCategory().getType());
                transactionCategory.setText(transactionExp.getCategory().getName());
                int iconId = getContext().getResources().getIdentifier("error", "drawable", getContext().getPackageName());
                transactionCategoryIcon.setImageResource(iconId);
            }

            transactionTime.setText(Helper.formatDate(transactionExp.getCreatedAt()));
            transactionNote.setText(transactionExp.getNote() != null ? transactionExp.getNote() : "");
            transactionAmount.setText(Helper.formatCurrency(transactionExp.getSpend()));
            walletName.setText(transactionExp.getWallet() != null ? transactionExp.getWallet().getName() : "");

            if (transactionExp.getImage() != null) {
                transactionImage.setVisibility(View.VISIBLE);
                Glide.with(this)
                        .load(transactionExp.getImage())
                        .apply(new RequestOptions()
                                .error(R.drawable.error)
                        )
                        .into(transactionImage);

                transactionImage.setOnClickListener(v -> {
                    Intent intent = new Intent(getActivity(), ShowImageActivity.class);
                    intent.putExtra("image_url", transactionExp.getImage());
                    startActivity(intent);
                });
            } else {
                transactionImage.setVisibility(View.GONE);
            }
        }
    }
}
