package com.example.expensetracker.api.Wallet;

import com.example.expensetracker.api.DataResponse;
import com.example.expensetracker.model.Wallet;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WalletApi {
    @POST("wallet/add")
    Call<DataResponse<Wallet>> addWallet(WalletReq walletReq);
    @PATCH("wallet/update/{id}")
    Call<DataResponse<Wallet>> updateWallet(@Path("id") String id, Wallet wallet);
    @DELETE("wallet/delete/{id}")
    Call<DataResponse<Wallet>> deleteWallet(@Path("id") String id);
    @PATCH("wallet/addmember")
    Call<DataResponse<Wallet>> addMember(AddMemberReq addMemberReq);
    @PATCH("wallet/removemember")
    Call<DataResponse<Wallet>> removeMember(RemoveMemberReq removeMemberReq);


}
