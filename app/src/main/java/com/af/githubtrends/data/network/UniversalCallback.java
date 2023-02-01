package com.af.githubtrends.data.network;

public interface UniversalCallback<T> {
    void onSuccess(T t);
    void onFailure(HttpFailure httpFailure);
}
