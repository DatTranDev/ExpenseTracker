package com.example.expensetracker.repository;

import com.example.expensetracker.api.RetrofitClient;
import com.example.expensetracker.api.Upload.UploadApi;
import com.example.expensetracker.api.ApiCallBack;
import com.example.expensetracker.api.Upload.UploadResponse;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class UploadRepository {
    private static UploadRepository uploadRepository;
    private UploadApi uploadApi;

    private UploadRepository() {
        uploadApi = RetrofitClient.getClient().create(UploadApi.class);
    }

    public static synchronized UploadRepository getInstance() {
        if (uploadRepository == null) {
            uploadRepository = new UploadRepository();
        }
        return uploadRepository;
    }

    public synchronized void uploadImage(String realPath, ApiCallBack<UploadResponse> callback) {
        File file = new File(realPath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        uploadApi.uploadImage(imagePart).enqueue(new Callback<UploadResponse>() {
            @Override
            public void onResponse(Call<UploadResponse> call, retrofit2.Response<UploadResponse> response) {
                if (response.isSuccessful()) {
                    UploadResponse uploadResponse = response.body();
                    callback.onSuccess(uploadResponse);
                } else {
                    if (response.code() == 400) {
                        try
                        {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            callback.onError(jObjError.getString("message"));
                        } catch (Exception e) {
                            callback.onError("Failed to upload image");
                        }
                    } else {
                        callback.onError("Failed to upload image");
                    }
                }
            }

            @Override
            public void onFailure(Call<UploadResponse> call, Throwable t) {
                callback.onError("Server error, please try again later: " + t.getMessage());
            }
        });
    }
}
