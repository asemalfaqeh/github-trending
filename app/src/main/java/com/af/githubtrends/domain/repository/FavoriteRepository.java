package com.af.githubtrends.domain.repository;


import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import java.util.ArrayList;

public interface FavoriteRepository {
    void saveFavorite(SearchRepositoriesResponse.Items repositories);
    void deleteFavorite(int id);
    ArrayList<SearchRepositoriesResponse.Items> getAllFavorites();
}
