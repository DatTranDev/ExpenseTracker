package com.example.expensetracker.viewmodel.addTransactionVM;

import android.content.Context;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Category;

import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.view.addTransaction.CategoryAdapter;
import com.google.gson.Gson;



import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseCategoryViewModel extends BaseObservable {
    private MutableLiveData<List<Category>> listCategory;
    private Context context;
    private boolean check;
    private List<Category> Cate;
    private ObservableField<String> typeTransaction = new ObservableField<>();
    public void setTypeTransaction(String type) {
        this.typeTransaction.set(type);
        printList();
    }
//    public ChooseCategoryViewModel() {
//        Cate = new ArrayList<>();
//        listCategory = new MutableLiveData<>();
//        listCategory.setValue(Cate);
//
//    }

    public ChooseCategoryViewModel(Context context, boolean check) {
        this.context = context;
        this.check = check;
    }

    public synchronized void printList()
    {
        Type type = new TypeToken<List<Category>>() {}.getType();
        List<Category> list = SharedPreferencesManager.getInstance(context).getList("categories", type);
        listCategory = new MutableLiveData<>();
        if(list!=null)
        {
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
            if(typeTransaction.get().equals("spend"))
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
            if(typeTransaction.get().equals("revenue"))
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
            if(typeTransaction.get().equals("loan"))
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



    public Category getParent(String id){
        if (listCategory.getValue() != null) {
            for (Category category : listCategory.getValue()) {
                if (category.getId().equals(id)) {
                    return category;
                }
            }
        }
        return null;
    }

    public LiveData<List<Category>> getListCategory() {
        return listCategory;
    }

}
