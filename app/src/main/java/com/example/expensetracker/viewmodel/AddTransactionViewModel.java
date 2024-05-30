package com.example.expensetracker.viewmodel;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.bumptech.glide.Glide;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Upload.UploadResponse;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.TransactionRepository;
import com.example.expensetracker.repository.UploadRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.example.expensetracker.view.addTransaction.mainAddActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddTransactionViewModel extends BaseObservable {
    private Context context;
    BigDecimal spend;
    int flag=0;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private AppUser user;
    public ObservableField<Category> category = new ObservableField<>();
    public ObservableField<Wallet> wallet = new ObservableField<>();
    public ObservableField<String> node = new ObservableField<>("");
    public ObservableField<String> borrower = new ObservableField<>("");
    public ObservableField<String> money = new ObservableField<>();
    //    public ObservableField<String> partner= new ObservableField<>("");
    public ObservableField<String> image = new ObservableField<>("");
    public ObservableField<Timestamp> timeTransaction = new ObservableField<>();
    //    private Timestamp timeTransaction;
    private final MutableLiveData<String> _message = new MutableLiveData<>();
    public LiveData<String> message = _message;
    public List<TransactionExp> listTransaction= new ArrayList<>();

    public AddTransactionViewModel(Context context) {

        user = SharedPreferencesManager.getInstance(context).getObject("user", AppUser.class);
        Type type = new TypeToken<List<TransactionExp>>() {}.getType();
        listTransaction=SharedPreferencesManager.getInstance(context).getList("transactions",type);
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

    public synchronized void addTransaction() {
        showMessage("Start");
        final Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
//        calendar.set(Calendar.MILLISECOND, 0);
        Timestamp currentDate = new Timestamp(calendar.getTimeInMillis());
        if (timeTransaction.get() == null) {
            timeTransaction.set(currentDate);
        } else {
            int compare = timeTransaction.get().compareTo(currentDate);
            if (compare >= 1) {
                showMessage("Ngày giao dịch không hợp lệ");
                return;
            }
        }
        if (wallet.get() != null && money.get() != null && category.get() != null && user != null) {


            String cleanString = money.get().toString().replaceAll("[,]", "");
            spend = new BigDecimal(cleanString);

            if (category.get().getType().equals("Khoản chi") || category.get().getType().equals("Cho vay") || category.get().getType().equals("Trả nợ")) {
                int compare = spend.compareTo(wallet.get().getAmount());
                if (compare > 0) {
                    showMessage("Số tiền đã vượt quá số tiền trong ví");
                    return;
                }
            }
            if(!image.get().equals("") || image.get()==null)
            {
                uploadImage(image.get());
            }
            else {
                TransactionExp newTransaction = new TransactionExp();
                newTransaction.setUserId(user.getId());
                newTransaction.setCategoryId(category.get().getId());
                newTransaction.setWalletId(wallet.get().getId());
                newTransaction.setSpend((BigDecimal) spend);
                newTransaction.setNote(node.get());
                newTransaction.setCurrency("VND");
                newTransaction.setPartner(borrower.get());
                newTransaction.setCreatedAt(timeTransaction.get());
                newTransaction.setImage(image.get());
                newTransaction.setCategory(category.get());

                TransactionRepository.getInstance().addTransaction(newTransaction, new ApiCallBack<TransactionExp>() {
                    @Override
                    public void onSuccess(TransactionExp transactionExp) {
                        newTransaction.setId(transactionExp.getId());
                        listTransaction.add(newTransaction);
                        SharedPreferencesManager.getInstance(context).removeKey("transactions");
                        SharedPreferencesManager.getInstance(context).saveList("transactions", listTransaction);
                        showMessage("Thêm giao dịch thành công");

                    }

                    @Override
                    public void onError(String message) {
                        showMessage("Thêm giao dịch thất bại");
                    }
                });

            }

        } else {
            if (money.get() == null) {
                showMessage("Vui lòng nhập số tiền của giao dịch");
                return;
            }
            if (category.get() == null) {
                showMessage("Vui lòng chọn danh mục");
                return;
            }
            if (wallet.get() == null) {
                showMessage("Vui lòng chọn ví để sử dụng");
                return;
            }
        }

    }

    public synchronized void uploadImage(String imageString) {

        if (imageString != null) {
            UploadRepository.getInstance().uploadImage(imageString, new ApiCallBack<UploadResponse>() {
                @Override
                public void onSuccess(UploadResponse uploadResponse) {
                    String imageString = uploadResponse.getImage();
                    image.set(imageString);
                    TransactionExp newTransaction = new TransactionExp();
                    newTransaction.setUserId(user.getId());
                    newTransaction.setCategoryId(category.get().getId());
                    newTransaction.setWalletId(wallet.get().getId());
                    newTransaction.setSpend((BigDecimal) spend);
                    newTransaction.setNote(node.get());
                    newTransaction.setCurrency("VND");
                    newTransaction.setPartner(borrower.get());
                    newTransaction.setCreatedAt(timeTransaction.get());
                    newTransaction.setImage(image.get());
                    newTransaction.setCategory(category.get());

                    TransactionRepository.getInstance().addTransaction(newTransaction, new ApiCallBack<TransactionExp>() {
                        @Override
                        public void onSuccess(TransactionExp transactionExp) {
                            newTransaction.setId(transactionExp.getId());
                            listTransaction.add(newTransaction);
                            SharedPreferencesManager.getInstance(context).saveList("transactions",listTransaction);
                            showMessage("Thêm giao dịch thành công");

                        }

                        @Override
                        public void onError(String message) {
                            showMessage("Thêm giao dịch thất bại");
                        }
                    });

                }

                @Override
                public void onError(String message) {
                    showMessage("Thêm giao dịch thất bại do không thêm được ảnh");
                }
            });
        }
    }
}