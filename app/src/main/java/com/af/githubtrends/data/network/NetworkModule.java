package com.af.githubtrends.data.network;
import io.reactivex.disposables.CompositeDisposable;

public class NetworkModule {
   public final ApiProvider apiProvider = RetrofitInstance.getRetrofitInstance().getApiServicesProvider();
   public final CompositeDisposable compositeDisposable = new CompositeDisposable();
}
