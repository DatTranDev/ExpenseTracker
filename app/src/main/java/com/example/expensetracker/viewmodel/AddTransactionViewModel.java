package com.example.expensetracker.viewmodel;

import android.content.Context;
import android.util.Log;

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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

public class AddTransactionViewModel extends BaseObservable {
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
//    private Timestamp timeTransaction;
    private final MutableLiveData<String> _message = new MutableLiveData<>();
    public LiveData<String> message = _message;
    public AddTransactionViewModel(Context context)
    {

        user=SharedPreferencesManager.getInstance(context).getObject("user", AppUser.class);
    }


    public void showMessage(String msg) {
        _message.setValue(msg);
    }
//    @Bindable
//    public Timestamp getTimeTransaction(){return  timeTransaction;}
//    public void setTimeTransaction(Timestamp a)
//    {
//        this.timeTransaction=a;
//        notifyPropertyChanged(BR.timeTransaction);
//    }

    public synchronized void addTransaction()
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

            TransactionRepository.getInstance().addTransaction(newTransaction, new ApiCallBack<TransactionExp>() {
                @Override
                public void onSuccess(TransactionExp transactionExp) {
                    showMessage("Thêm giao dịch thành công");
                }

                @Override
                public void onError(String message) {
                    Log.d("test","Thất bại");
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

}