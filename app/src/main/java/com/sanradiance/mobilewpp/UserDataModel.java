package com.sanradiance.mobilewpp;

public class UserDataModel {
    public String accessToken;
    public String userName;
    public int userId;
    public long userMobile;

    public UserDataModel(String accessToken, String userName, int userId, long userMobile) {
        this.accessToken = accessToken;
        this.userName = userName;
        this.userId = userId;
        this.userMobile = userMobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(int userMobile) {
        this.userMobile = userMobile;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
