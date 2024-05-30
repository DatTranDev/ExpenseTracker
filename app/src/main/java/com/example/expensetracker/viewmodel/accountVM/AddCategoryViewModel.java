package com.example.expensetracker.viewmodel.accountVM;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Category.CategoryReq;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.model.Category;
import com.example.expensetracker.model.Icon;
import com.example.expensetracker.model.Wallet;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddCategoryViewModel extends BaseObservable {
    Context context;
    public ObservableField<Category> category= new ObservableField<>();
    public ObservableField<String> nameCategory = new ObservableField<>();

    public ObservableField<String> type = new ObservableField<>();
    public ObservableField<Icon> iconCategory = new ObservableField<>();

    public  ObservableField<Category> parentCategory = new ObservableField<>();
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

    public  AddCategoryViewModel(Context context)
    {
        super();
        user = SharedPreferencesManager.getInstance(context).getObject("user", AppUser.class);
        categories = new MutableLiveData<>(getCategoriesFromSharedPreferences());

    }
    public synchronized void addCategory(){

        if(nameCategory.get()==null || nameCategory.get().equals("") || user==null || iconCategory.get() == null )
        {
            Log.e("Test: ", "Success");
            if(nameCategory.get()==null){
                showMessage("Vui lòng nhập tên danh mục mới");
                return;
            }
            if(iconCategory.get()==null)
            {
                showMessage("Vui lòng chọn icon cho danh mục");
                return;
            }
        }
        else {
            try
            {
                CategoryReq categoryReq;
                if (parentCategory.get() == null)
                    categoryReq = new CategoryReq(user.getId(), nameCategory.get().toString(), Objects.requireNonNull(iconCategory.get()).getId(), null,type.get().toString(),false );
                else
                    categoryReq = new CategoryReq(user.getId(), nameCategory.get().toString(), Objects.requireNonNull(iconCategory.get()).getId(), parentCategory.get().getId(),type.get().toString(),false );

                CategoryRepository.getInstance().addCategory(categoryReq, new ApiCallBack<Category>() {
                    @Override
                    public synchronized void onSuccess(Category category) {
                        List<Category> currentCategories = categories.getValue();
                        if (currentCategories != null) {
                            currentCategories.add(category);
                            categories.setValue(currentCategories);
                            saveCategoriesToSharedPreferences(currentCategories);
                            showMessage("Thêm danh mục thành công");
                            resetData();
                        }
                    }
                    @Override
                    public void onError(String message) {
                        showMessage(message);
                    }
                });
            }
            catch (Exception ex){
                Log.e("Exception: ", ex.getMessage());
                return;
            }

        }
    }

    private synchronized void saveCategoriesToSharedPreferences(List<Category> categories) {
        SharedPreferencesManager.getInstance(context).saveList("categories",categories);
    }

    private synchronized List<Category> getCategoriesFromSharedPreferences() {
        Type type = new TypeToken<List<Category>>() {}.getType();
        List<Category> list = SharedPreferencesManager.getInstance(context).getList("categories", type);
//        List<Category> list = new ArrayList<>();
//        String categoryString= sharedPreferences.getString("categories","null");
//        if(categoryString!="null") {
//            Gson gson = new Gson();
//            Type type = new TypeToken<List<Category>>() {
//            }.getType();
//            list = gson.fromJson(categoryString, type);
//        }
        return list;
    }

    public void resetData(){
        nameCategory.set(null);
        parentCategory.set(null);
        iconCategory.set(null);
    }
    public boolean isFormValid(){
        if(nameCategory.get() ==null ||  nameCategory.get().equals(""))
        {
            return false;
        }
        return true;
    }
}
