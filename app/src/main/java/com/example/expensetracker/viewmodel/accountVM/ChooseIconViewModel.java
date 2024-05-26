package com.example.expensetracker.viewmodel.accountVM;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Icon;
import com.example.expensetracker.repository.AppUserRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSession;
public class ChooseIconViewModel extends BaseObservable{
    private final MutableLiveData<List<Icon>> listIcon;
    AppUser user;
    public ChooseIconViewModel(Context context) {
        SharedPreferences sharedPreferences= context.getSharedPreferences("icons",Context.MODE_PRIVATE);
        String iconString= sharedPreferences.getString("icons","null");
        Log.d("1",iconString);
        listIcon = new MutableLiveData<>();
        if(iconString!="null")
        {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Category>>() {}.getType();
            List<Icon> list = gson.fromJson(iconString,type);
            Log.d("type",type.toString());

            listIcon.setValue(list);
        }
    }

    public LiveData<List<Icon>> getListIcon() {
        return listIcon;
    }
}
