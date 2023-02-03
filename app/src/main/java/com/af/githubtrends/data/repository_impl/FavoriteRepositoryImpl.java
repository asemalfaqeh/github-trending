package com.af.githubtrends.data.repository_impl;

import android.content.Context;

import com.af.githubtrends.data.datasource.local.FavoriteLocalDataSource;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import com.af.githubtrends.domain.repository.FavoriteRepository;
import java.util.ArrayList;

public class FavoriteRepositoryImpl implements FavoriteRepository {

    private final FavoriteLocalDataSource favoriteLocalDataSource;

    public FavoriteRepositoryImpl(Context context) {
        favoriteLocalDataSource = new FavoriteLocalDataSource(context);
    }

    @Override
    public void saveFavorite(SearchRepositoriesResponse.Items repositories) {
        favoriteLocalDataSource.saveFavoriteRepoItem(repositories);
    }

    @Override
    public void deleteFavorite(int id) {
        favoriteLocalDataSource.removeItem(id);
    }

    @Override
    public ArrayList<SearchRepositoriesResponse.Items> getAllFavorites() {
        return favoriteLocalDataSource.getAllFavoriteItems();
    }

}
