package com.af.githubtrends.data.network;
import io.reactivex.disposables.CompositeDisposable;

public class NetworkModule {
   public ApiProvider apiProvider = RetrofitInstance.getRetrofitInstance().getApiServicesProvider();
   public CompositeDisposable compositeDisposable = new CompositeDisposable();
}
