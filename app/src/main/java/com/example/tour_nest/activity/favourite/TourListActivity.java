package com.example.tour_nest.activity.favourite;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tour_nest.R;
import com.example.tour_nest.adapter.tour.FavoriteAdapter;
import com.example.tour_nest.base.BaseActivity;
import com.example.tour_nest.base.FirebaseCallback;
import com.example.tour_nest.databinding.ActivityLoginBinding;
import com.example.tour_nest.databinding.ActivityTourListBinding;
import com.example.tour_nest.model.Tour;
import com.example.tour_nest.model.tour.FavoriteItem;
import com.example.tour_nest.service.TourService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TourListActivity extends BaseActivity {

    private ActivityTourListBinding binding;

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

        if (region != null) {
            binding.tvTitle.setText(region);
            binding.bottomNavigation.setVisibility(View.INVISIBLE);
            TourService.search("region", region).onResult(new FirebaseCallback<List<Tour>>() {
                @Override
                public void onSuccess(List<Tour> result) {
                    for (Tour tour : result) {
                        favoriteList.add(new FavoriteItem(
                                tour.getThumbnail(),
                                tour.getName(),
                                "★★★★★"));
                    }

                    FavoriteAdapter adapter = new FavoriteAdapter(getBaseContext(), favoriteList);
//                    setupBottomNavigation(R.id.nav_favorite);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(TourListActivity.this, "loi TourListActivity ", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if (search != null) {
            binding.tvTitle.setText("Search : " +search);
            binding.bottomNavigation.setVisibility(View.INVISIBLE);
            TourService.search("name", search).onResult(new FirebaseCallback<List<Tour>>() {
                @Override
                public void onSuccess(List<Tour> result) {
                    for (Tour tour : result) {
                        favoriteList.add(new FavoriteItem(
                                tour.getThumbnail(),
                                tour.getName(),
                                "★★★★★"));
                    }

                    FavoriteAdapter adapter = new FavoriteAdapter(getBaseContext(), favoriteList);
//                    setupBottomNavigation(R.id.nav_favorite);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(TourListActivity.this, "loi TourListActivity ", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {

        }

    }
}