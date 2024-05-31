package com.example.expensetracker.viewmodel.accountVM;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isTransactionDeleted = new MutableLiveData<>();

    public LiveData<Boolean> getIsTransactionDeleted() {
        return isTransactionDeleted;
    }

    public void setIsTransactionDeleted(boolean isDeleted) {
        isTransactionDeleted.setValue(isDeleted);
    }
}