package com.af.githubtrends.data.repository_impl;

import android.content.Context;
import com.af.githubtrends.data.datasource.remote.SearchRepositoriesDatasource;
import com.af.githubtrends.data.network.HttpFailure;
import com.af.githubtrends.data.network.NetworkInfo;
import com.af.githubtrends.data.network.UniversalCallback;
import com.af.githubtrends.domain.model.request.SearchRepositoriesRequest;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import com.af.githubtrends.domain.repository.SearchRepository;


public class SearchRepositoryImpl implements SearchRepository {

    private final SearchRepositoriesDatasource searchDatasource;
    private final NetworkInfo networkInfo;

    public SearchRepositoryImpl(Context context) {
        searchDatasource = new SearchRepositoriesDatasource();
        networkInfo = new NetworkInfo(context);
    }

    @Override
    public void getSearchRepositories(UniversalCallback<SearchRepositoriesResponse> universalCallback, SearchRepositoriesRequest searchRepositoriesRequest) {
        if(networkInfo.isNetworkAvailable()){
            searchDatasource.searchRepositoriesDatasource(universalCallback,searchRepositoriesRequest);
        }else {
            universalCallback.onFailure(new HttpFailure("No Internet Connection",-1));
        }
    }

    @Override
    public void clear() {
        searchDatasource.clear();
    }
}
