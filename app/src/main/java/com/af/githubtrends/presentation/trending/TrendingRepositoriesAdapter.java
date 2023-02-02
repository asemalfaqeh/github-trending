package com.af.githubtrends.presentation.trending;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.af.githubtrends.R;
import com.af.githubtrends.databinding.RepositoryDetailsAdapterBinding;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import java.util.ArrayList;


public class TrendingRepositoriesAdapter extends RecyclerView.Adapter<TrendingRepositoriesAdapter.ViewHolder> {

    private Context context;
    private final ArrayList<SearchRepositoriesResponse.Items> repositoriesResponseArrayList;

    public TrendingRepositoriesAdapter(ArrayList<SearchRepositoriesResponse.Items> searchRepositoriesResponses) {
        this.repositoriesResponseArrayList = searchRepositoriesResponses;
    }

    @NonNull
    @Override
    public TrendingRepositoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.repository_details_adapter, parent, false);
        return new ViewHolder(RepositoryDetailsAdapterBinding.bind(view));
    }

    @Override
    public void onBindViewHolder(@NonNull TrendingRepositoriesAdapter.ViewHolder holder, int position) {
        holder.bindView(repositoriesResponseArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return repositoriesResponseArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RepositoryDetailsAdapterBinding adapterBinding;
        public ViewHolder(@NonNull RepositoryDetailsAdapterBinding itemView) {
            super(itemView.getRoot());
            this.adapterBinding = itemView;
        }

        void bindView(SearchRepositoriesResponse.Items searchRepositoriesResponse){
            adapterBinding.tvName.setText(searchRepositoriesResponse.getOwner().getLogin());
        }

    }
}
