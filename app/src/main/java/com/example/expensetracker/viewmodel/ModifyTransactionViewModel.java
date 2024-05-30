package com.example.expensetracker.viewmodel;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.TransactionRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ModifyTransactionViewModel extends BaseObservable {
    private Context context;
    private AppUser user;
    public ObservableField<Category> category = new ObservableField<>();
    public ObservableField<Wallet> wallet = new ObservableField<>();
    public ObservableField<String> node= new ObservableField<>("");
    public ObservableField<String> borrower= new ObservableField<>("");
    public ObservableField<String> money= new ObservableField<>();
    public ObservableField<String> partner= new ObservableField<>("");
    public ObservableField<String> image = new ObservableField<>("");
    public ObservableField<Timestamp> timeTransaction= new ObservableField<>();
    private final MutableLiveData<String> _message = new MutableLiveData<>();
    public List<TransactionExp> listTransaction = new ArrayList<>();
    public LiveData<String> message = _message;
    public ModifyTransactionViewModel(Context context)
    {
        user = SharedPreferencesManager.getInstance(context).getObject("user", AppUser.class);
        Type type = new TypeToken<List<TransactionExp>>() {}.getType();
        listTransaction = SharedPreferencesManager.getInstance(context).getList("transactions", type);
    }

    public void setData(TransactionExp transaction) {
        if (transaction != null) {
            category.set(transaction.getCategory());
            wallet.set(transaction.getWallet());
            node.set(transaction.getNote());
            borrower.set(transaction.getPartner());
            money.set(transaction.getSpend().toString());
            image.set(transaction.getImage());
            timeTransaction.set(transaction.getCreatedAt());
        }
    }

    public void showMessage(String msg) {
        _message.setValue(msg);
    }

    public void setTimeTransaction(Timestamp timestamp) {
        timeTransaction.set(timestamp);
    }

    public void modifyTransaction(TransactionExp transactionExp)
    {
        if(timeTransaction.get()==null)
        {
            final Calendar calendar = Calendar.getInstance();
            calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH),0,0,0);
            calendar.set(Calendar.MILLISECOND, 0);
            timeTransaction.set( new Timestamp(calendar.getTimeInMillis()));
        }
        if(wallet.get()!=null && money.get()!=null && category.get()!=null && user!=null)
        {
            BigDecimal spend ;
            String cleanString = money.get().toString().replaceAll("[,]", "");
            spend=new BigDecimal(cleanString);
            int compare= spend.compareTo(wallet.get().getAmount());
            if(compare>0)
            {
                showMessage("Số tiền đã vượt quá số tiền trong ví");
                return;
            }
            TransactionExp newTransaction= new TransactionExp();
            newTransaction.setUserId(user.getId());
            newTransaction.setCategoryId(category.get().getId());
            newTransaction.setWalletId(wallet.get().getId());
            newTransaction.setSpend((BigDecimal) spend);
            newTransaction.setNote(node.get());
            newTransaction.setCurrency("VND");
            newTransaction.setPartner(borrower.get());
            newTransaction.setCreatedAt(timeTransaction.get());
            newTransaction.setImage(image.get());

            TransactionRepository.getInstance().updateTransaction(transactionExp.getId(), newTransaction, new ApiCallBack<TransactionExp>() {
                @Override
                public void onSuccess(TransactionExp transaction) {
                    newTransaction.setId(transactionExp.getId());
                    for (int i = 0; i < listTransaction.size(); i++) {
                        if (listTransaction.get(i).getId() == newTransaction.getId()) {
                            listTransaction.set(i, newTransaction);
                            break;
                        }
                    }
                    SharedPreferencesManager.getInstance(context).removeKey("transactions");
                    SharedPreferencesManager.getInstance(context).saveList("transactions", listTransaction);
                    _message.setValue("Sửa giao dịch thành công!");
                }

                @Override
                public void onError(String message) {
                    _message.setValue("Sửa giao dịch thất bại!");
                }
            });


        }
        else {
            if(money.get()==null){
                showMessage("Vui lòng nhập số tiền của giao dịch");
            }
            if(category.get()==null)
            {
                showMessage("Vui lòng chọn danh mục");
            }
            if(wallet.get()==null)
            {
                showMessage("Vui lòng chọn ví để sử dụng");
            }
        }

    }

    public TransactionExp getUpdatedTransaction(TransactionExp transactionExp) {
        transactionExp.setUserId(user.getId());
        transactionExp.setCategoryId(category.get().getId());
        transactionExp.setWallet(wallet.get());
        transactionExp.setSpend(new BigDecimal(money.get().replaceAll("[,]", "")));
        transactionExp.setNote(node.get());
        transactionExp.setCurrency("VND");
        transactionExp.setPartner(borrower.get());
        transactionExp.setCreatedAt(timeTransaction.get());
        transactionExp.setImage(image.get());
        return transactionExp;
    }


}