package com.af.githubtrends.presentation.favorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import com.af.githubtrends.databinding.ActivityFavoriteBinding;
import com.af.githubtrends.presentation.repository_details.FavoriteViewModel;


public class FavoriteActivity extends AppCompatActivity {

    private FavoriteViewModel favoriteViewModel;
    private ActivityFavoriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
    }

    private void initRv(){

    }

}