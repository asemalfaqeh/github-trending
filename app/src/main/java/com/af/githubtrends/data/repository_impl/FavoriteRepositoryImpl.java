package com.af.githubtrends.data.repository_impl;

import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import com.af.githubtrends.domain.repository.FavoriteRepository;
import java.util.ArrayList;

public class FavoriteRepositoryImpl implements FavoriteRepository {

    @Override
    public void saveFavorite(SearchRepositoriesResponse repositories) {

    }

    @Override
    public void deleteFavorite() {

    }

    @Override
    public ArrayList<SearchRepositoriesResponse> getAllFavorites() {
        return null;
    }

}
