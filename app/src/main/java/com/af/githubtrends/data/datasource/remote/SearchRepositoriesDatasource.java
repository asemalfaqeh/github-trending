package com.af.githubtrends.data.datasource.remote;


import android.util.Log;

import androidx.annotation.NonNull;

import com.af.githubtrends.data.network.HttpFailure;
import com.af.githubtrends.data.network.NetworkModule;
import com.af.githubtrends.data.network.UniversalCallback;
import com.af.githubtrends.domain.model.request.SearchRepositoriesRequest;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import retrofit2.Response;


public class SearchRepositoriesDatasource extends NetworkModule{
    private static final String TAG = "SearchRepositoriesData";
    public void searchRepositoriesDatasource(UniversalCallback<SearchRepositoriesResponse> callback, @NonNull SearchRepositoriesRequest searchRepositoriesRequest) {
        compositeDisposable.add(apiProvider.getSearchRepositoriesApi(searchRepositoriesRequest.getOrder(),
                        searchRepositoriesRequest.getDate(),searchRepositoriesRequest.getSort(), searchRepositoriesRequest.getPage()+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> callback.onFailure(new HttpFailure(throwable.getMessage(),-3)))
                .subscribeWith(new DisposableSingleObserver<Response<SearchRepositoriesResponse>>() {
                    @Override
                    public void onSuccess(Response<SearchRepositoriesResponse> response) {
                        if (response.code() == 200){
                            callback.onSuccess(response.body(), response.headers());
                        }else if (response.code() == 403){
                            if (response.errorBody() != null) {
                                try {
                                    String errorBody = response.errorBody().string();
                                    JSONObject jsonObject = new JSONObject(errorBody);
                                    callback.onFailure(new HttpFailure(jsonObject.getString("message"), response.code()));
                                } catch (IOException | JSONException e) {
                                    callback.onFailure(new HttpFailure(e.getMessage(), -2));
                                }
                            }
                        }else {
                            callback.onFailure(new HttpFailure("No Result",-2));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: " + e.getMessage());
                        HttpException exception = (HttpException) e;
                        callback.onFailure(new HttpFailure(exception.getMessage(), exception.code()));
                    }
                }));
    }

    public void clear(){
        compositeDisposable.clear();
    }

}


