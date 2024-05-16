package com.example.expensetracker.bottom_sheet;

import com.example.expensetracker.model.Wallet;

public interface WalletUpdateListener {
    void onWalletAdded(Wallet wallet);
    void onWalletUpdated(Wallet wallet);
    void onWalletDeleted(String walletId);
}
