package com.sanradiance.mobilewpp.ImageUploadHelpers;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface FileService {
    @Multipart
    @Headers("X-Requested-With:XMLHttpRequest")
    @POST("file")
    Call<ServerResponse> upload(@Part MultipartBody.Part image);
}
