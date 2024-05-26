package com.example.expensetracker.bottom_sheet;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.List;

public class NotifictionAccount extends BottomSheetDialogFragment {

    private ImageView btnCancel;
    private SwitchMaterial btn;

    private static final String PREFS_NAME = "my_prefs";

    private static final String TOGGLE_STATE_KEY = "toggle_state";


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

        // Load the saved state from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        boolean isChecked = sharedPreferences.getBoolean(TOGGLE_STATE_KEY, false);
        btn.setChecked(isChecked);

        // Set a listener to save the state when the button is toggled
        btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(TOGGLE_STATE_KEY, isChecked);
                editor.apply();
            }
        });




        return bottomSheetDialog;
    }


    private void initView(View view) {
        btn = view.findViewById(R.id.material_switch);
        btnCancel = view.findViewById(R.id.notification_back);

    }

}
