package com.af.githubtrends.data.network;

import okhttp3.Headers;

public interface UniversalCallback<T> {
    void onSuccess(T t, Headers headers);
    void onFailure(HttpFailure httpFailure);
}
