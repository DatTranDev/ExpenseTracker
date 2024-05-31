package com.example.expensetracker.bottom_sheet;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.multidex.BuildConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.TransactionAdapter;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.exportfile.PDFExporter;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
public class ExportFileAccount extends BottomSheetDialogFragment implements TransactionAdapter.OnItemClickListener{

    private static final String KEY_WALLET_LIST = "wallet_list";
    private Button pdfButton;

    private ImageButton btnback;

    private TransactionAdapter transactionAdapter;

    private List<TransactionExp> allTransactions = new ArrayList<>();

    private LinearLayout transactionEmpty;
    private ProgressBar progressBar;
    private View overlay;
    private Boolean isLoading;
    public ExportFileAccount(){}


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            allTransactions = getArguments().getParcelableArrayList(KEY_WALLET_LIST);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.account_export, null);
        bottomSheetDialog.setContentView(viewDialog);

        AppUser user = SharedPreferencesManager.getInstance(getActivity()).getObject("user", AppUser.class);
        initView(viewDialog);

        isLoading = true;

        if (isLoading)
        {
            overlay.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }
        bottomSheetDialog.setOnShowListener(dialog -> {
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            FrameLayout bottomSheet = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                int maxHeight = getResources().getDisplayMetrics().heightPixels;
                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                if (layoutParams != null) {
                    layoutParams.height = maxHeight;
                    bottomSheet.setLayoutParams(layoutParams);
                }
            }
        });


        MainActivity mainActivity = (MainActivity)getActivity();
        RecyclerView rvTransaction = viewDialog.findViewById(R.id.account_transaction_export);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mainActivity);
        rvTransaction.setLayoutManager(linearLayoutManager);
        transactionAdapter = new TransactionAdapter(getContext(),allTransactions, this);
        getTransactionsForUser(user.getId());

        rvTransaction.setAdapter(transactionAdapter);
        pdfButton.setOnClickListener(v -> {
            String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/transactions.pdf";
            PDFExporter.exportToPDF(getContext(), allTransactions);

            Toast.makeText(getContext(), "PDF exported in downloads", Toast.LENGTH_SHORT).show();
        });

        btnback.setOnClickListener(v -> bottomSheetDialog.dismiss());
        return bottomSheetDialog;
    }

    private void getTransactionsForUser(String userId) {
        AppUserRepository repository = AppUserRepository.getInstance();
        repository.getTransaction(userId, new ApiCallBack<List<TransactionExp>>() {
            @Override
            public void onSuccess(List<TransactionExp> transactions) {
                isLoading = false;
                allTransactions = transactions;
                transactionAdapter.updateTransaction(allTransactions);
                transactionAdapter.notifyDataSetChanged();
                if (!allTransactions.isEmpty()) {
                    transactionEmpty.setVisibility(View.GONE);
                } else {
                    transactionEmpty.setVisibility(View.VISIBLE);
                }
                overlay.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onError(String errorMessage) {

            }
        });
    }

    private void initView(View view) {
        pdfButton = view.findViewById(R.id.account_pdf);
        btnback = view.findViewById(R.id.back);
        transactionEmpty = view.findViewById(R.id.export_empty);
        progressBar = view.findViewById(R.id.progress_bar);
        overlay = view.findViewById(R.id.overlay);
    }

    @Override
    public void onItemClick(TransactionExp transactionExp) {
        //TransactionDetailsFragment transactionDetailsFragment = TransactionDetailsFragment.newInstance(transactionExp);
        //transactionDetailsFragment.show(getActivity().getSupportFragmentManager(), transactionDetailsFragment.getTag());
    }


}
