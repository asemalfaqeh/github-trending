package com.af.githubtrends.presentation.trending;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import com.af.githubtrends.data.network.UniversalCallback;
import com.af.githubtrends.data.repository.SearchRepositoryImpl;
import com.af.githubtrends.domain.model.request.SearchRepositoriesRequest;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import com.af.githubtrends.domain.repository.SearchRepository;


public class TrendingViewModel extends AndroidViewModel {

    private final SearchRepository searchRepository;

    public TrendingViewModel(Application application) {
        super(application);
        searchRepository = new SearchRepositoryImpl(application);
    }

    public void getSearchRepositoriesViewModel(UniversalCallback<SearchRepositoriesResponse> universalCallback, SearchRepositoriesRequest searchRepositoriesRequest){
        searchRepository.getSearchRepositories(universalCallback, searchRepositoriesRequest);
    }
}


