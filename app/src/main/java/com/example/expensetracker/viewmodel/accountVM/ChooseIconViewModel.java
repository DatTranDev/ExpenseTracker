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
import com.example.expensetracker.utils.SharedPreferencesManager;
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
        Type type = new TypeToken<List<Icon>>() {}.getType();
        List<Icon> list = SharedPreferencesManager.getInstance(context).getList("icons",type);

        listIcon = new MutableLiveData<>();
        if(list != null)
        {
            listIcon.setValue(list);
        }
    }

    public LiveData<List<Icon>> getListIcon() {
        return listIcon;
    }
}
