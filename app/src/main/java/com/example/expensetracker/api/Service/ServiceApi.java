package com.example.expensetracker.api.Service;

import com.example.expensetracker.model.AppUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface    ServiceApi {
    @POST("service/sendemail")
    Call<MailSend> sendEmail(@Body AppUser appUser);
}
