package com.example.expensetracker.viewmodel.addTransactionVM;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.repository.AppUserRepository;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSession;

public class ChooseCategoryViewModel extends BaseObservable {

    private final MutableLiveData<List<Category>> listCategory;
    AppUser user;
    public ChooseCategoryViewModel(Context context) {
        SharedPreferences sharedPreferences= context.getSharedPreferences("user",Context.MODE_PRIVATE);


        String userString= sharedPreferences.getString("user","null");

        if(userString!="null")
        {
            Gson gson = new Gson();
            user= gson.fromJson(userString,AppUser.class);
        }
        listCategory = new MutableLiveData<>();
        // Thêm dữ liệu vào danh sách
        List<Category> items ;
        AppUserRepository.getInstance().getCategory(user.getId(), new ApiCallBack<List<Category>>() {
            @Override
            public void onSuccess(List<Category> categories) {

                listCategory.setValue(categories);
                Log.i("test",listCategory.getValue().get(0).getName());

            }
            @Override
            public void onError(String message) {
                Log.i("test"," lấy danh mục lỗi");
            }
        });


    }

    public LiveData<List<Category>> getListCategory() {
        return listCategory;
    }


}
