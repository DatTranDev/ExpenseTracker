package com.example.expensetracker.api.Upload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadApi {
    @Multipart
    @POST("upload/image")
    Call<UploadResponse> uploadImage(@Part MultipartBody.Part image);
}
