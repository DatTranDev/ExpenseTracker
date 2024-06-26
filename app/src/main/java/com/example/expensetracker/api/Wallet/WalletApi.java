package com.example.expensetracker.api.Wallet;

import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.TransactionExp;
import com.example.expensetracker.model.UserWallet;
import com.example.expensetracker.model.Wallet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WalletApi {
    @POST("wallet/add")
    Call<DataResponse<Wallet>> addWallet(@Body WalletReq walletReq);
    @PATCH("wallet/update/{id}")
    Call<DataResponse<Wallet>> updateWallet(@Path("id") String id, @Body Wallet wallet);
    @HTTP(method = "DELETE", path = "wallet/delete", hasBody = true)
    Call<DataResponse<Wallet>> deleteWallet(@Body UserWallet userWallet);
    @PATCH("wallet/addmember")
    Call<DataResponse<Wallet>> addMember(@Body AddMemberReq addMemberReq);
    @PATCH("wallet/removemember")
    Call<DataResponse<Wallet>> removeMember(@Body RemoveMemberReq removeMemberReq);
    @GET("wallet/gettransaction/{id}")
    Call<DataResponse<List<TransactionExp>>> getTransaction(@Path("id") String id);

}
