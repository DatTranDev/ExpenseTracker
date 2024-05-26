package com.example.expensetracker.viewmodel.addTransactionVM;

import android.content.Context;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ChooseCategoryViewModel extends BaseObservable {

    private final MutableLiveData<List<Category>> listCategory;
    AppUser user;
    public ChooseCategoryViewModel(Context context, String typeTransaction) {
        Type type = new TypeToken<List<Category>>() {}.getType();
        List<Category> list = SharedPreferencesManager.getInstance(context).getList("categories", type);
        Log.d("1",list.toString());
        listCategory = new MutableLiveData<>();
        if(list!=null)
        {
            List<Category> classify = new ArrayList<>();
            if(typeTransaction.equals("spend"))
            {
                for(int i=0; i<list.size();i++)
                {
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
                    if(!list.get(i).getType().equals("Khoản thu") && !list.get(i).getType().equals("Khoản chi"))
                    {
                        classify.add(list.get(i));
                    }
                }
            }
//            for(int i=0; i<classify.size();i++)
//            {
//                String id= classify.get(i).getId();
//                int stt=i+1;
//                for(int j=i+1;j<classify.size();j++)
//                {
//                    if(classify.get(j).getParentCategoryId()==id)
//                    {
//
//                    }
//                }
//            }

            listCategory.setValue(classify);
        }
    }

    public LiveData<List<Category>> getListCategory() {
        return listCategory;
    }


}
