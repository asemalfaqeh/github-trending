package com.af.githubtrends.presentation.repository_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;
import com.af.githubtrends.R;
import com.af.githubtrends.databinding.ActivityResposiotyDetailsBinding;
import com.af.githubtrends.domain.model.response.SearchRepositoriesResponse;
import com.af.githubtrends.presentation.viewmodel.FavoriteViewModel;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class RepositoryDetailsActivity extends AppCompatActivity {

    private SearchRepositoriesResponse.Items item;
    private ActivityResposiotyDetailsBinding binding;
    private FavoriteViewModel favoriteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResposiotyDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        favoriteViewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
        item = (SearchRepositoriesResponse.Items) getIntent().getSerializableExtra("repository_obj");
        bindView();
        makeListeners();
    }

    @SuppressLint("SetTextI18n")
    private void bindView(){
        binding.tvOwnerName.setText(item.getOwner().getLogin());
        binding.tvRepoName.setText(item.getName()+"");
        binding.tvDescription.setText(item.getDescription()+"");
        Glide.with(this).load(item.getOwner().getAvatar_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.placeholder)
                .into(binding.imvInfo);
        binding.tvForks.setText("Forks: " +item.getForks()+"");
        binding.tvStarsCount.setText(item.getStargazers_count()+"");
        binding.tvLanguage.setText("Language: "+item.getLanguage()+"");
        binding.tvUrl.setText(item.getHtml_url()+"");
        binding.tvDate.setText(item.getCreated_at()+"");

    }

    private void makeListeners(){
        binding.viewBack.setOnClickListener(v -> finish());
        binding.tvUrl.setOnClickListener(v -> openUrlBrowser(item.getHtml_url()+""));
        if (favoriteViewModel.isItemExistsViewModel(item.getId())){
            binding.viewFav.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_500));
        }else {
            binding.viewFav.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_grey));
        }
        binding.viewFav.setOnClickListener(v -> {
            if (favoriteViewModel.isItemExistsViewModel(item.getId())) {
                favoriteViewModel.deleteFavoriteViewModel(item.getId());
                binding.viewFav.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.dark_grey));
            } else {
                favoriteViewModel.saveFavoriteViewModel(item);
                binding.viewFav.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.purple_500));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void openUrlBrowser(String url){
        try {
            Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browser);
        }catch (Exception e){
            Toast.makeText(this, e.getMessage()+"", Toast.LENGTH_SHORT).show();
        }
    }

}