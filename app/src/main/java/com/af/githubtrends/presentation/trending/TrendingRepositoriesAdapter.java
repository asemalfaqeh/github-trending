package com.af.githubtrends.presentation.trending;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.af.githubtrends.R;
import com.af.githubtrends.databinding.RepositoryDetailsAdapterBinding;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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
        holder.bindView(repositoriesResponseArrayList.get(position), context);
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

        @SuppressLint("SetTextI18n")
        void bindView(SearchRepositoriesResponse.Items searchRepositoriesResponse,Context context){
            Glide.with(context).load(searchRepositoriesResponse.getOwner().getAvatar_url())
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(adapterBinding.imvInfo);
            if(searchRepositoriesResponse.getDescription() != null) {
                adapterBinding.tvDescription.setText(searchRepositoriesResponse.getDescription());
            }else{
                adapterBinding.tvDescription.setText("N/A");
            }
            adapterBinding.tvOwnerName.setText(searchRepositoriesResponse.getOwner().getLogin()+"");
            adapterBinding.tvRepoName.setText(searchRepositoriesResponse.getName()+"");
            adapterBinding.tvStarsCount.setText(searchRepositoriesResponse.getStargazers_count()+"");
        }

    }
}
