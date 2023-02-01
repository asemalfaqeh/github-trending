package com.af.githubtrends.presentation.trending;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import com.af.githubtrends.R;
import com.af.githubtrends.data.network.HttpFailure;
import com.af.githubtrends.data.network.UniversalCallback;
import com.af.githubtrends.domain.model.request.SearchRepositoriesRequest;
import com.af.githubtrends.databinding.TrendingActivityBinding;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;


public class TrendingActivity extends AppCompatActivity {

    private TrendingViewModel homeViewModel;
    private TrendingActivityBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.trending_activity);
        homeViewModel = new ViewModelProvider(this).get(TrendingViewModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData(){
        SearchRepositoriesRequest searchRepositoriesRequest = new SearchRepositoriesRequest();
        searchRepositoriesRequest.setDate("created:>2023-01-01");
        searchRepositoriesRequest.setSort("stars");
        searchRepositoriesRequest.setOrder("desc");
        homeViewModel.getSearchRepositoriesViewModel(new UniversalCallback<SearchRepositoriesResponse>() {
            @Override
            public void onSuccess(SearchRepositoriesResponse searchRepositoriesResponse) {
            }

            @Override
            public void onFailure(HttpFailure httpFailure) {

            }
        },searchRepositoriesRequest);
    }
}