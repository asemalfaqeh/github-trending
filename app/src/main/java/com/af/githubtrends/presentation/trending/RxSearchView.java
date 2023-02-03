package com.af.githubtrends.presentation.trending;

import android.util.Log;

import androidx.appcompat.widget.SearchView;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxSearchView {
    private final PublishSubject<String> subject = PublishSubject.create();
    private static final String TAG = "RxSearchView";
    public RxSearchView(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                subject.onNext(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    subject.onNext("null");
                }else {
                    subject.onNext(newText);
                }
                Log.d(TAG, "onQueryTextChange: " + newText);
                return true;
            }
        });
    }

    public Observable<String> getQueryTextObservable() {
        return subject;
    }
}
