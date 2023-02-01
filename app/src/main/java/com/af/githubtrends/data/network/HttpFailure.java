package com.af.githubtrends.data.network;

import androidx.annotation.NonNull;

public class HttpFailure {

    private String message;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @NonNull
    @Override
    public String toString() {
        return "HttpFailure{" +
                "message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
