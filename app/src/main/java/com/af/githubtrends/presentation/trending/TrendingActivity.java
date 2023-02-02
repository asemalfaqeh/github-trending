package com.af.githubtrends.presentation.trending;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import com.af.githubtrends.data.network.HttpFailure;
import com.af.githubtrends.data.network.UniversalCallback;
import com.af.githubtrends.domain.model.request.SearchRepositoriesRequest;
import com.af.githubtrends.databinding.TrendingActivityBinding;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import java.util.ArrayList;
import java.util.List;


public class TrendingActivity extends AppCompatActivity {

    private TrendingViewModel homeViewModel;
    private TrendingActivityBinding mainBinding;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = TrendingActivityBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());
        homeViewModel = new ViewModelProvider(this).get(TrendingViewModel.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData(){

        SearchRepositoriesRequest searchRepositoriesRequest = new SearchRepositoriesRequest();
        searchRepositoriesRequest.setDate("created:>2023-01-23");
        searchRepositoriesRequest.setSort("stars");
        searchRepositoriesRequest.setOrder("desc");
        searchRepositoriesRequest.setPage(page);

        homeViewModel.getSearchRepositoriesViewModel(new UniversalCallback<SearchRepositoriesResponse>() {
            @Override
            public void onSuccess(SearchRepositoriesResponse searchRepositoriesResponse) {
                if (!searchRepositoriesResponse.getIncomplete_results()) {
                    initRv(searchRepositoriesResponse.getItems());
                }
            }

            @Override
            public void onFailure(HttpFailure httpFailure) {}
        }, searchRepositoriesRequest);
    }

    private void initRv(List<SearchRepositoriesResponse.Items> itemsArrayList){
        TrendingRepositoriesAdapter trendingRepositoriesAdapter = new TrendingRepositoriesAdapter((ArrayList<SearchRepositoriesResponse.Items>) itemsArrayList);
        mainBinding.rvSearch.setAdapter(trendingRepositoriesAdapter);
        mainBinding.rvSearch.setHasFixedSize(true);
    }

}