package com.sanradiance.mobilewpp.ImageUploadHelpers;


public class APIUtils {
    private APIUtils(){
    }

    public static final String API_URL = "https://domytaxonline.com.au/shuddha-neeru-test/public/api/auth/upload/";

    public static FileService getFileService(){
        return RetrofitClient.getClient(API_URL).create(FileService.class);
    }
}
