package com.af.githubtrends.data.datasource.remote;


import com.af.githubtrends.data.network.HttpFailure;
import com.af.githubtrends.data.network.NetworkModule;
import com.af.githubtrends.data.network.UniversalCallback;
import com.af.githubtrends.domain.model.request.SearchRepositoriesRequest;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;


public class SearchRepositoriesDatasource extends NetworkModule{

    public void searchRepositoriesDatasource(UniversalCallback<SearchRepositoriesResponse> callback, SearchRepositoriesRequest searchRepositoriesRequest) {
        compositeDisposable.add(apiProvider.getSearchRepositoriesApi(searchRepositoriesRequest.getOrder(),
                        searchRepositoriesRequest.getDate(),searchRepositoriesRequest.getSort()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<SearchRepositoriesResponse>() {
                    @Override
                    public void onSuccess(SearchRepositoriesResponse searchRepositoriesResponse) {
                        callback.onSuccess(searchRepositoriesResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        HttpException exception = (HttpException) e;
                        HttpFailure httpFailure = new HttpFailure();
                        httpFailure.setMessage(exception.getMessage());
                        httpFailure.setCode(exception.code());
                        callback.onFailure(httpFailure);
                    }
                }));
    }

    public void clear(){
        compositeDisposable.clear();
    }

}


