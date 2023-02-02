package com.af.githubtrends.presentation.trending;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.af.githubtrends.data.network.HttpFailure;
import com.af.githubtrends.data.network.UniversalCallback;
import com.af.githubtrends.domain.model.request.SearchRepositoriesRequest;
import com.af.githubtrends.databinding.TrendingActivityBinding;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import okhttp3.Headers;


public class TrendingActivity extends AppCompatActivity {

    private TrendingViewModel homeViewModel;
    private TrendingActivityBinding bindView;
    private final AtomicInteger nextPage = new AtomicInteger(1);
    private final AtomicInteger lastPage = new AtomicInteger(0);
    private boolean isLoading = true;
    private static final String TAG = "TrendingActivity";
    int pastVisibleItem, visibleItemsCount, totalItemsCount;
    private final ArrayList<SearchRepositoriesResponse.Items> itemsArrayList = new ArrayList<>();
    private TrendingRepositoriesAdapter trendingRepositoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView = TrendingActivityBinding.inflate(getLayoutInflater());
        setContentView(bindView.getRoot());
        homeViewModel = new ViewModelProvider(this).get(TrendingViewModel.class);
        loadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        homeViewModel.clear();
    }

    private void loadData() {

        SearchRepositoriesRequest searchRepositoriesRequest = new SearchRepositoriesRequest();
        searchRepositoriesRequest.setDate("created:>2023-01-01");
        searchRepositoriesRequest.setSort("stars");
        searchRepositoriesRequest.setOrder("desc");
        searchRepositoriesRequest.setPage(nextPage.get());

        homeViewModel.getSearchRepositoriesViewModel(new UniversalCallback<SearchRepositoriesResponse>() {
            @Override
            public void onSuccess(SearchRepositoriesResponse searchRepositoriesResponse, Headers headers) {
                itemsArrayList.addAll(searchRepositoriesResponse.getItems());
                String link = headers.get("Link");
                Log.d(TAG, "Link: " + link);
                if (nextPage.get() > 1) {
                    if (link != null) {
                        isLoading = nextPage.get() != lastPage.get();
                        getNextPage(link);
                    }else {
                        nextPage.getAndIncrement();
                    }

                    trendingRepositoriesAdapter.notifyItemInserted(itemsArrayList.size());
                } else {
                    initRv(itemsArrayList);
                    nextPage.incrementAndGet();
                    if (link != null) {
                        getLastPage(link);
                    }
                }

            }

            @Override
            public void onFailure(HttpFailure httpFailure) {
                Toast.makeText(TrendingActivity.this, httpFailure.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, searchRepositoriesRequest);
    }

    private void initRv(List<SearchRepositoriesResponse.Items> itemsArrayList) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        trendingRepositoriesAdapter = new TrendingRepositoriesAdapter((ArrayList<SearchRepositoriesResponse.Items>) itemsArrayList);
        bindView.rvSearch.setAdapter(trendingRepositoriesAdapter);
        bindView.rvSearch.setHasFixedSize(true);
        bindView.rvSearch.setLayoutManager(layoutManager);
        bindView.rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    //check for scroll down
                    visibleItemsCount = layoutManager.getChildCount();
                    totalItemsCount = layoutManager.getItemCount();
                    pastVisibleItem = layoutManager.findFirstVisibleItemPosition();

                    if (isLoading) {
                        if ((visibleItemsCount + pastVisibleItem) >= totalItemsCount) {
                            isLoading = false;
                            Log.v("...", "Last Item Wow !");
                            loadData();
                        }
                    }
                }
            }
        });
    }

    private void getLastPage(String link) {

        String[] parts = link.split(",");

        for (String part : parts) {
            int relIndex = part.indexOf("rel=") + 5;
            int endRelIndex = part.lastIndexOf("\"");
            int pageIndex = part.indexOf("page=") + 5;
            int endPageIndex = part.indexOf(">;");

            if (endRelIndex != -1 && endPageIndex != -1) {
                String rel = part.substring(relIndex, endRelIndex);

                if (rel.equals("last")) {
                    String page = part.substring(pageIndex, endPageIndex);
                    lastPage.set(Integer.parseInt(page));
                    break;
                }

            }
        }
    }

    private void getNextPage(String link) {
        String[] parts = link.split(",");
        for (String part : parts) {
            int relIndex = part.indexOf("rel=") + 5;
            int endRelIndex = part.lastIndexOf("\"");
            int pageIndex = part.indexOf("page=") + 5;
            int endPageIndex = part.indexOf(">;");

            if (endRelIndex != -1 && endPageIndex != -1) {
                String rel = part.substring(relIndex, endRelIndex);

                if (rel.equals("next")) {
                    String page = part.substring(pageIndex, endPageIndex);
                    nextPage.set(Integer.parseInt(page));
                    break;
                }

            }
        }
    }


}