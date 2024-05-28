package com.example.expensetracker.repository;

import com.example.expensetracker.api.RetrofitClient;
import com.example.expensetracker.api.Service.ServiceApi;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.model.AppUser;
import com.example.expensetracker.api.Service.MailSend;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceRepository {
    private static ServiceRepository serviceRepository;
    private ServiceApi serviceApi;

    private ServiceRepository() {
        serviceApi = RetrofitClient.getClient().create(ServiceApi.class);
    }

    public static synchronized ServiceRepository getInstance() {
        if (serviceRepository == null) {
            serviceRepository = new ServiceRepository();
        }
        return serviceRepository;
    }

    public synchronized void sendEmail(AppUser appUser, ApiCallBack<MailSend> callback) {
        Call<MailSend> call = serviceApi.sendEmail(appUser);
        call.enqueue(new Callback<MailSend>() {
            @Override
            public void onResponse(Call<MailSend> call, Response<MailSend> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    if (response.code() == 400) {
                        try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to send email");
                        }
                    } else {
                        callback.onError("Failed to send email");
                    }
                    callback.onError("Failed to send email");
                }
            }

            @Override
            public void onFailure(Call<MailSend> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }
}
