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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSession;

public class ChooseCategoryViewModel extends BaseObservable {

    private final MutableLiveData<List<Category>> listCategory;
    AppUser user;
    public ChooseCategoryViewModel(Context context) {
        SharedPreferences sharedPreferences= context.getSharedPreferences("user",Context.MODE_PRIVATE);


        String categoryString= sharedPreferences.getString("categories","null");

        listCategory = new MutableLiveData<>();
        if(categoryString!="null")
        {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Category>>() {}.getType();
            List<Category> list= gson.fromJson(categoryString,type);
            listCategory.setValue(list);
        }


    }

    public LiveData<List<Category>> getListCategory() {
        return listCategory;
    }


}
