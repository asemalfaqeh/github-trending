package com.af.githubtrends.domain.repository;

import com.af.githubtrends.data.network.UniversalCallback;
import com.af.githubtrends.domain.model.request.SearchRepositoriesRequest;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;

public interface SearchRepository {
    void getSearchRepositories(UniversalCallback<SearchRepositoriesResponse> universalCallback, SearchRepositoriesRequest searchRepositoriesRequest);
}
