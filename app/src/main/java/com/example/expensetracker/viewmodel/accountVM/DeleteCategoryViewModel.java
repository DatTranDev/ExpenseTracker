package com.example.expensetracker.viewmodel.accountVM;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Category.CategoryReq;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Icon;
import com.example.expensetracker.model.UserCategory;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DeleteCategoryViewModel extends BaseObservable {
    Context context;
    public ObservableField<Category> category= new ObservableField<>();
    public ObservableField<String> nameCategory = new ObservableField<>();

    public ObservableField<String> type = new ObservableField<>();
    public ObservableField<Icon> iconCategory = new ObservableField<>();

    public  ObservableField<String> parentCategory = new ObservableField<>();

    public MutableLiveData<Boolean> btnEnabled = new MutableLiveData<>(false);
    public AppUser user;

    public MutableLiveData<String> get_message() {
        return _message;
    }

    private final MutableLiveData<String> _message = new MutableLiveData<>();
    public LiveData<String> message = _message;

    private MutableLiveData<List<Category>> categories;
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public void showMessage(String msg) {
        _message.setValue(msg);
    }

    public  DeleteCategoryViewModel(Context context)
    {
        super();
        sharedPreferences= context.getSharedPreferences("user",Context.MODE_PRIVATE);
        String userString= sharedPreferences.getString("user","null");

        if(userString!="null")
        {
            gson = new Gson();
            user= gson.fromJson(userString, AppUser.class);
        }
        categories = new MutableLiveData<>(getCategoriesFromSharedPreferences());

    }
    public synchronized void deleteCategory(){
        UserCategory userCategory = new UserCategory(user.getId(), category.get().getId(), false);
        CategoryRepository.getInstance().deleteCategory(userCategory, new ApiCallBack<Category>() {
            @Override
            public synchronized void onSuccess(Category category) {
                List<Category> currentCategories = categories.getValue();
                if (currentCategories != null) {
                    // Remove the category from the list
                    currentCategories.remove(category);
                    categories.setValue(currentCategories);
                    saveCategoriesToSharedPreferences(currentCategories);
                    showMessage("Xóa danh mục thành công");
                    resetData();
                }
            }
            @Override
            public void onError(String message) {
                showMessage("Xóa danh mục thất bại");
            }
        });
    }


    private void saveCategoriesToSharedPreferences(List<Category> categories) {
        SharedPreferencesManager.getInstance(context).saveList("categories",categories);
    }

    private synchronized List<Category> getCategoriesFromSharedPreferences() {
        Type type = new TypeToken<List<Category>>() {}.getType();
        List<Category> list = SharedPreferencesManager.getInstance(context).getList("categories", type);
//        String json = sharedPreferences.getString("categories", null);
//
//        String categoryString= sharedPreferences.getString("categories","null");
//        if(categoryString!="null") {
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<Category>>() {
//            }.getType();
//
//            list = gson.fromJson(categoryString, type);
//        }
        return list;
    }

    public void resetData(){
        nameCategory.set(null);
        parentCategory.set(null);
        iconCategory.set(null);
    }
}
