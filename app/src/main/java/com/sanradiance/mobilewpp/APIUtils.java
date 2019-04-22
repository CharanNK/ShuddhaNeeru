package com.sanradiance.mobilewpp;


public class APIUtils {
    private APIUtils(){
    }

    public static final String API_URL = "https://domytaxonline.com.au/shuddha-neeru/public/api/auth/upload/";

    public static FileService getFileService(){
        return RetrofitClient.getClient(API_URL).create(FileService.class);
    }
}
