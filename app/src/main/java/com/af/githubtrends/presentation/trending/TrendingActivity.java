package com.af.githubtrends.presentation.trending;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.widget.SearchView;

import android.view.View;
import android.widget.Toast;

import com.af.githubtrends.R;
import com.af.githubtrends.data.network.HttpFailure;
import com.af.githubtrends.data.network.UniversalCallback;
import com.af.githubtrends.domain.model.request.SearchRepositoriesRequest;
import com.af.githubtrends.databinding.TrendingActivityBinding;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import com.af.githubtrends.presentation.favorite.FavoriteActivity;
import com.af.githubtrends.presentation.repository_details.RepositoryDetailsActivity;
import com.af.githubtrends.utils.DatesFrames;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import okhttp3.Headers;
/// AF Dev

public class TrendingActivity extends AppCompatActivity {

    private TrendingViewModel homeViewModel;
    private TrendingActivityBinding bindView;
    private final AtomicInteger nextPage = new AtomicInteger(1);
    private final AtomicInteger lastPage = new AtomicInteger(0);
    private boolean isLoading = true;
    private static final String TAG = "TrendingActivity";
    private String date;
    SearchView searchView = null;
    private RxSearchView rxSearchView;
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final ArrayList<SearchRepositoriesResponse.Items> itemsArrayList = new ArrayList<>();
    private final ArrayList<SearchRepositoriesResponse.Items> filteredArrList = new ArrayList<>();
    private final ArrayList<SearchRepositoriesResponse.Items> searchList = new ArrayList<>();

    private TrendingRepositoriesAdapter trendingRepositoriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView = TrendingActivityBinding.inflate(getLayoutInflater());
        setContentView(bindView.getRoot());
        homeViewModel = new ViewModelProvider(this).get(TrendingViewModel.class);
        loadData(DatesFrames.getInstance().getLastDay());
        makeListeners();

    }

    @Override
    protected void onResume() {super.onResume();}

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        homeViewModel.clear();
        compositeDisposable.clear();
        super.onDestroy();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) TrendingActivity.this.getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(TrendingActivity.this.getComponentName()));
        }
        if (searchView != null) {
            rxSearchView = new RxSearchView(searchView);
        }

        compositeDisposable.add(rxSearchView.getQueryTextObservable()
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> {
                    if (query.isEmpty()){
                        return;
                    }
                    filteredArrList.clear();
                    if(query.equals("null")){
                        filteredArrList.addAll(searchList);
                        itemsArrayList.clear();
                        itemsArrayList.addAll(filteredArrList);
                        trendingRepositoriesAdapter.notifyDataSetChanged();
                    }else if (query.length() > 3){
                        for (SearchRepositoriesResponse.Items item : searchList) {
                            if (item.getOwner().getLogin().toLowerCase().contains(query.toLowerCase())) {
                                filteredArrList.add(item);
                            }
                        }
                        itemsArrayList.clear();
                        itemsArrayList.addAll(filteredArrList);
                        trendingRepositoriesAdapter.notifyDataSetChanged();
                    }


                }));


        if (searchItem != null) {
            searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
                @Override
                public boolean onMenuItemActionExpand(@NonNull MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(@NonNull MenuItem item) {
                    itemsArrayList.clear();
                    itemsArrayList.addAll(searchList);
                    trendingRepositoriesAdapter.notifyDataSetChanged();
                    isLoading = true;
                    return true;
                }
            });
        }

        return super.onCreateOptionsMenu(menu);

    }

    private void makeListeners() {
        bindView.radioLd.setChecked(true);
        bindView.radioLd.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                reset();
                loadData(DatesFrames.getInstance().getLastDay());
            }
        });
        bindView.radioLw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                reset();
                loadData(DatesFrames.getInstance().getLastWeek());
            }
        });
        bindView.radioLm.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                reset();
                loadData(DatesFrames.getInstance().getLastMonth());
            }
        });
        bindView.fab.setOnClickListener(v -> startActivity(new Intent(this, FavoriteActivity.class)));
    }

    private void loadData(String date) {

        if (searchView != null){
            if (!searchView.isIconified()){
                return;
            }
        }

        this.date = date;
        showProgress();
        SearchRepositoriesRequest searchRepositoriesRequest = new SearchRepositoriesRequest();
        searchRepositoriesRequest.setDate("created:>" + date);
        searchRepositoriesRequest.setSort("stars");
        searchRepositoriesRequest.setOrder("desc");
        searchRepositoriesRequest.setPage(nextPage.get());

        homeViewModel.getSearchRepositoriesViewModel(new UniversalCallback<SearchRepositoriesResponse>() {
            @Override
            public void onSuccess(SearchRepositoriesResponse searchRepositoriesResponse, Headers headers) {
                hideProgress();
                itemsArrayList.addAll(searchRepositoriesResponse.getItems());
                searchList.addAll(itemsArrayList);
                String link = headers.get("Link");
                Log.d(TAG, "Link: " + link);
                if (nextPage.get() > 1) {
                    if (link != null) {
                        isLoading = nextPage.get() != lastPage.get();
                        getNextPage(link);
                    } else {
                        nextPage.getAndIncrement();
                    }

                    trendingRepositoriesAdapter.notifyItemInserted(itemsArrayList.size());
                } else {
                    initRv();
                    nextPage.incrementAndGet();
                    if (link != null) {
                        getLastPage(link);
                    }
                }

            }

            @Override
            public void onFailure(HttpFailure httpFailure) {
                hideProgress();
                if (httpFailure.getCode() == 403) {
                    isLoading = true;
                }else if (httpFailure.getCode() == -4) {
                    Toast.makeText(TrendingActivity.this, httpFailure.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, searchRepositoriesRequest);
    }

    private void initRv() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        trendingRepositoriesAdapter = new TrendingRepositoriesAdapter(itemsArrayList, items -> {
            Intent intent = new Intent(this, RepositoryDetailsActivity.class);
            intent.putExtra("repository_obj", items);
            startActivity(intent);
        });

        bindView.rvSearch.setAdapter(trendingRepositoriesAdapter);
        bindView.rvSearch.setHasFixedSize(true);
        bindView.rvSearch.setLayoutManager(layoutManager);
        bindView.rvSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                   // visibleItemsCount = layoutManager.getChildCount();
                   // totalItemsCount = layoutManager.getItemCount();
                   // pastVisibleItem = layoutManager.findFirstVisibleItemPosition();
                   // if ((visibleItemsCount + pastVisibleItem) >= totalItemsCount) {
                    if(!recyclerView.canScrollVertically(1)){
                        if (isLoading) {
                            isLoading = false;
                            loadData(date);
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

    private void reset() {
        nextPage.set(1);
        lastPage.set(0);
        itemsArrayList.clear();
    }

    private void hideProgress() {
        if (nextPage.get() == 1) {
            bindView.progress.setVisibility(View.GONE);
            bindView.rvSearch.setVisibility(View.VISIBLE);
        }
    }

    private void showProgress() {
        if (nextPage.get() == 1) {
            bindView.progress.setVisibility(View.VISIBLE);
            bindView.rvSearch.setVisibility(View.GONE);
        }
    }
}