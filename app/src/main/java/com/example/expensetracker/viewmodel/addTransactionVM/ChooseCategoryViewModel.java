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
import com.example.expensetracker.view.addTransaction.CategoryAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLSession;

public class ChooseCategoryViewModel extends BaseObservable {

    private final MutableLiveData<List<Category>> listCategory;
    private CategoryAdapter adapter;
    AppUser user;
    public ChooseCategoryViewModel(Context context, String typeTransaction, boolean check) {
        SharedPreferences sharedPreferences= context.getSharedPreferences("categories",Context.MODE_PRIVATE);
        String categoryString= sharedPreferences.getString("categories","null");
        adapter = new CategoryAdapter(context, new ArrayList<>());
        Log.d("1",categoryString);
        listCategory = new MutableLiveData<>();
        if(categoryString!="null")
        {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Category>>() {}.getType();
            List<Category> list = gson.fromJson(categoryString,type);
            Log.d("type",type.toString());
            List<Category> classify = new ArrayList<>();
            if(typeTransaction.equals("spend"))
            {
                for(int i=0; i<list.size();i++)
                {
                    if (check){
                        if (list.get(i).getType().equals("Khoản chi") && list.get(i).getParentCategoryId() == null)
                            classify.add(list.get(i));
                    }
                    else
                    if(list.get(i).getType().equals("Khoản chi"))
                    {
                        classify.add(list.get(i));
                    }
                }
            }
            if(typeTransaction.equals("revenue"))
            {
                for(int i=0; i<list.size();i++)
                {
                    if (check){
                        if (list.get(i).getType().equals("Khoản thu") && list.get(i).getParentCategoryId() == null)
                            classify.add(list.get(i));
                    }
                    else
                    if(list.get(i).getType().equals("Khoản thu"))
                    {
                        classify.add(list.get(i));
                    }
                }
            }
            if(typeTransaction.equals("loan"))
            {
                for(int i=0; i<list.size();i++)
                {
                    if (check){
                        if (!list.get(i).getType().equals("Khoản thu") && !list.get(i).getType().equals("Khoản chi") && list.get(i).getParentCategoryId() == null)
                            classify.add(list.get(i));
                    }
                    else
                    if(!list.get(i).getType().equals("Khoản thu") && !list.get(i).getType().equals("Khoản chi"))
                    {
                        classify.add(list.get(i));
                    }
                }
            }


            listCategory.setValue(classify);
        }
    }

    public LiveData<List<Category>> getListCategory() {
        return listCategory;
    }

}
