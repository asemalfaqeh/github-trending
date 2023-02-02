package com.af.githubtrends.data.network;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

class RetrofitInstance {

    private static Retrofit retrofit = null;
    private static final int timeout = 120;
    private static RetrofitInstance retrofitInstance;

    protected RetrofitInstance(){
        init();
    }

    private void init(){

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        //TODO HTTPLoggingInterceptor//
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BASIC);
        httpClientBuilder.interceptors().add(httpLoggingInterceptor);
        Gson gson = new GsonBuilder().setLenient().create();

        httpClientBuilder.connectTimeout(timeout, TimeUnit.SECONDS)
                .connectTimeout(timeout, TimeUnit.SECONDS) // 2 minutes
                .writeTimeout(timeout,TimeUnit.SECONDS)   // 2 minutes
                .readTimeout(timeout, TimeUnit.SECONDS);// 2 minutes

        final String baseUrl = "https://api.github.com/";
        retrofit = new Retrofit.Builder().baseUrl(baseUrl)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    protected static RetrofitInstance getRetrofitInstance() {
        if (retrofitInstance == null) {
            synchronized (RetrofitInstance.class) {
                return retrofitInstance = new RetrofitInstance();
            }
        }else {
            return retrofitInstance;
        }
    }

    protected ApiProvider getApiServicesProvider(){
        return retrofit.create(ApiProvider.class);
    }


}