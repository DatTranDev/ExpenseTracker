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
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.repository.CategoryRepository;
import com.example.expensetracker.utils.SharedPreferencesManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DeleteCategoryViewModel extends BaseObservable {
    Context context;
    public ObservableField<Category> category= new ObservableField<>();
    public ObservableField<String> nameCategory = new ObservableField<>();

    public ObservableField<String> type = new ObservableField<>();
    public ObservableField<Icon> iconCategory = new ObservableField<>();

    public  ObservableField<String> parentCategory = new ObservableField<>();
    public ObservableField<Category> parentCate= new ObservableField<>();

    public MutableLiveData<Boolean> btnEnabled = new MutableLiveData<>(false);
    public AppUser user;

    public MutableLiveData<String> get_message() {
        return _message;
    }

    private final MutableLiveData<String> _message = new MutableLiveData<>();
    public LiveData<String> message = _message;

    private MutableLiveData<List<Category>> categories;

    public void showMessage(String msg) {
        _message.setValue(msg);
    }

    public  DeleteCategoryViewModel(Context context)
    {
        super();
        user = SharedPreferencesManager.getInstance(context).getObject("user", AppUser.class);
        categories = new MutableLiveData<>(getCategoriesFromSharedPreferences());

    }
    public synchronized void deleteCategory(){
        UserCategory userCategory = new UserCategory(user.getId(), category.get().getId(), false);
        Category categoryRemove= category.get();
        CategoryRepository.getInstance().deleteCategory(userCategory, new ApiCallBack<Category>() {
            @Override
            public synchronized void onSuccess(Category category) {
                List<Category> currentCategories = categories.getValue();
                if (currentCategories != null) {
                    // Remove the category from the list
                    for(int i=0;i<currentCategories.size();i++)
                    {
                        if(currentCategories.get(i).getId().equals(categoryRemove.getId()))
                        {
                            currentCategories.remove(i);
                            break;
                        }
                    }
                    categories.setValue(currentCategories);
                    saveCategoriesToSharedPreferences(currentCategories);
                    showMessage("Xóa danh mục thành công");
                    resetData();
                }
            }
            @Override
            public void onError(String message) {
                showMessage(message);
            }
        });
    }

    public synchronized void modifyCategory()
    {
        if (category.get().isPublic() == true)
        {
            showMessage("Danh mục này không thể chỉnh sửa");
        }
        else {
            CategoryReq categoryReq;
            if (parentCate.get().getId() == null){
                categoryReq = new CategoryReq(user.getId(), nameCategory.get().toString(), Objects.requireNonNull(iconCategory.get()).getId(), null,type.get().toString(),false );
            }
            categoryReq = new CategoryReq(user.getId(), nameCategory.get().toString(), Objects.requireNonNull(iconCategory.get()).getId(), parentCate.get().getId(),type.get().toString(),false );
            Category categoryRemove = category.get();
            CategoryRepository.getInstance().updateCategory(category.get().getId(), categoryReq, new ApiCallBack<Category>() {
                @Override
                public void onSuccess(Category category) {
                    List<Category> currentCategories = categories.getValue();

                    if (currentCategories != null) {
                        for(int i=0;i<currentCategories.size();i++)
                        {
                            if(currentCategories.get(i).getId().equals(categoryRemove.getId()))
                            {
                                currentCategories.remove(i);
                                currentCategories.add(categoryRemove);
                                break;
                            }
                        }
                        categories.setValue(currentCategories);
                        saveCategoriesToSharedPreferences(currentCategories);
                        showMessage("Sửa danh mục thành công");
                        resetData();
                    }
                }

                @Override
                public void onError(String message) {
                    showMessage(message);
                }
            });
        }
    }


    private void saveCategoriesToSharedPreferences(List<Category> categories) {
        SharedPreferencesManager.getInstance(context).saveList("categories",categories);
    }

    private synchronized List<Category> getCategoriesFromSharedPreferences() {
        Type type = new TypeToken<List<Category>>() {}.getType();
        List<Category> list = SharedPreferencesManager.getInstance(context).getList("categories", type);

        return list;
    }

    public void resetData(){
        nameCategory.set(null);
        parentCategory.set(null);
        iconCategory.set(null);
    }
}
