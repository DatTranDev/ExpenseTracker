package com.example.expensetracker.viewmodel;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.BR;
import com.example.expensetracker.api.AppUser.UserCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.utils.Helper;

public class LoginViewModel extends BaseObservable {
    private String email;
    private Context context;
    private String password;
    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoggedIn = new MutableLiveData<>();
    private MutableLiveData<AppUser> appUserLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public MutableLiveData<AppUser> getAppUserLiveData() {
        return appUserLiveData;
    }
    public  MutableLiveData<Boolean> getIsLoggedIn() {
        return isLoggedIn;
    }
    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }
    @Bindable
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }
    @Bindable
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void onClickLogin(){
        if(Helper.isValidEmail(getEmail())){
            isLoading.postValue(true);
            AppUserRepository.getInstance().login(getEmail(), getPassword(), new UserCallBack() {
                @Override
                public void onSuccess(AppUser appUser) {
                    toastMessage.postValue("Welcome " + appUser.getUserName());
                    isLoggedIn.postValue(true);
                    appUserLiveData.postValue(appUser);
                    isLoading.postValue(false);
                }
                @Override
                public void onError(String message) {
                    toastMessage.postValue(message);
                    isLoading.postValue(false);
                }
            });
        } else {
            toastMessage.postValue("Vui lòng nhập đúng định dạng email");
        }
    }
}

