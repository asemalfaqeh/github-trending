package com.af.githubtrends.presentation.favorite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.af.githubtrends.databinding.ActivityFavoriteBinding;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import com.af.githubtrends.presentation.repository_details.RepositoryDetailsActivity;
import com.af.githubtrends.presentation.trending.TrendingRepositoriesAdapter;
import com.af.githubtrends.presentation.viewmodel.FavoriteViewModel;


public class FavoriteActivity extends AppCompatActivity {

    private FavoriteViewModel favoriteViewModel;
    private ActivityFavoriteBinding binding;

    ///Asem Alfaqeh
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        makeListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRv();
    }

    private void initRv(){

        if (favoriteViewModel.getAllFavorites().size() == 0){
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.rvFavorite.setVisibility(View.GONE);
            return;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        TrendingRepositoriesAdapter trendingRepositoriesAdapter = new TrendingRepositoriesAdapter(favoriteViewModel.getAllFavorites(), new TrendingRepositoriesAdapter.TrendingItemListener() {
            @Override
            public void onItemClickListener(SearchRepositoriesResponse.Items items) {
                Intent intent = new Intent(FavoriteActivity.this, RepositoryDetailsActivity.class);
                intent.putExtra("repository_obj", items);
                startActivity(intent);
            }
        });
        binding.rvFavorite.setAdapter(trendingRepositoriesAdapter);
        binding.rvFavorite.setLayoutManager(layoutManager);
    }

    private void makeListeners(){
        binding.viewBack.setOnClickListener(v -> finish());
    }

}