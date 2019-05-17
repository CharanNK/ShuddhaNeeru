package com.sanradiance.mobilewpp.ImageUploadHelpers;

public class ServerResponse {
    String message;
    int file_id;
    String success;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFileId() {
        return file_id;
    }

    public void setFileId(int fileId) {
        this.file_id = fileId;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
