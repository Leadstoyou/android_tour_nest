package com.example.tour_nest.activity.favourite;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.activity.auth.LoginActivity;
import com.example.tour_nest.adapter.tour.FavoriteAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.databinding.ActivityLoginBinding;
import com.example.tour_nest.databinding.ActivityTourListBinding;
import com.example.tour_nest.model.Favourite;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.model.User;
import com.example.tour_nest.model.tour.FavoriteItem;
import com.example.tour_nest.service.FavouriteService;
import com.example.tour_nest.service.TourService;
import com.example.tour_nest.util.LogUtil;
import com.example.tour_nest.util.SharedPrefHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TourListActivity extends BaseActivity {

    private ActivityTourListBinding binding;
    private User user = new User();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTourListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        RecyclerView recyclerView = findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        List<FavoriteItem> favoriteList = new ArrayList<>();
        binding.btnBack.setOnClickListener(v -> finish());
        String region = getIntent().getStringExtra("region");
        String search = getIntent().getStringExtra("search");
        LogUtil.logMessage("region :: " + region);
        LogUtil.logMessage("search :: " + search);
        user = SharedPrefHelper.getUser(getBaseContext());
        if (user == null) {
            startActivity(new Intent(TourListActivity.this, LoginActivity.class));
        }
        if (region != null) {
            binding.tvTitle.setText(region);
            binding.bottomNavigation.setVisibility(View.INVISIBLE);
            TourService.search("region", region).onResult(new FirebaseCallback<List<Tour>>() {
                @Override
                public void onSuccess(List<Tour> result) {
                    for (Tour tour : result) {
                        favoriteList.add(new FavoriteItem(tour.getId(),
                                tour.getThumbnail(),
                                tour.getName(),
                                "★★★★★"));
                    }
                    LogUtil.logMessage("favoriteList :: " + favoriteList);
                    FavoriteAdapter adapter = new FavoriteAdapter(getBaseContext(), favoriteList);
                    runOnUiThread(() -> recyclerView.setAdapter(adapter));
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(TourListActivity.this, "loi TourListActivity ", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (search != null) {
            binding.tvTitle.setText("Search : " + search);
            binding.bottomNavigation.setVisibility(View.INVISIBLE);
            TourService.search("name", search).onResult(new FirebaseCallback<List<Tour>>() {
                @Override
                public void onSuccess(List<Tour> result) {
                    for (Tour tour : result) {
                        favoriteList.add(new FavoriteItem(tour.getId(),
                                tour.getThumbnail(),
                                tour.getName(),
                                "★★★★★"));
                    }

                    FavoriteAdapter adapter = new FavoriteAdapter(TourListActivity.this, favoriteList);
                    runOnUiThread(() -> recyclerView.setAdapter(adapter));
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(TourListActivity.this, "loi TourListActivity ", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            binding.btnBack.setVisibility(View.INVISIBLE);
            if (user == null || user.getId() == null) {
                LogUtil.logMessage("User is null or invalid");
                startActivity(new Intent(TourListActivity.this, LoginActivity.class));
                return;
            }

            FavouriteService.getByUserId(user.getId()).onResult(new FirebaseCallback<Favourite>() {
                @Override
                public void onSuccess(Favourite result) {
                    if (result == null || result.getFavouriteTours() == null || result.getFavouriteTours().isEmpty()) {
                        LogUtil.logMessage("No favorite tours found");
                        runOnUiThread(() -> {
                            recyclerView.setAdapter(new FavoriteAdapter(TourListActivity.this, favoriteList)); // Hiển thị danh sách rỗng
                            setupBottomNavigation(R.id.nav_favorite);
                        });
                        return;
                    }

                    TourService.getRef().getByIds(result.getFavouriteTours(), new FirebaseCallback<List<Tour>>() {
                        @Override
                        public void onSuccess(List<Tour> result) {
                            for (Tour tour : result) {
                                favoriteList.add(new FavoriteItem(tour.getId(),
                                        tour.getThumbnail(),
                                        tour.getName(),
                                        "★★★★★"));
                            }
                            FavoriteAdapter adapter = new FavoriteAdapter(TourListActivity.this, favoriteList);
                            runOnUiThread(() -> {
                                setupBottomNavigation(R.id.nav_favorite);
                                recyclerView.setAdapter(adapter);
                            });
                        }

                        @Override
                        public void onFailure(Exception e) {
                            LogUtil.logMessage("Error fetching tours: " + e.getMessage());
                            Toast.makeText(TourListActivity.this, "Failed to load favorite tours", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(Exception e) {
                    LogUtil.logMessage("Error fetching favorites: " + e.getMessage());
                    Toast.makeText(TourListActivity.this, "Failed to load favorites", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}