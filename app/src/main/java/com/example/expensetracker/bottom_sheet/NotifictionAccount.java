package com.example.expensetracker.bottom_sheet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.R;
import com.example.expensetracker.adapter.WalletShowAdapter;
import com.example.expensetracker.model.Wallet;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class NotifictionAccount extends BottomSheetDialogFragment {

    private ImageView btnCancel;
    private FloatingActionButton btnAdd;


    public NotifictionAccount(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View viewDialog = LayoutInflater.from(getContext()).inflate(R.layout.account_notification, null);
        bottomSheetDialog.setContentView(viewDialog);
        initView(viewDialog);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
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
        //btnAdd = view.findViewById(R.id.account_add_wallet);
        btnCancel = view.findViewById(R.id.notification_back);
        // recyclerView = view.findViewById(R.id.account_wallet_list);
        // recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // walletAdapter = new WalletShowAdapter(wallets, this);
        //total = view.findViewById(R.id.wallet_total_amount);
        // recyclerView.setAdapter(walletAdapter);
    }

}
