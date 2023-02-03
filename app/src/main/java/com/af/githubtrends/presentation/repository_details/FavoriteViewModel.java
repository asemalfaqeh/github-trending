package com.af.githubtrends.presentation.repository_details;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.af.githubtrends.data.repository_impl.FavoriteRepositoryImpl;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import com.af.githubtrends.domain.repository.FavoriteRepository;

import java.util.ArrayList;

public class FavoriteViewModel extends AndroidViewModel{

    private final FavoriteRepository favoriteRepository;

    public FavoriteViewModel(@NonNull Application application) {
        super(application);
        favoriteRepository = new FavoriteRepositoryImpl(application);
    }


    public void saveFavoriteViewModel(SearchRepositoriesResponse.Items repositories) {
        favoriteRepository.saveFavorite(repositories);
    }

    public void deleteFavoriteViewModel(int id) {
        favoriteRepository.deleteFavorite(id);
    }

    public ArrayList<SearchRepositoriesResponse.Items> getAllFavorites() {
        return favoriteRepository.getAllFavorites();
    }

}
