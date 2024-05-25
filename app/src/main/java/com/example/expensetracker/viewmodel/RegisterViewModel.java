package com.example.expensetracker.viewmodel;

import androidx.databinding.BaseObservable;
import androidx.lifecycle.MutableLiveData;

import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.repository.AppUserRepository;
import com.example.expensetracker.utils.Helper;

public class RegisterViewModel extends BaseObservable {
    private String email;
    private String password;
    private String userName;
    private boolean isCheck;

    private MutableLiveData<String> toastMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRegistered = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public boolean getIsCheck() {
        return isCheck;
    }
    public void setIsCheck(boolean check) {
        isCheck = check;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public MutableLiveData<String> getToastMessage() {
        return toastMessage;
    }
    public MutableLiveData<Boolean> getIsRegistered() {
        return isRegistered;
    }
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    public void onClickRegister(){
        if(Helper.isValidEmail(getEmail())){
            if (getPassword().length() < 6){
                toastMessage.postValue("Mật khẩu phải có ít nhất 6 ký tự");
                return;
            }
            if (isCheck == false){
                toastMessage.postValue("Vui lòng đồng ý với điều khoản sử dụng");
                return;
            }
            isLoading.postValue(true);
            AppUser appUser = new AppUser();
            appUser.setEmail(getEmail());
            appUser.setPassword(getPassword());
            appUser.setUserName(getUserName());
            AppUserRepository.getInstance().register(appUser, new ApiCallBack<AppUser>() {
                @Override
                public void onSuccess(AppUser appUser) {
                    toastMessage.postValue("Đăng ký thành công");
                    isRegistered.postValue(true);
                    isLoading.postValue(false);
                }
                @Override
                public void onError(String message) {
                    toastMessage.postValue(message);
                    isLoading.postValue(false);
                }
            });
        }else{
            toastMessage.postValue("Vui lòng nhập đúng định dạng email");
        }
    }
}
