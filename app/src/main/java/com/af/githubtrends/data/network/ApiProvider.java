package com.af.githubtrends.data.network;

import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiProvider {

    @GET("search/repositories")
    Single<SearchRepositoriesResponse> getSearchRepositoriesApi(@Query("order") String order, @Query("q") String q,@Query("sort") String sort);

}
