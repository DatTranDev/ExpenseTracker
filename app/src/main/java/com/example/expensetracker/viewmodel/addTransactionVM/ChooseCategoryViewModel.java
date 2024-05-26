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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLSession;

public class ChooseCategoryViewModel extends BaseObservable {

    private final MutableLiveData<List<Category>> listCategory;
    private CategoryAdapter adapter;
    AppUser user;
    public ChooseCategoryViewModel(Context context, String typeTransaction, boolean check) {
        SharedPreferences sharedPreferences= context.getSharedPreferences("categories",Context.MODE_PRIVATE);
        String categoryString= sharedPreferences.getString("categories","null");
        Log.d("1",categoryString);
        listCategory = new MutableLiveData<>();
        if(categoryString!="null")
        {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Category>>() {}.getType();
            List<Category> list = gson.fromJson(categoryString,type);

            List<Category> sortedCategories = new ArrayList<>();
            Map<String, Category> parentMap = new HashMap<>();
            Map<String, List<Category>> childMap = new HashMap<>();

            // Phân loại các category vào parentMap và childMap
            for (int i=0; i<list.size();i++) {
                if (list.get(i).getParentCategoryId()==null) {
                    parentMap.put(list.get(i).getId(), list.get(i));
                } else {
                    childMap.computeIfAbsent(list.get(i).getParentCategoryId(), k -> new ArrayList<>()).add(list.get(i));
                }
            }

            // Sắp xếp các parent category và thêm vào sortedCategories
            for (Map.Entry<String, Category> entry : parentMap.entrySet()) {
                Category parent = entry.getValue();
                sortedCategories.add(parent);

                // Thêm các child category tương ứng nếu có
                List<Category> children = childMap.get(parent.getId());
                if (children != null) {
                    sortedCategories.addAll(children);
                }
            }

            list = sortedCategories;

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
