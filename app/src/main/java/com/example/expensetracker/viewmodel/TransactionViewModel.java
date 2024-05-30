package com.example.expensetracker.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class TransactionViewModel extends ViewModel {
    private MutableLiveData<List<TransactionExp>> transactionsLiveData;
    private List<TransactionExp> transactionList;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<String> errorMessageLiveData = new MutableLiveData<>();
    private AppUserRepository appUserRepository;

    public TransactionViewModel() {
        transactionList = new ArrayList<>();
        transactionsLiveData = new MutableLiveData<>();
        transactionsLiveData.setValue(transactionList);
        isLoading = new MutableLiveData<>();
        appUserRepository = AppUserRepository.getInstance();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public LiveData<List<TransactionExp>> getTransactionsLiveData() {
        return transactionsLiveData;
    }

    public LiveData<String> getErrorMessageLiveData() {
        return errorMessageLiveData;
    }

    public void loadTransactions(String userId) {
        isLoading.setValue(true);
        appUserRepository.getTransaction(userId, new ApiCallBack<List<TransactionExp>>() {
            @Override
            public void onSuccess(List<TransactionExp> transactions) {
                transactionList = transactions;
                transactionsLiveData.setValue(transactionList);
                isLoading.setValue(false);
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
                isLoading.setValue(false);
            }
        });
    }

    public void loadIsSharingTransactions(String userId) {
        isLoading.setValue(true);
        appUserRepository.getTransaction(userId, new ApiCallBack<List<TransactionExp>>() {
            @Override
            public void onSuccess(List<TransactionExp> transactions) {
                List<TransactionExp> transactionExpsisSharing = new ArrayList<>();
                for (TransactionExp exp : transactions) {
                    Wallet wallet = new Wallet();
                    wallet.setId(exp.getWalletId());
                    if (wallet.isSharing()) {
                        transactionExpsisSharing.add(exp);
                    }
                }
                transactionList = transactionExpsisSharing;
                transactionsLiveData.setValue(transactionList);
                isLoading.setValue(false);
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
                isLoading.setValue(false);
            }
        });
    }

    public void addTransaction(TransactionExp transaction, Context context) {
        TransactionRepository.getInstance().addTransaction(transaction, new ApiCallBack<TransactionExp>() {
            @Override
            public void onSuccess(TransactionExp addedTransaction) {
                transactionList.add(addedTransaction);
                transactionsLiveData.setValue(new ArrayList<>(transactionList));
                Toast.makeText(context, "Thêm ví thành công!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }

    public void updateTransaction(TransactionExp transaction) {
        TransactionRepository.getInstance().updateTransaction(transaction.getId(), transaction, new ApiCallBack<TransactionExp>() {
            @Override
            public void onSuccess(TransactionExp updatedTransaction) {
                List<TransactionExp> currentTransactions = transactionsLiveData.getValue();
                if (currentTransactions != null) {
                    for (int i = 0; i < currentTransactions.size(); i++) {
                        if (currentTransactions.get(i).getId().equals(updatedTransaction.getId())) {
                            currentTransactions.set(i, updatedTransaction);
                            break;
                        }
                    }
                    transactionsLiveData.setValue(currentTransactions);
                }
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }

    public void deleteTransaction(TransactionExp transaction, Context context) {
        TransactionRepository.getInstance().deleteTransaction(transaction.getId(), new ApiCallBack<TransactionExp>() {
            @Override
            public void onSuccess(TransactionExp deletedTransaction) {
                List<TransactionExp> currentTransactions = transactionsLiveData.getValue();
                if (currentTransactions != null) {
                    transactionsLiveData.setValue(currentTransactions);
                    Toast.makeText(context, "Xóa giao dịch thành công", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String message) {
                errorMessageLiveData.setValue(message);
            }
        });
    }
}
